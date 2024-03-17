package com.nei.cronos.core.designsystem

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class TestViewModel @Inject constructor() : ViewModel() {
    val count = flow {
        var count = 0
        while (true) {
            delay(1000)
            emit(count++)
        }
    }

    fun test() {}
}


@Composable
fun TestScreen() {
    val viewModel = hiltViewModel<TestViewModel>()

    val kFunction0 = viewModel::test

    val state by viewModel.count.collectAsStateWithLifecycle(0)

    TestBody(state, kFunction0)
}

@Composable
fun TestBody(count: Int, kFunction0: () -> Unit) {
    MaterialTheme {

        Column {
            CustomButton {
                kFunction0.invoke()
            }
            CustomButton(kFunction0)
            Text(text = "Test count: $count")
            Button(onClick = { kFunction0.invoke() }) {
                Text(text = "Test 1")
            }
            Button(onClick = kFunction0) {
                Text(text = "Test 2")
            }
            Button(onClick = { kFunction0.invoke() }) {
                Text(text = "Test 3")
            }
        }
    }
}

@Composable
fun CustomButton(onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text(text = "Test")
    }
}