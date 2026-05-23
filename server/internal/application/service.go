package application

import (
	"context"
	"time"

	"github.com/wastingnotime/contacts-mobile/server/internal/domain"
)

type Repository interface {
	ListContacts(ctx context.Context) ([]domain.Contact, error)
	LoadContactByID(ctx context.Context, id string) (domain.Contact, error)
	CreateContact(ctx context.Context, draft domain.ContactDraft) (domain.Contact, error)
	UpdateContact(ctx context.Context, id string, draft domain.ContactDraft) (domain.Contact, error)
	DeleteContact(ctx context.Context, id string) error
	Ready(ctx context.Context) error
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

func (s *Service) Ready(ctx context.Context) error {
	timeoutContext, cancel := context.WithTimeout(ctx, 2*time.Second)
	defer cancel()

	return s.repository.Ready(timeoutContext)
}
