package main

import (
	"log"
	"net/http"

	"github.com/wastingnotime/contacts-mobile/server/internal/application"
	"github.com/wastingnotime/contacts-mobile/server/internal/config"
	"github.com/wastingnotime/contacts-mobile/server/internal/infrastructure/contactsapi"
	transporthttp "github.com/wastingnotime/contacts-mobile/server/internal/transport/http"
)

func main() {
	cfg, err := config.LoadFromEnv()
	if err != nil {
		log.Fatal(err)
	}

	repository, err := contactsapi.NewClient(
		cfg.ContactsAPIBaseURL,
		cfg.APIPathPrefix,
		cfg.AuthSubject,
		cfg.AuthRoles,
	)
	if err != nil {
		log.Fatal(err)
	}

	service := application.NewService(repository)
	server, err := transporthttp.NewServer(service, cfg.APIPathPrefix)
	if err != nil {
		log.Fatal(err)
	}

	log.Printf("contacts BFF listening on %s", cfg.ListenAddr)
	if err := http.ListenAndServe(cfg.ListenAddr, server.Handler()); err != nil {
		log.Fatal(err)
	}
}
