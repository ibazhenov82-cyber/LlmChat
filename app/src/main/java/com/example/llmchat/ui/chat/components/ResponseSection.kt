package com.example.llmchat.ui.chat.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.llmchat.R
import com.example.llmchat.ui.chat.ChatUiState
import java.util.Locale

@Composable
fun ResponseSection(
    uiState: ChatUiState,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.response_label),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        if (uiState.elapsedMillis != null) {
            ResponseMetrics(
                elapsedMillis = uiState.elapsedMillis,
                totalTokens = uiState.totalTokens,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        OutlinedTextField(
            value = uiState.responseText,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            placeholder = { Text(stringResource(R.string.response_placeholder)) }
        )
    }
}

@Composable
private fun ResponseMetrics(
    elapsedMillis: Long,
    totalTokens: Int?,
    modifier: Modifier = Modifier
) {
    val elapsedSeconds = elapsedMillis / 1000.0
    val timeText = stringResource(
        R.string.metrics_time,
        String.format(Locale.getDefault(), "%.2f", elapsedSeconds)
    )
    val tokensText = if (totalTokens != null) {
        stringResource(R.string.metrics_tokens, totalTokens)
    } else {
        stringResource(R.string.metrics_tokens_unknown)
    }

    Text(
        text = "$timeText  ·  $tokensText",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier
    )
}
