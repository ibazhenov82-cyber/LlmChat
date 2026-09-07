package com.example.llmchat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.llmchat.ui.chat.ChatScreen
import com.example.llmchat.ui.chat.ChatViewModel
import com.example.llmchat.ui.chat.ChatViewModelFactory
import com.example.llmchat.ui.theme.LlmChatTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appContainer = (application as LlmChatApplication).appContainer

        setContent {
            LlmChatTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ChatRoot(factory = ChatViewModelFactory(appContainer.llmRepository))
                }
            }
        }
    }
}

@Composable
private fun ChatRoot(factory: ChatViewModelFactory) {
    val viewModel: ChatViewModel = viewModel(factory = factory)
    ChatScreen(viewModel = viewModel)
}
