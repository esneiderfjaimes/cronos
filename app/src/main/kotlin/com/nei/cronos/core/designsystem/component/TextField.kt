package com.nei.cronos.core.designsystem.component

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
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
    Log.i(TAG, "TextField: Reading value: $value")
    BasicTextFieldFoundation(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .padding(top = 16.dp)
            .wrapContentHeight()
            .padding(start = 16.dp, end = 16.dp)
            .fillMaxWidth(),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        textStyle = textStyle.copy(color = colors.focusedTextColor),
        cursorBrush = colors.brush,
    ) { innerTextField ->
        val showPlaceholder by remember(value) {
            derivedStateOf {
                Log.i(TAG, "TextField: calculating showPlaceholder")
                value.isEmpty()
            }
        }
        Log.i(TAG, "TextField: showPlaceholder: ${value.isEmpty()}")

        // placeholder
        AnimatedVisibility(
            visible = showPlaceholder,
            enter = fadeIn(),
            exit = ExitTransition.None
        ) {
            CompositionLocalProvider(
                LocalTextStyle provides textStyle,
                LocalContentColor provides colors.focusedPlaceholderColor.copy(0.75f)
            ) {
                placeholder.invoke()
            }
        }
        innerTextField.invoke()
    }
}

private const val TAG = "TextField"