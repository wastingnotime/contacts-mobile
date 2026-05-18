package application

import (
	"context"

	"github.com/wastingnotime/contacts-mobile/server/internal/domain"
)

type Repository interface {
	ListContacts(ctx context.Context) ([]domain.Contact, error)
	LoadContactByID(ctx context.Context, id string) (domain.Contact, error)
	CreateContact(ctx context.Context, draft domain.ContactDraft) (domain.Contact, error)
	UpdateContact(ctx context.Context, id string, draft domain.ContactDraft) (domain.Contact, error)
	DeleteContact(ctx context.Context, id string) error
}

type Service struct {
	repository Repository
}

func NewService(repository Repository) *Service {
	return &Service{repository: repository}
}

func (s *Service) ListContacts(ctx context.Context) ([]domain.Contact, error) {
	return s.repository.ListContacts(ctx)
}

func (s *Service) LoadContactByID(ctx context.Context, id string) (domain.Contact, error) {
	return s.repository.LoadContactByID(ctx, id)
}

func (s *Service) CreateContact(ctx context.Context, draft domain.ContactDraft) (domain.Contact, error) {
	return s.repository.CreateContact(ctx, draft)
}

func (s *Service) UpdateContact(ctx context.Context, id string, draft domain.ContactDraft) (domain.Contact, error) {
	return s.repository.UpdateContact(ctx, id, draft)
}

func (s *Service) DeleteContact(ctx context.Context, id string) error {
	return s.repository.DeleteContact(ctx, id)
}
