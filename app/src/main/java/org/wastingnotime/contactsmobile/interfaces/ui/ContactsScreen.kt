package org.wastingnotime.contactsmobile.interfaces.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest
import org.wastingnotime.contactsmobile.domain.Contact
import org.wastingnotime.contactsmobile.interfaces.theme.ContactsTheme

@Composable
fun ContactsRoute(
    viewModel: ContactsViewModel,
    modifier: Modifier = Modifier,
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val detailUiState = viewModel.detailUiState.collectAsStateWithLifecycle().value
    val createUiState = viewModel.createUiState.collectAsStateWithLifecycle().value
    val editUiState = viewModel.editUiState.collectAsStateWithLifecycle().value
    val listViewportState = viewModel.listViewportState.collectAsStateWithLifecycle().value
    ContactsScreen(
        uiState = uiState,
        detailUiState = detailUiState,
        createUiState = createUiState,
        editUiState = editUiState,
        listViewportState = listViewportState,
        onContactClick = viewModel::openContact,
        onBack = viewModel::closeContactDetail,
        onRefresh = viewModel::refresh,
        onRetry = viewModel::refresh,
        onSearchQueryChange = viewModel::updateSearchQuery,
        onListViewportChange = { index, offset, anchorContactId, secondaryAnchorContactId ->
            viewModel.updateListViewport(
                firstVisibleItemIndex = index,
                firstVisibleItemScrollOffset = offset,
                anchorContactId = anchorContactId,
                secondaryAnchorContactId = secondaryAnchorContactId,
            )
        },
        onDismissContactsStaleIndicator = viewModel::dismissContactsStaleIndicator,
        onCreateContact = viewModel::openCreateContact,
        onCloseCreateContact = viewModel::closeCreateContact,
        onCreateFirstNameChange = viewModel::updateCreateFirstName,
        onCreateLastNameChange = viewModel::updateCreateLastName,
        onCreatePhoneNumberChange = viewModel::updateCreatePhoneNumber,
        onCreateSubmit = viewModel::submitCreateContact,
        onCreateOpenContact = viewModel::openCreatedContact,
        onEditContact = viewModel::openEditContact,
        onCloseEditContact = viewModel::closeEditContact,
        onEditFirstNameChange = viewModel::updateEditFirstName,
        onEditLastNameChange = viewModel::updateEditLastName,
        onEditPhoneNumberChange = viewModel::updateEditPhoneNumber,
        onEditSubmit = viewModel::submitEditContact,
        onEditOpenContact = viewModel::openUpdatedContact,
        onDeleteContact = viewModel::deleteContact,
        onDismissContactDetailStaleIndicator = viewModel::dismissContactDetailStaleIndicator,
        modifier = modifier,
    )
}

@Composable
fun ContactsScreen(
    uiState: ContactsUiState,
    detailUiState: ContactDetailUiState,
    createUiState: CreateContactUiState,
    editUiState: EditContactUiState = EditContactUiState.Hidden,
    listViewportState: ContactsListViewportState = ContactsListViewportState(),
    onContactClick: (Contact) -> Unit,
    onBack: () -> Unit,
    onRefresh: () -> Unit,
    onRetry: () -> Unit,
    onSearchQueryChange: (String) -> Unit = {},
    onListViewportChange: (Int, Int, String?, String?) -> Unit = { _, _, _, _ -> },
    onDismissContactsStaleIndicator: () -> Unit = {},
    onCreateContact: () -> Unit,
    onCloseCreateContact: () -> Unit,
    onCreateFirstNameChange: (String) -> Unit,
    onCreateLastNameChange: (String) -> Unit,
    onCreatePhoneNumberChange: (String) -> Unit,
    onCreateSubmit: () -> Unit,
    onCreateOpenContact: () -> Unit,
    onEditContact: () -> Unit = {},
    onCloseEditContact: () -> Unit = {},
    onEditFirstNameChange: (String) -> Unit = {},
    onEditLastNameChange: (String) -> Unit = {},
    onEditPhoneNumberChange: (String) -> Unit = {},
    onEditSubmit: () -> Unit = {},
    onEditOpenContact: () -> Unit = {},
    onDeleteContact: () -> Unit = {},
    onDismissContactDetailStaleIndicator: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            ContactsTopBar(
                detailUiState = detailUiState,
                createUiState = createUiState,
                editUiState = editUiState,
                onBack = onBack,
                onRefresh = onRefresh,
                onCreateContact = onCreateContact,
                onCloseCreateContact = onCloseCreateContact,
                onEditContact = onEditContact,
                onCloseEditContact = onCloseEditContact,
            )
        },
        modifier = modifier,
    ) { paddingValues ->
        if (createUiState != CreateContactUiState.Hidden) {
            CreateContactContent(
                createUiState = createUiState,
                onCloseCreateContact = onCloseCreateContact,
                onFirstNameChange = onCreateFirstNameChange,
                onLastNameChange = onCreateLastNameChange,
                onPhoneNumberChange = onCreatePhoneNumberChange,
                onSubmit = onCreateSubmit,
                onOpenContact = onCreateOpenContact,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
            )
            return@Scaffold
        }

        if (editUiState != EditContactUiState.Hidden) {
            EditContactContent(
                editUiState = editUiState,
                onCloseEditContact = onCloseEditContact,
                onFirstNameChange = onEditFirstNameChange,
                onLastNameChange = onEditLastNameChange,
                onPhoneNumberChange = onEditPhoneNumberChange,
                onSubmit = onEditSubmit,
                onOpenContact = onEditOpenContact,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
            )
            return@Scaffold
        }

        if (detailUiState != ContactDetailUiState.Hidden) {
            DetailContent(
                detailUiState = detailUiState,
                onBack = onBack,
                onRetry = onRetry,
                onDelete = onDeleteContact,
                onDismissStaleIndicator = onDismissContactDetailStaleIndicator,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
            )
            return@Scaffold
        }

        when (uiState) {
            ContactsUiState.Loading -> LoadingState(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
            )

            is ContactsUiState.Empty -> EmptyState(
                emptyState = uiState,
                onRetry = onRetry,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
            )

            is ContactsUiState.Error -> ErrorState(
                message = uiState.message,
                onRetry = onRetry,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
            )

            is ContactsUiState.Loaded -> ContactsList(
                contactsState = uiState,
                listViewportState = listViewportState,
                onContactClick = onContactClick,
                onRetry = onRetry,
                onSearchQueryChange = onSearchQueryChange,
                onListViewportChange = onListViewportChange,
                onDismissStaleIndicator = onDismissContactsStaleIndicator,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
            )
            is ContactsUiState.FilteredEmpty -> FilteredEmptyState(
                filteredState = uiState,
                onRetry = onRetry,
                onSearchQueryChange = onSearchQueryChange,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ContactsTopBar(
    detailUiState: ContactDetailUiState,
    createUiState: CreateContactUiState,
    editUiState: EditContactUiState,
    onBack: () -> Unit,
    onRefresh: () -> Unit,
    onCreateContact: () -> Unit,
    onCloseCreateContact: () -> Unit,
    onEditContact: () -> Unit,
    onCloseEditContact: () -> Unit,
) {
    val title = when {
        editUiState != EditContactUiState.Hidden -> when (editUiState) {
            EditContactUiState.Hidden -> "Contacts"
            is EditContactUiState.Form -> "Edit contact"
            is EditContactUiState.Success -> "Contact updated"
        }
        detailUiState != ContactDetailUiState.Hidden -> when (detailUiState) {
            is ContactDetailUiState.Loaded -> detailUiState.contact.displayName.ifBlank { "Contact details" }
            is ContactDetailUiState.Loading -> "Contact details"
            is ContactDetailUiState.Deleting -> detailUiState.contact.displayName.ifBlank { "Contact details" }
            is ContactDetailUiState.NotFound -> "Contact details"
            is ContactDetailUiState.Error -> "Contact details"
            ContactDetailUiState.Hidden -> "Contacts"
        }
        createUiState != CreateContactUiState.Hidden -> when (createUiState) {
            CreateContactUiState.Hidden -> "Contacts"
            is CreateContactUiState.Form -> "New contact"
            is CreateContactUiState.Success -> "Contact created"
        }
        else -> "Contacts"
    }

    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            if (editUiState != EditContactUiState.Hidden) {
                TextButton(onClick = onCloseEditContact) {
                    Text(text = "Back")
                }
            } else if (createUiState != CreateContactUiState.Hidden) {
                TextButton(onClick = onCloseCreateContact) {
                    Text(text = "Back")
                }
            } else if (detailUiState != ContactDetailUiState.Hidden) {
                TextButton(onClick = onBack) {
                    Text(text = "Back")
                }
            }
        },
        actions = {
            if (editUiState == EditContactUiState.Hidden && createUiState == CreateContactUiState.Hidden) {
                TextButton(onClick = onRefresh) {
                    Text(text = "Refresh")
                }
                TextButton(onClick = onCreateContact) {
                    Text(text = "Add")
                }
                if (detailUiState is ContactDetailUiState.Loaded) {
                    TextButton(onClick = onEditContact) {
                        Text(text = "Edit")
                    }
                }
            }
        },
    )
}

@Composable
private fun LoadingState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun EmptyState(
    emptyState: ContactsUiState.Empty,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        emptyState.transientErrorMessage?.let { message ->
            TransientErrorBanner(
                message = message,
                onRetry = onRetry,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            )
        }
        StateMessage(
            title = "No contacts yet",
            body = "The backend returned an empty list.",
            actionLabel = "Reload",
            onAction = onRetry,
        )
    }
}

@Composable
private fun ErrorState(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    StateMessage(
        title = "Unable to load contacts",
        body = message,
        actionLabel = "Retry",
        onAction = onRetry,
        modifier = modifier,
    )
}

@Composable
private fun DetailContent(
    detailUiState: ContactDetailUiState,
    onBack: () -> Unit,
    onRetry: () -> Unit,
    onDelete: () -> Unit,
    onDismissStaleIndicator: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (detailUiState) {
        ContactDetailUiState.Hidden -> Unit
        is ContactDetailUiState.Loading -> LoadingState(modifier = modifier)
        is ContactDetailUiState.Loaded -> ContactDetail(
            contact = detailUiState.contact,
            transientErrorMessage = detailUiState.transientErrorMessage,
            freshnessState = detailUiState.freshnessState,
            staleAcknowledged = detailUiState.staleAcknowledged,
            onRetry = onRetry,
            onDelete = onDelete,
            onDismissStaleIndicator = onDismissStaleIndicator,
            modifier = modifier,
        )
        is ContactDetailUiState.Deleting -> ContactDetailDeleting(
            contact = detailUiState.contact,
            modifier = modifier,
        )
        is ContactDetailUiState.NotFound -> DetailProblemState(
            title = "Contact not found",
            body = "The selected contact is no longer available.",
            primaryActionLabel = "Retry",
            onPrimaryAction = onRetry,
            secondaryActionLabel = "Back",
            onSecondaryAction = onBack,
            modifier = modifier,
        )
        is ContactDetailUiState.Error -> DetailProblemState(
            title = "Unable to load contact",
            body = detailUiState.message,
            primaryActionLabel = "Retry",
            onPrimaryAction = onRetry,
            secondaryActionLabel = "Back",
            onSecondaryAction = onBack,
            modifier = modifier,
        )
    }
}

@Composable
private fun CreateContactContent(
    createUiState: CreateContactUiState,
    onCloseCreateContact: () -> Unit,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onPhoneNumberChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onOpenContact: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (createUiState) {
        CreateContactUiState.Hidden -> Unit
        is CreateContactUiState.Form -> CreateContactForm(
            form = createUiState.form,
            onFirstNameChange = onFirstNameChange,
            onLastNameChange = onLastNameChange,
            onPhoneNumberChange = onPhoneNumberChange,
            onSubmit = onSubmit,
            onCancel = onCloseCreateContact,
            modifier = modifier,
        )
        is CreateContactUiState.Success -> CreateContactSuccess(
            contact = createUiState.contact,
            onOpenContact = onOpenContact,
            onCloseCreateContact = onCloseCreateContact,
            modifier = modifier,
        )
    }
}

@Composable
private fun CreateContactForm(
    form: CreateContactFormState,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onPhoneNumberChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val firstNameFocusRequester = remember { FocusRequester() }
    val lastNameFocusRequester = remember { FocusRequester() }
    val phoneNumberFocusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    Column(
        modifier = modifier.padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        form.errorMessage?.let { message ->
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error,
            )
        }
        OutlinedTextField(
            value = form.firstName,
            onValueChange = onFirstNameChange,
            label = { Text("First name") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
            ),
            keyboardActions = KeyboardActions(
                onNext = { lastNameFocusRequester.requestFocus() },
            ),
            isError = form.fieldErrors.containsKey(CreateContactField.FirstName),
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(firstNameFocusRequester)
                .testTag("create-first-name"),
        )
        OutlinedTextField(
            value = form.lastName,
            onValueChange = onLastNameChange,
            label = { Text("Last name") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
            ),
            keyboardActions = KeyboardActions(
                onNext = { phoneNumberFocusRequester.requestFocus() },
            ),
            isError = form.fieldErrors.containsKey(CreateContactField.LastName),
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(lastNameFocusRequester)
                .testTag("create-last-name"),
        )
        OutlinedTextField(
            value = form.phoneNumber,
            onValueChange = onPhoneNumberChange,
            label = { Text("Phone number") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    onSubmit()
                },
            ),
            isError = form.fieldErrors.containsKey(CreateContactField.PhoneNumber),
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(phoneNumberFocusRequester)
                .testTag("create-phone-number"),
        )
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(
                onClick = onSubmit,
                enabled = !form.isSubmitting,
            ) {
                Text(text = if (form.isSubmitting) "Submitting" else "Create")
            }
            TextButton(onClick = onCancel) {
                Text(text = "Cancel")
            }
        }
    }
}

@Composable
private fun CreateContactSuccess(
    contact: Contact,
    onOpenContact: () -> Unit,
    onCloseCreateContact: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.padding(24.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = "Contact created",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
            )
            Text(
                text = contact.displayName.ifBlank { "Unnamed contact" },
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
            )
            TextButton(onClick = onCloseCreateContact) {
                Text(text = "Back to list")
            }
        }
    }
}

@Composable
private fun EditContactContent(
    editUiState: EditContactUiState,
    onCloseEditContact: () -> Unit,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onPhoneNumberChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onOpenContact: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (editUiState) {
        EditContactUiState.Hidden -> Unit
        is EditContactUiState.Form -> EditContactForm(
            form = editUiState.form,
            onFirstNameChange = onFirstNameChange,
            onLastNameChange = onLastNameChange,
            onPhoneNumberChange = onPhoneNumberChange,
            onSubmit = onSubmit,
            onCancel = onCloseEditContact,
            modifier = modifier,
        )
        is EditContactUiState.Success -> EditContactSuccess(
            contact = editUiState.contact,
            onOpenContact = onOpenContact,
            onCloseEditContact = onCloseEditContact,
            modifier = modifier,
        )
    }
}

@Composable
private fun EditContactForm(
    form: EditContactFormState,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onPhoneNumberChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val firstNameFocusRequester = remember { FocusRequester() }
    val lastNameFocusRequester = remember { FocusRequester() }
    val phoneNumberFocusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    Column(
        modifier = modifier.padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        form.errorMessage?.let { message ->
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error,
            )
        }
        OutlinedTextField(
            value = form.firstName,
            onValueChange = onFirstNameChange,
            label = { Text("First name") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
            ),
            keyboardActions = KeyboardActions(
                onNext = { lastNameFocusRequester.requestFocus() },
            ),
            isError = form.fieldErrors.containsKey(EditContactField.FirstName),
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(firstNameFocusRequester)
                .testTag("edit-first-name"),
        )
        OutlinedTextField(
            value = form.lastName,
            onValueChange = onLastNameChange,
            label = { Text("Last name") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
            ),
            keyboardActions = KeyboardActions(
                onNext = { phoneNumberFocusRequester.requestFocus() },
            ),
            isError = form.fieldErrors.containsKey(EditContactField.LastName),
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(lastNameFocusRequester)
                .testTag("edit-last-name"),
        )
        OutlinedTextField(
            value = form.phoneNumber,
            onValueChange = onPhoneNumberChange,
            label = { Text("Phone number") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    onSubmit()
                },
            ),
            isError = form.fieldErrors.containsKey(EditContactField.PhoneNumber),
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(phoneNumberFocusRequester)
                .testTag("edit-phone-number"),
        )
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(
                onClick = onSubmit,
                enabled = !form.isSubmitting,
            ) {
                Text(text = if (form.isSubmitting) "Saving" else "Save")
            }
            TextButton(onClick = onCancel) {
                Text(text = "Cancel")
            }
        }
    }
}

@Composable
private fun EditContactSuccess(
    contact: Contact,
    onOpenContact: () -> Unit,
    onCloseEditContact: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.padding(24.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = "Contact updated",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
            )
            Text(
                text = contact.displayName.ifBlank { "Unnamed contact" },
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
            )
            TextButton(onClick = onCloseEditContact) {
                Text(text = "Back to detail")
            }
        }
    }
}

@Composable
private fun DetailProblemState(
    title: String,
    body: String,
    primaryActionLabel: String,
    onPrimaryAction: () -> Unit,
    secondaryActionLabel: String,
    onSecondaryAction: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.padding(24.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
            )
            Text(
                text = body,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
            )
            Button(onClick = onPrimaryAction) {
                Text(text = primaryActionLabel)
            }
            TextButton(onClick = onSecondaryAction) {
                Text(text = secondaryActionLabel)
            }
        }
    }
}

@Composable
private fun StateMessage(
    title: String,
    body: String,
    actionLabel: String,
    onAction: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxWidth().padding(24.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
            )
            Text(
                text = body,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
            )
            Button(onClick = onAction) {
                Text(text = actionLabel)
            }
        }
    }
}

@Composable
private fun TransientErrorBanner(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = "Unable to refresh",
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
            )
            TextButton(onClick = onRetry) {
                Text(text = "Retry")
            }
        }
    }
}

@Composable
private fun ContactsList(
    contactsState: ContactsUiState.Loaded,
    listViewportState: ContactsListViewportState,
    onContactClick: (Contact) -> Unit,
    onRetry: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onListViewportChange: (Int, Int, String?, String?) -> Unit,
    onDismissStaleIndicator: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val contactsListState = rememberLazyListState(
        initialFirstVisibleItemIndex = listViewportState.firstVisibleItemIndex,
        initialFirstVisibleItemScrollOffset = listViewportState.firstVisibleItemScrollOffset,
    )
    LaunchedEffect(contactsListState) {
        snapshotFlow {
            val visibleItems = contactsListState.layoutInfo.visibleItemsInfo
            val visibleContactIds = visibleItems
                .take(2)
                .mapNotNull { item -> contactsState.contacts.getOrNull(item.index)?.id }
            ViewportSnapshot(
                contactsListState.firstVisibleItemIndex,
                contactsListState.firstVisibleItemScrollOffset,
                visibleContactIds.getOrNull(0),
                visibleContactIds.getOrNull(1),
            )
        }.collectLatest { (index, offset, anchorContactId, secondaryAnchorContactId) ->
            onListViewportChange(index, offset, anchorContactId, secondaryAnchorContactId)
        }
    }
    LaunchedEffect(contactsState.contacts) {
        val alignedViewportState = listViewportState.alignTo(contactsState.contacts)
        if (contactsState.contacts.isNotEmpty()) {
            contactsListState.scrollToItem(
                index = alignedViewportState.firstVisibleItemIndex,
                scrollOffset = alignedViewportState.firstVisibleItemScrollOffset,
            )
        }
    }
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        SearchField(
            query = contactsState.searchQuery,
            onQueryChange = onSearchQueryChange,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        )
        if (contactsState.freshnessState == ContactsFreshnessState.Stale && !contactsState.staleAcknowledged) {
            FreshnessBanner(
                text = "Showing stale contacts",
                actionLabel = "Dismiss",
                onAction = onDismissStaleIndicator,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }
        if (contactsState.searchQuery.isNotBlank()) {
            SearchSummary(
                matchedCount = contactsState.contacts.size,
                query = contactsState.searchQuery,
                onClearSearch = { onSearchQueryChange("") },
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }
        contactsState.transientErrorMessage?.let { message ->
            TransientErrorBanner(
                message = message,
                onRetry = onRetry,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            )
        }
        LazyColumn(
            state = contactsListState,
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(contactsState.contacts, key = { it.id }) { contact ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onContactClick(contact) },
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = contact.displayName.ifBlank { "Unnamed contact" },
                            style = MaterialTheme.typography.titleMedium,
                        )
                        Text(
                            text = contact.phoneNumber,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
            }
        }
    }
}

private data class ViewportSnapshot(
    val index: Int,
    val offset: Int,
    val anchorContactId: String?,
    val secondaryAnchorContactId: String?,
)

@Composable
private fun FilteredEmptyState(
    filteredState: ContactsUiState.FilteredEmpty,
    onRetry: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        SearchField(
            query = filteredState.query,
            onQueryChange = onSearchQueryChange,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        )
        SearchSummary(
            matchedCount = 0,
            query = filteredState.query,
            onClearSearch = { onSearchQueryChange("") },
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        filteredState.transientErrorMessage?.let { message ->
            TransientErrorBanner(
                message = message,
                onRetry = onRetry,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            )
        }
        StateMessage(
            title = "No matching contacts",
            body = "No loaded contacts match \"${filteredState.query}\".",
            actionLabel = "Clear search",
            onAction = { onSearchQueryChange("") },
        )
    }
}

@Composable
private fun FreshnessBanner(
    text: String,
    actionLabel: String,
    onAction: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
            )
            TextButton(onClick = onAction) {
                Text(text = actionLabel)
            }
        }
    }
}

@Composable
private fun SearchSummary(
    matchedCount: Int,
    query: String,
    onClearSearch: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "$matchedCount matches for \"$query\"",
            style = MaterialTheme.typography.bodyMedium,
        )
        TextButton(onClick = onClearSearch) {
            Text(text = "Clear")
        }
    }
}

@Composable
private fun SearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        label = { Text("Search contacts") },
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
    )
}

@Composable
private fun ContactDetail(
    contact: Contact,
    transientErrorMessage: String?,
    freshnessState: ContactsFreshnessState,
    staleAcknowledged: Boolean,
    onRetry: () -> Unit,
    onDelete: () -> Unit,
    onDismissStaleIndicator: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val showDeleteConfirmation = remember { mutableStateOf(false) }
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        transientErrorMessage?.let { message ->
            TransientErrorBanner(
                message = message,
                onRetry = onRetry,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            )
        }
        if (freshnessState == ContactsFreshnessState.Stale && !staleAcknowledged) {
            FreshnessBanner(
                text = "Showing stale contact details",
                actionLabel = "Dismiss",
                onAction = onDismissStaleIndicator,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = contact.displayName.ifBlank { "Unnamed contact" },
                style = MaterialTheme.typography.headlineMedium,
            )
            Card {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = "First name",
                        style = MaterialTheme.typography.labelLarge,
                    )
                    Text(
                        text = contact.firstName,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Text(
                        text = "Last name",
                        style = MaterialTheme.typography.labelLarge,
                    )
                    Text(
                        text = contact.lastName,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Text(
                        text = "Phone number",
                        style = MaterialTheme.typography.labelLarge,
                    )
                    Text(
                        text = contact.phoneNumber,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }
            Text(
                text = "Contact id: ${contact.id}",
                style = MaterialTheme.typography.bodySmall,
            )
            Button(
                onClick = { showDeleteConfirmation.value = true },
                modifier = Modifier.testTag("detail-delete-button"),
            ) {
                Text(text = "Delete")
            }
        }
        if (showDeleteConfirmation.value) {
            AlertDialog(
                onDismissRequest = { showDeleteConfirmation.value = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDeleteConfirmation.value = false
                            onDelete()
                        },
                        modifier = Modifier.testTag("delete-confirm-button"),
                    ) {
                        Text(text = "Delete")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showDeleteConfirmation.value = false },
                        modifier = Modifier.testTag("delete-cancel-button"),
                    ) {
                        Text(text = "Cancel")
                    }
                },
                title = { Text(text = "Delete contact?") },
                text = { Text(text = "This action cannot be undone.") },
            )
        }
    }
}

@Composable
private fun ContactDetailDeleting(
    contact: Contact,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = "Deleting ${contact.displayName.ifBlank { "contact" }}",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
            )
            CircularProgressIndicator()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ContactsScreenLoadedPreview() {
    ContactsTheme(dynamicColor = false) {
        ContactsScreen(
            uiState = ContactsUiState.Loaded(
                contacts = previewContacts(),
            ),
            detailUiState = ContactDetailUiState.Hidden,
            createUiState = CreateContactUiState.Hidden,
            onContactClick = {},
            onBack = {},
            onRefresh = {},
            onRetry = {},
            onCreateContact = {},
            onCloseCreateContact = {},
            onCreateFirstNameChange = {},
            onCreateLastNameChange = {},
            onCreatePhoneNumberChange = {},
            onCreateSubmit = {},
            onCreateOpenContact = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ContactsScreenTransientFailurePreview() {
    ContactsTheme(dynamicColor = false) {
        ContactsScreen(
            uiState = ContactsUiState.Loaded(
                contacts = previewContacts(),
                transientErrorMessage = "Unable to refresh contacts right now.",
            ),
            detailUiState = ContactDetailUiState.Hidden,
            createUiState = CreateContactUiState.Hidden,
            onContactClick = {},
            onBack = {},
            onRefresh = {},
            onRetry = {},
            onCreateContact = {},
            onCloseCreateContact = {},
            onCreateFirstNameChange = {},
            onCreateLastNameChange = {},
            onCreatePhoneNumberChange = {},
            onCreateSubmit = {},
            onCreateOpenContact = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ContactsScreenFilteredEmptyPreview() {
    ContactsTheme(dynamicColor = false) {
        ContactsScreen(
            uiState = ContactsUiState.FilteredEmpty(query = "zebra"),
            detailUiState = ContactDetailUiState.Hidden,
            createUiState = CreateContactUiState.Hidden,
            onContactClick = {},
            onBack = {},
            onRefresh = {},
            onRetry = {},
            onCreateContact = {},
            onCloseCreateContact = {},
            onCreateFirstNameChange = {},
            onCreateLastNameChange = {},
            onCreatePhoneNumberChange = {},
            onCreateSubmit = {},
            onCreateOpenContact = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ContactsDetailPreview() {
    val contacts = previewContacts()
    ContactsTheme(dynamicColor = false) {
        ContactsScreen(
            uiState = ContactsUiState.Loaded(
                contacts = contacts,
                searchQuery = "ada",
            ),
            detailUiState = ContactDetailUiState.Loaded(
                contact = contacts.first(),
                transientErrorMessage = "Detail refreshed with a warning.",
            ),
            createUiState = CreateContactUiState.Hidden,
            onContactClick = {},
            onBack = {},
            onRefresh = {},
            onRetry = {},
            onCreateContact = {},
            onCloseCreateContact = {},
            onCreateFirstNameChange = {},
            onCreateLastNameChange = {},
            onCreatePhoneNumberChange = {},
            onCreateSubmit = {},
            onCreateOpenContact = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ContactsCreatePreview() {
    ContactsTheme(dynamicColor = false) {
        ContactsScreen(
            uiState = ContactsUiState.Loaded(
                contacts = previewContacts(),
            ),
            detailUiState = ContactDetailUiState.Hidden,
            createUiState = CreateContactUiState.Form(
                CreateContactFormState(
                    firstName = "Ada",
                    lastName = "Lovelace",
                    phoneNumber = "555-0100",
                ),
            ),
            onContactClick = {},
            onBack = {},
            onRefresh = {},
            onRetry = {},
            onCreateContact = {},
            onCloseCreateContact = {},
            onCreateFirstNameChange = {},
            onCreateLastNameChange = {},
            onCreatePhoneNumberChange = {},
            onCreateSubmit = {},
            onCreateOpenContact = {},
        )
    }
}

private fun previewContacts(): List<Contact> = listOf(
    Contact(
        id = "contact-1",
        firstName = "Ada",
        lastName = "Lovelace",
        phoneNumber = "555-0100",
    ),
    Contact(
        id = "contact-2",
        firstName = "Grace",
        lastName = "Hopper",
        phoneNumber = "555-0101",
    ),
)
