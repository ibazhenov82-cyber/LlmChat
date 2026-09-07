package com.example.llmchat.data.repository

import com.example.llmchat.BuildConfig
import com.example.llmchat.data.model.ChatCompletionRequest
import com.example.llmchat.data.model.ChatMessage
import com.example.llmchat.data.model.LlmModel
import com.example.llmchat.data.model.LlmProvider
import com.example.llmchat.data.network.ChatApi
import retrofit2.HttpException
import java.io.IOException

interface LlmRepository {
    /**
     * Отправляет текст задачи выбранной модели и возвращает текст решения из choices[0].message.content.
     */
    suspend fun sendPrompt(model: LlmModel, prompt: String): Result<String>
}

class LlmRepositoryImpl(
    private val deepSeekApi: ChatApi,
    private val ollamaApi: ChatApi
) : LlmRepository {

    override suspend fun sendPrompt(model: LlmModel, prompt: String): Result<String> {
        if (model.provider == LlmProvider.DEEPSEEK && BuildConfig.DEEPSEEK_API_KEY.isBlank()) {
            return Result.failure(
                IllegalStateException("Не задан DEEPSEEK_API_KEY в local.properties")
            )
        }

        val request = ChatCompletionRequest(
            model = model.id,
            messages = listOf(ChatMessage(role = "user", content = prompt))
        )

        return try {
            val response = when (model.provider) {
                LlmProvider.DEEPSEEK -> deepSeekApi.createChatCompletion(request)
                LlmProvider.OLLAMA_LOCAL -> ollamaApi.createChatCompletion(request)
            }

            val content = response.choices?.firstOrNull()?.message?.content?.trim()
            if (content.isNullOrBlank()) {
                Result.failure(IllegalStateException("Пустой ответ от модели"))
            } else {
                Result.success(content)
            }
        } catch (e: HttpException) {
            val serverMessage = runCatching { e.response()?.errorBody()?.string() }.getOrNull()
            Result.failure(
                Exception(
                    buildString {
                        append("Ошибка сервера (${e.code()})")
                        if (!serverMessage.isNullOrBlank()) append(": $serverMessage")
                    }
                )
            )
        } catch (e: IOException) {
            val hint = if (model.provider == LlmProvider.OLLAMA_LOCAL) {
                " Проверьте LOCAL_HOST_QWEN3 в local.properties: для эмулятора должен быть " +
                    "http://10.0.2.2:11434/ (не 127.0.0.1 — он указывает на сам эмулятор), " +
                    "для реального устройства — LAN-IP компьютера, и Ollama должна быть " +
                    "запущена с OLLAMA_HOST=0.0.0.0:11434."
            } else {
                " Проверьте интернет-соединение на устройстве/эмуляторе."
            }
            Result.failure(
                Exception(
                    "Ошибка сети (${e.javaClass.simpleName}: ${e.message ?: "нет описания"})." + hint
                )
            )
        } catch (e: Exception) {
            Result.failure(Exception("${e.javaClass.simpleName}: ${e.message}"))
        }
    }
}
