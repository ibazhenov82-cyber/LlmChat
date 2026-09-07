package com.example.llmchat.ui.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.llmchat.R
import com.example.llmchat.data.model.LlmModel
import com.example.llmchat.ui.chat.components.InputSection
import com.example.llmchat.ui.chat.components.ResponseSection

/** Ширина экрана (dp), начиная с которой раскладка считается "планшетной". */
private const val TABLET_WIDTH_BREAKPOINT_DP = 600

@Composable
fun ChatScreen(
    viewModel: ChatViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ChatScreenContent(
        uiState = uiState,
        onModelSelected = viewModel::onModelSelected,
        onPromptChanged = viewModel::onPromptChanged,
        onSendClicked = viewModel::onSendClicked,
        modifier = modifier
    )
}

/**
 * Вместо WindowSizeClass API (который в разных версиях material3 требует разных
 * @OptIn(ExperimentalMaterial3WindowSizeClassApi) и ломается между версиями библиотеки)
 * используем простое и стабильное решение через LocalConfiguration.screenWidthDp.
 */
@Composable
private fun ChatScreenContent(
    uiState: ChatUiState,
    onModelSelected: (LlmModel) -> Unit,
    onPromptChanged: (String) -> Unit,
    onSendClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val isTablet = configuration.screenWidthDp >= TABLET_WIDTH_BREAKPOINT_DP
    val contentPadding = if (isTablet) 24.dp else 16.dp

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        Text(
            text = stringResource(R.string.app_title),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (isTablet) {
            TabletLayout(uiState, onModelSelected, onPromptChanged, onSendClicked)
        } else {
            PhoneLayout(uiState, onModelSelected, onPromptChanged, onSendClicked)
        }
    }
}

@Composable
private fun TabletLayout(
    uiState: ChatUiState,
    onModelSelected: (LlmModel) -> Unit,
    onPromptChanged: (String) -> Unit,
    onSendClicked: () -> Unit
) {
    // На широких экранах (планшеты, раскладные устройства, десктоп-режим)
    // ввод и ответ показываются рядом, бок о бок.
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        InputSection(
            uiState = uiState,
            onModelSelected = onModelSelected,
            onPromptChanged = onPromptChanged,
            onSendClicked = onSendClicked,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
        )

        ResponseSection(
            uiState = uiState,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        )
    }
}

@Composable
private fun PhoneLayout(
    uiState: ChatUiState,
    onModelSelected: (LlmModel) -> Unit,
    onPromptChanged: (String) -> Unit,
    onSendClicked: () -> Unit
) {
    // На узких экранах ввод и ответ идут друг за другом вертикально.
    Column(modifier = Modifier.fillMaxSize()) {
        InputSection(
            uiState = uiState,
            onModelSelected = onModelSelected,
            onPromptChanged = onPromptChanged,
            onSendClicked = onSendClicked,
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        )

        Spacer(modifier = Modifier.height(16.dp))

        ResponseSection(
            uiState = uiState,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )
    }
}
