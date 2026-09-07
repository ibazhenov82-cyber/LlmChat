package com.example.llmchat.ui.chat.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.llmchat.R
import com.example.llmchat.data.model.LlmModel
import com.example.llmchat.ui.chat.ChatUiState

@Composable
fun InputSection(
    uiState: ChatUiState,
    onModelSelected: (LlmModel) -> Unit,
    onPromptChanged: (String) -> Unit,
    onSendClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        ModelSelector(
            selectedModel = uiState.selectedModel,
            onModelSelected = onModelSelected
        )

        Spacer(modifier = Modifier.height(16.dp))

        PromptInput(
            value = uiState.promptText,
            onValueChange = onPromptChanged
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = onSendClicked, enabled = uiState.isSendEnabled) {
                Text(stringResource(R.string.send_button))
            }
            if (uiState.isLoading) {
                Spacer(modifier = Modifier.width(12.dp))
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp
                )
            }
        }

        uiState.errorMessage?.let { message ->
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
