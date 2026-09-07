package com.example.llmchat.ui.chat.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.example.llmchat.R
import com.example.llmchat.data.model.LlmModel
import com.example.llmchat.data.model.LlmProvider

/**
 * Список моделей в виде RadioButton-группы. Сознательно не используется
 * ExposedDropdownMenuBox (Material3), так как в разных версиях библиотеки
 * он требует @OptIn(ExperimentalMaterial3Api) — по требованиям проекта
 * экспериментальные API не используются.
 */
@Composable
fun ModelSelector(
    selectedModel: LlmModel,
    onModelSelected: (LlmModel) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.selectableGroup()) {
        Text(
            text = stringResource(R.string.model_selector_title),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LlmModel.values().forEach { model ->
            val isSelected = model == selectedModel
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .selectable(
                        selected = isSelected,
                        onClick = { onModelSelected(model) },
                        role = Role.RadioButton
                    )
                    .padding(vertical = 8.dp, horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(selected = isSelected, onClick = null)
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(text = model.displayName, style = MaterialTheme.typography.bodyLarge)
                    Text(
                        text = stringResource(
                            if (model.provider == LlmProvider.OLLAMA_LOCAL) {
                                R.string.model_provider_local
                            } else {
                                R.string.model_provider_api
                            }
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
