package config

import (
	"testing"
)

func TestLoadFromEnvUsesDefaultsAndRequiredUpstreamURL(t *testing.T) {
	t.Setenv("CONTACTS_API_BASE_URL", "http://127.0.0.1:8081")

	cfg, err := LoadFromEnv()
	if err != nil {
		t.Fatalf("LoadFromEnv() error = %v", err)
	}

	if cfg.ListenAddr != defaultListenAddr {
		t.Fatalf("ListenAddr = %q, want %q", cfg.ListenAddr, defaultListenAddr)
	}
	if cfg.ContactsAPIBaseURL != "http://127.0.0.1:8081" {
		t.Fatalf("ContactsAPIBaseURL = %q", cfg.ContactsAPIBaseURL)
	}
	if cfg.APIPathPrefix != defaultAPIPathPrefix {
		t.Fatalf("APIPathPrefix = %q, want %q", cfg.APIPathPrefix, defaultAPIPathPrefix)
	}
	if cfg.AuthSubject != defaultAuthSubject {
		t.Fatalf("AuthSubject = %q, want %q", cfg.AuthSubject, defaultAuthSubject)
	}
	if cfg.AuthRoles != defaultAuthRoles {
		t.Fatalf("AuthRoles = %q, want %q", cfg.AuthRoles, defaultAuthRoles)
	}
}

func TestLoadFromEnvRejectsMissingContactsAPIBaseURL(t *testing.T) {
	t.Setenv("CONTACTS_API_BASE_URL", "")

	_, err := LoadFromEnv()
	if err == nil {
		t.Fatal("LoadFromEnv() error = nil, want error")
	}
}
