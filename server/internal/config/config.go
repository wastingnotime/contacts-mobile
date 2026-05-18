package config

import (
	"fmt"
	"net/url"
	"os"
	"strings"
)

const (
	defaultListenAddr    = ":8080"
	defaultAPIPathPrefix = "/api"
	defaultAuthSubject   = "admin-user"
	defaultAuthRoles     = "admin"
)

type Config struct {
	ListenAddr         string
	ContactsAPIBaseURL string
	APIPathPrefix      string
	AuthSubject        string
	AuthRoles          string
}

func LoadFromEnv() (Config, error) {
	cfg := Config{
		ListenAddr:         lookupOrDefault("CONTACTS_BFF_LISTEN_ADDR", defaultListenAddr),
		ContactsAPIBaseURL: strings.TrimSpace(os.Getenv("CONTACTS_API_BASE_URL")),
		APIPathPrefix:      lookupOrDefault("CONTACTS_BFF_API_PREFIX", defaultAPIPathPrefix),
		AuthSubject:        lookupOrDefault("CONTACTS_BFF_AUTH_SUBJECT", defaultAuthSubject),
		AuthRoles:          lookupOrDefault("CONTACTS_BFF_AUTH_ROLES", defaultAuthRoles),
	}

	return cfg, cfg.Validate()
}

func (c *Config) Validate() error {
	if c.ListenAddr = strings.TrimSpace(c.ListenAddr); c.ListenAddr == "" {
		return fmt.Errorf("contactsBffListenAddr must not be blank")
	}

	if c.ContactsAPIBaseURL = strings.TrimSpace(c.ContactsAPIBaseURL); c.ContactsAPIBaseURL == "" {
		return fmt.Errorf("contactsApiBaseUrl must be configured")
	}
	if _, err := url.ParseRequestURI(c.ContactsAPIBaseURL); err != nil {
		return fmt.Errorf("contactsApiBaseUrl must be a valid URL: %w", err)
	}

	c.APIPathPrefix = normalizePathPrefix(c.APIPathPrefix)
	if c.APIPathPrefix == "" {
		return fmt.Errorf("contactsBffApiPrefix must not be blank")
	}

	c.AuthSubject = strings.TrimSpace(c.AuthSubject)
	if c.AuthSubject == "" {
		return fmt.Errorf("contactsBffAuthSubject must not be blank")
	}

	c.AuthRoles = strings.TrimSpace(c.AuthRoles)
	if c.AuthRoles == "" {
		return fmt.Errorf("contactsBffAuthRoles must not be blank")
	}

	return nil
}

func lookupOrDefault(key, fallback string) string {
	if value, ok := os.LookupEnv(key); ok {
		trimmed := strings.TrimSpace(value)
		if trimmed != "" {
			return trimmed
		}
	}
	return fallback
}

func normalizePathPrefix(prefix string) string {
	normalized := strings.TrimSpace(prefix)
	normalized = strings.TrimRight(normalized, "/")
	if normalized == "" {
		return ""
	}
	if !strings.HasPrefix(normalized, "/") {
		normalized = "/" + normalized
	}
	return normalized
}
