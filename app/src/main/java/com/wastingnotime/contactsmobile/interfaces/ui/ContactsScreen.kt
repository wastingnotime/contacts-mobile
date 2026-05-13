package com.wastingnotime.contactsmobile.interfaces.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wastingnotime.contactsmobile.domain.Contact

@Composable
fun ContactsRoute(
    viewModel: ContactsViewModel,
    modifier: Modifier = Modifier,
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val selectedContact = viewModel.selectedContact.collectAsStateWithLifecycle().value
    ContactsScreen(
        uiState = uiState,
        selectedContact = selectedContact,
        onContactClick = viewModel::openContact,
        onBack = viewModel::closeContactDetail,
        onRefresh = viewModel::refresh,
        onRetry = viewModel::refresh,
        modifier = modifier,
    )
}

@Composable
fun ContactsScreen(
    uiState: ContactsUiState,
    selectedContact: Contact?,
    onContactClick: (Contact) -> Unit,
    onBack: () -> Unit,
    onRefresh: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            ContactsTopBar(
                selectedContact = selectedContact,
                onBack = onBack,
                onRefresh = onRefresh,
            )
        },
        modifier = modifier,
    ) { paddingValues ->
        if (selectedContact != null) {
            ContactDetail(
                contact = selectedContact,
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

            ContactsUiState.Empty -> EmptyState(
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
                onContactClick = onContactClick,
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
    selectedContact: Contact?,
    onBack: () -> Unit,
    onRefresh: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(
                text = if (selectedContact == null) {
                    "Contacts"
                } else {
                    selectedContact.displayName.ifBlank { "Contact details" }
                },
            )
        },
        navigationIcon = {
            if (selectedContact != null) {
                TextButton(onClick = onBack) {
                    Text(text = "Back")
                }
            }
        },
        actions = {
            TextButton(onClick = onRefresh) {
                Text(text = "Refresh")
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
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    StateMessage(
        title = "No contacts yet",
        body = "The backend returned an empty list.",
        actionLabel = "Reload",
        onAction = onRetry,
        modifier = modifier,
    )
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
private fun StateMessage(
    title: String,
    body: String,
    actionLabel: String,
    onAction: () -> Unit,
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
            Button(onClick = onAction) {
                Text(text = actionLabel)
            }
        }
    }
}

@Composable
private fun ContactsList(
    contactsState: ContactsUiState.Loaded,
    onContactClick: (Contact) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
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

@Composable
private fun ContactDetail(
    contact: Contact,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(24.dp),
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
    }
}
