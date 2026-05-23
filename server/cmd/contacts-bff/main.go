package main

import (
	"fmt"
	"io"
	"log"
	"net"
	"net/http"
	"os"
	"strings"
	"time"

	"github.com/wastingnotime/contacts-mobile/server/internal/application"
	"github.com/wastingnotime/contacts-mobile/server/internal/config"
	"github.com/wastingnotime/contacts-mobile/server/internal/infrastructure/contactsapi"
	"github.com/wastingnotime/contacts-mobile/server/internal/observability"
	transporthttp "github.com/wastingnotime/contacts-mobile/server/internal/transport/http"
)

func main() {
	if len(os.Args) > 1 && os.Args[1] == "healthcheck" {
		if err := runHealthcheck(); err != nil {
			log.Print(err)
			os.Exit(1)
		}
		return
	}

	if err := run(); err != nil {
		log.Fatal(err)
	}
}

func run() error {
	cfg, err := config.LoadFromEnv()
	if err != nil {
		return err
	}

	repository, err := contactsapi.NewClient(
		cfg.ContactsAPIBaseURL,
		cfg.AuthSubject,
		cfg.AuthRoles,
	)
	if err != nil {
		return err
	}

	runtimeObservability, err := observability.NewFromEnv()
	if err != nil {
		return err
	}
	defer func() {
		if shutdownErr := runtimeObservability.Shutdown(); shutdownErr != nil {
			log.Printf("failed to shut down observability: %v", shutdownErr)
		}
	}()

	service := application.NewService(repository)
	server, err := transporthttp.NewServer(service, cfg.APIPathPrefix, runtimeObservability.Logger())
	if err != nil {
		return err
	}

	runtimeObservability.Logger().Info("contacts BFF listening", "listen_addr", cfg.ListenAddr)
	if err := http.ListenAndServe(cfg.ListenAddr, server.Handler()); err != nil {
		return err
	}
	return nil
}

func runHealthcheck() error {
	client := &http.Client{Timeout: 2 * time.Second}
	request, err := http.NewRequest(http.MethodGet, healthcheckURL(), nil)
	if err != nil {
		return fmt.Errorf("build healthcheck request: %w", err)
	}

	response, err := client.Do(request)
	if err != nil {
		return fmt.Errorf("perform healthcheck request: %w", err)
	}
	defer response.Body.Close()

	_, _ = io.Copy(io.Discard, response.Body)

	if response.StatusCode != http.StatusOK {
		return fmt.Errorf("healthcheck returned %s", response.Status)
	}

	return nil
}

func healthcheckURL() string {
	listenAddr := strings.TrimSpace(os.Getenv("CONTACTS_BFF_LISTEN_ADDR"))
	if listenAddr == "" {
		listenAddr = ":8080"
	}

	_, port, err := net.SplitHostPort(listenAddr)
	if err != nil || strings.TrimSpace(port) == "" {
		port = "8080"
	}

	return fmt.Sprintf("http://127.0.0.1:%s/health/ready", port)
}
