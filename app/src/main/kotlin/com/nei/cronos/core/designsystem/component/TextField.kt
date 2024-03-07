package com.nei.cronos.core.designsystem.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.nei.cronos.core.designsystem.theme.CronosTheme
import com.nei.cronos.core.designsystem.utils.ThemePreviews
import androidx.compose.foundation.text.BasicTextField as BasicTextFieldFoundation

@Composable
fun TextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    placeholder: @Composable () -> Unit = {},
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    colors: TextFieldColors = TextFieldDefaults.colors(),
) {
    BasicTextFieldFoundation(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .sizeIn(maxWidth = 488.dp)
            .fillMaxWidth(),
        singleLine = true,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        textStyle = textStyle.copy(color = colors.focusedTextColor),
        cursorBrush = colors.brush,
    ) { innerTextField ->
        val showPlaceholder by remember(value) { derivedStateOf { value.isEmpty() } }

        // placeholder
        AnimatedVisibility(
            visible = showPlaceholder,
            enter = fadeIn(),
            exit = ExitTransition.None
        ) {
            CompositionLocalProvider(
                LocalTextStyle provides textStyle,
                LocalContentColor provides colors.focusedPlaceholderColor.copy(0.75f),
                content = placeholder
            )
        }
        innerTextField.invoke()
    }
}

@ThemePreviews
@Composable
fun TextFieldPreview() {
    CronosTheme {
        CronosBackground {
            TextField(
                modifier = Modifier
                    .padding(16.dp),
                value = "Title",
                onValueChange = {},
            )
        }
    }
}

@ThemePreviews
@Composable
fun TextFieldEmptyPreview() {
    CronosTheme {
        CronosBackground {
            TextField(
                modifier = Modifier
                    .padding(16.dp),
                value = "",
                placeholder = { Text(text = "placeholder") },
                onValueChange = {},
            )
        }
    }
}