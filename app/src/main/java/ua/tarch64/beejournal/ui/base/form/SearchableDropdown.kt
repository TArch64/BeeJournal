package ua.tarch64.beejournal.ui.base.form

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import kotlinx.coroutines.delay

@Composable
fun <T> SearchableDropdown(
    value: T?,
    onValueChange: (T) -> Unit,
    onSearch: suspend (String) -> List<T>,
    itemLabel: (T) -> String,
    label: String,
    modifier: Modifier = Modifier,
    debounceMs: Long = 300L,
    maxResults: Int = 5,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    var query by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var suggestions by remember { mutableStateOf<List<T>>(emptyList()) }
    var loading by remember { mutableStateOf(false) }

    LaunchedEffect(query) {
        if (query.isBlank()) {
            suggestions = emptyList()
            return@LaunchedEffect
        }

        delay(debounceMs)
        loading = true

        suggestions = try {
            onSearch(query).take(maxResults)
        } catch (_: Exception) {
            emptyList()
        }

        loading = false
    }

    Box(modifier) {
        OutlinedTextField(
            value = if (expanded) query else (value?.let { itemLabel(it) } ?: ""),
            onValueChange = { query = it },
            label = { Text(label) },
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged {
                    if (it.isFocused) {
                        expanded = true
                        query = ""
                    }
                },
            trailingIcon = {
                if (loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    IconButton(
                        onClick = {
                            expanded = !expanded
                            query = ""
                        }
                    ) {
                        Icon(
                            if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                            null
                        )
                    }
                }
            },
            keyboardOptions = keyboardOptions
        )

        DropdownMenu(
            expanded = expanded && suggestions.isNotEmpty(),
            onDismissRequest = { expanded = false },
            properties = PopupProperties(focusable = false)
        ) {
            AnimatedContent(
                targetState = suggestions,
                transitionSpec = { fadeIn(tween(200)) togetherWith fadeOut(tween(200)) }
            ) { items ->
                Column {
                    items.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(itemLabel(item)) },
                            onClick = {
                                onValueChange(item)
                                expanded = false
                                query = ""
                            }
                        )
                    }
                }
            }
        }
    }
}