package com.example.llmchat.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.llmchat.data.model.LlmModel
import com.example.llmchat.data.repository.LlmRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatViewModel(private val repository: LlmRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    fun onModelSelected(model: LlmModel) {
        _uiState.update { it.copy(selectedModel = model) }
    }

    fun onPromptChanged(text: String) {
        _uiState.update { it.copy(promptText = text, errorMessage = null) }
    }

    fun onSendClicked() {
        val currentState = _uiState.value
        if (!currentState.isSendEnabled) return

        _uiState.update {
            it.copy(
                isLoading = true,
                errorMessage = null,
                responseText = "",
                elapsedMillis = null,
                totalTokens = null
            )
        }

        viewModelScope.launch {
            val result = repository.sendPrompt(
                model = currentState.selectedModel,
                prompt = currentState.promptText.trim()
            )

            result.fold(
                onSuccess = { chatResult ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            responseText = chatResult.content,
                            elapsedMillis = chatResult.elapsedMillis,
                            totalTokens = chatResult.totalTokens
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = error.message ?: "Неизвестная ошибка")
                    }
                }
            )
        }
    }
}

class ChatViewModelFactory(private val repository: LlmRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
            return ChatViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
