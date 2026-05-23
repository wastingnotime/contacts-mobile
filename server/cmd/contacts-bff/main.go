package main

import (
	"log"
	"net/http"

	"github.com/wastingnotime/contacts-mobile/server/internal/application"
	"github.com/wastingnotime/contacts-mobile/server/internal/config"
	"github.com/wastingnotime/contacts-mobile/server/internal/infrastructure/contactsapi"
	"github.com/wastingnotime/contacts-mobile/server/internal/observability"
	transporthttp "github.com/wastingnotime/contacts-mobile/server/internal/transport/http"
)

func main() {
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
