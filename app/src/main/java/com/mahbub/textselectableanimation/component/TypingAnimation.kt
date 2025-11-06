package com.mahbub.textselectableanimation.component

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun TypingAnimation() {
    var displayText by remember { mutableStateOf("") }
    var showCursor by remember { mutableStateOf(true) }
    val targetText = "ABCD"

    // Cursor blink animation
    LaunchedEffect(Unit) {
        while (true) {
            delay(500)
            showCursor = !showCursor
        }
    }

    // Typing and deleting animation
    LaunchedEffect(Unit) {
        while (true) {
            // Typing phase
            for (i in targetText.indices) {
                delay(300)
                displayText = targetText.substring(0, i + 1)
            }

            // Pause at full text
            delay(1000)

            // Deleting phase
            for (i in targetText.length downTo 0) {
                delay(200)
                displayText = targetText.substring(0, i)
            }

            // Pause at empty text
            delay(500)
        }
    }


        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = displayText + if (showCursor) "|" else " ",
                fontSize = 48.sp,
                fontFamily = FontFamily.Monospace,
                color = MaterialTheme.colorScheme.primary
            )
    }
}

