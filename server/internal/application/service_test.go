package application

import (
	"context"
	"testing"

	"github.com/wastingnotime/contacts-mobile/server/internal/domain"
)

type fakeRepository struct {
	contacts []domain.Contact
}

func (r *fakeRepository) ListContacts(ctx context.Context) ([]domain.Contact, error) {
	return append([]domain.Contact(nil), r.contacts...), nil
}

func (r *fakeRepository) LoadContactByID(ctx context.Context, id string) (domain.Contact, error) {
	for _, contact := range r.contacts {
		if contact.ID == id {
			return contact, nil
		}
	}
	return domain.Contact{}, domain.ErrContactNotFound
}

func (r *fakeRepository) CreateContact(ctx context.Context, draft domain.ContactDraft) (domain.Contact, error) {
	contact := domain.Contact{
		ID:          "created",
		FirstName:   draft.FirstName,
		LastName:    draft.LastName,
		PhoneNumber: draft.PhoneNumber,
	}
	r.contacts = append(r.contacts, contact)
	return contact, nil
}

func (r *fakeRepository) UpdateContact(ctx context.Context, id string, draft domain.ContactDraft) (domain.Contact, error) {
	contact := domain.Contact{
		ID:          id,
		FirstName:   draft.FirstName,
		LastName:    draft.LastName,
		PhoneNumber: draft.PhoneNumber,
	}
	return contact, nil
}

func (r *fakeRepository) DeleteContact(ctx context.Context, id string) error {
	return nil
}

func (r *fakeRepository) Ready(ctx context.Context) error {
	return nil
}

func TestServiceDelegatesToRepository(t *testing.T) {
	repository := &fakeRepository{
		contacts: []domain.Contact{{ID: "1", FirstName: "Ada", LastName: "Lovelace", PhoneNumber: "123"}},
	}
	service := NewService(repository)

	contacts, err := service.ListContacts(context.Background())
	if err != nil {
		t.Fatalf("ListContacts() error = %v", err)
	}
	if len(contacts) != 1 || contacts[0].ID != "1" {
		t.Fatalf("ListContacts() = %#v", contacts)
	}
}

func TestServiceReadyDelegatesToRepository(t *testing.T) {
	service := NewService(&fakeRepository{})

	if err := service.Ready(context.Background()); err != nil {
		t.Fatalf("Ready() error = %v", err)
	}
}
