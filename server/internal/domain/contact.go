package domain

import "errors"

var ErrContactNotFound = errors.New("contact not found")

type Contact struct {
	ID          string `json:"id"`
	FirstName   string `json:"first_name"`
	LastName    string `json:"last_name"`
	PhoneNumber string `json:"phone_number"`
}

type ContactDraft struct {
	FirstName   string
	LastName    string
	PhoneNumber string
}
