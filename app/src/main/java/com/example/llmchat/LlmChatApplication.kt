package com.example.llmchat

import android.app.Application
import com.example.llmchat.di.AppContainer

class LlmChatApplication : Application() {

    lateinit var appContainer: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer()
    }
}
