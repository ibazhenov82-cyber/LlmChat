# LLM Chat (Kotlin + Jetpack Compose)

Приложение-клиент для отправки текста задачи в LLM с выбором модели:

- **qwen3:0.6b** — локальная модель через [Ollama](https://ollama.com) (OpenAI-совместимый endpoint `/v1/chat/completions`)
- **deepseek-v4-flash**, **deepseek-v4-pro** — через [DeepSeek API](https://platform.deepseek.com)

## Архитектура

- **MVVM**, ручной DI (без Hilt/Koin — проще и стабильнее для проекта такого размера)
- **Retrofit + Gson + OkHttp** для сетевых запросов
- **Jetpack Compose (Material3)** — используются только стабильные API, без `@OptIn(Experimental...)`
- Адаптивная раскладка телефон/планшет реализована через `LocalConfiguration.current.screenWidthDp`
  (порог 600dp), а не через `WindowSizeClass`, который в разных версиях `material3` требует
  разных experimental opt-in аннотаций и нестабилен между версиями библиотеки.

```
app/src/main/java/com/example/llmchat/
├── MainActivity.kt
├── LlmChatApplication.kt
├── di/AppContainer.kt                  — простой сервис-локатор
├── data/
│   ├── model/LlmModel.kt               — enum моделей (id, имя, провайдер)
│   ├── model/ChatModels.kt             — DTO запроса/ответа chat/completions
│   ├── network/ChatApi.kt              — общий Retrofit-интерфейс
│   ├── network/NetworkModule.kt        — сборка Retrofit-клиентов (DeepSeek/Ollama)
│   └── repository/LlmRepository.kt     — бизнес-логика запроса и разбор choices[]
└── ui/
    ├── theme/                          — MaterialTheme (стабильные API)
    └── chat/
        ├── ChatViewModel.kt
        ├── ChatUiState.kt
        ├── ChatScreen.kt               — адаптивная раскладка (LocalConfiguration)
        └── components/                 — ModelSelector, PromptInput, InputSection, ResponseSection
```

## Настройка перед сборкой

Откройте `local.properties` в корне проекта и укажите свои значения:

```properties
DEEPSEEK_API_KEY=sk-...ваш_реальный_ключ...
LOCAL_HOST_QWEN3=http://10.0.2.2:11434/
```

- `DEEPSEEK_API_KEY` — получить на https://platform.deepseek.com/api_keys
- `LOCAL_HOST_QWEN3` — адрес запущенного локально Ollama-сервера в формате `http://<HOST>:<PORT>/`
  - для **эмулятора Android**: `http://10.0.2.2:11434/` (10.0.2.2 — алиас localhost хост-машины)
  - для **реального устройства** в той же Wi-Fi сети: `http://<IP-адрес-компьютера>:11434/`

Эти значения прокидываются в `BuildConfig.DEEPSEEK_API_KEY` и `BuildConfig.LOCAL_HOST_QWEN3`
на этапе сборки (см. `app/build.gradle.kts`) и никогда не хранятся в коде напрямую.

### Запуск локальной модели

```bash
ollama pull qwen3:0.6b
ollama serve
```

Ollama по умолчанию слушает `11434` порт и (начиная с достаточно новых версий) предоставляет
OpenAI-совместимый endpoint `POST /v1/chat/completions` — именно его использует приложение.

## Сборка проекта

1. Откройте папку проекта в Android Studio (Koala/Ladybug или новее — требуется поддержка AGP 8.5+).
2. Если Android Studio предложит сгенерировать Gradle Wrapper (файл `gradle-wrapper.jar`
   не включён в архив, чтобы не раздувать zip бинарным файлом) — согласитесь, либо выполните
   вручную (при установленном Gradle 8.7+):
   ```bash
   gradle wrapper --gradle-version 8.7
   ```
3. Дождитесь Gradle Sync и запустите конфигурацию `app` на эмуляторе/устройстве.

## Особенности сети

В манифесте включён `android:usesCleartextTraffic="true"` — это необходимо, так как локальный
Ollama-сервер по умолчанию доступен по обычному `http` (не `https`). Для продакшн-сборки
рекомендуется заменить это на `network_security_config.xml` с точечным разрешением cleartext
только для конкретного локального хоста.

## Формат запроса/ответа

И DeepSeek API, и Ollama (в режиме `/v1/chat/completions`) используют одинаковую,
OpenAI-совместимую схему:

```json
// Запрос
{
  "model": "deepseek-v4-flash",
  "messages": [
    { "role": "user", "content": "<текст задачи>" }
  ]
}
```

```json
// Ответ
{
  "choices": [
    { "message": { "role": "assistant", "content": "<решение>" } }
  ]
}
```

Текст решения берётся из `choices[0].message.content` и отображается в read-only
многострочном текстовом поле ("textarea") на экране.
