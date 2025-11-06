import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay


@Composable
fun TextTypeAndSelect() {

    var step by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(300L * "Type Something".length + 1000L)
            step = 1

            delay(1000L)
            step = 2

            delay(400L * ("Something".length + 1) + 1000L)
            step = 0
        }
    }

    when (step) {
        0 -> TypingAnimationOne("Type Something")
        1 -> ShowOrOnce()
        2 -> TextSelectionAnimationOne("Select", "Something")
        else -> {}
    }
}


@Composable
fun TypingAnimationOne( text : String) {
    var displayText by remember { mutableStateOf("") }
    var showCursor by remember { mutableStateOf(true) }
    val targetText = text

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

//            // Deleting phase
//            for (i in targetText.length downTo 0) {
//                delay(200)
//                displayText = targetText.substring(0, i)
//            }
//
//            // Pause at empty text
//            delay(500)
        }
    }



    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
            Text(
                text = displayText + if (showCursor) "|" else " ",
                fontSize = 14.sp,
                fontFamily = FontFamily.Monospace,
                color = MaterialTheme.colorScheme.primary
            )


    }
}



@Composable
fun ShowOrOnce() {
    var visible by remember { mutableStateOf(true) }

    // Hide after 1 second
    LaunchedEffect(Unit) {
        delay(1000) // 1000 ms = 1 second
        visible = false
    }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        if (visible) {
            Text(
                text = "or",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun TextSelectionAnimationOne(textFixed: String, text: String) {
    var selectionEnd by remember { mutableStateOf(0) }
    var textLayoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }
    val targetText =text
    val fontSize = 14.sp

    // Animate selection
    LaunchedEffect(Unit) {
        while (true) {
            for (i in 0..targetText.length) {
                delay(400)
                selectionEnd = i
            }
            delay(1000)

        }
    }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Row {
            Text(
                text = textFixed,
                fontSize = 14.sp,
                fontFamily = FontFamily.Monospace,
                color = MaterialTheme.colorScheme.primary
            )


            Box {
                // Text with animated highlight
                Text(
                    text = buildAnnotatedString {
                        for (i in targetText.indices) {
                            if (i < selectionEnd) {
                                withStyle(style = SpanStyle(background = Color(0xFFB3D9FF))) {
                                    append(targetText[i])
                                }
                            } else {
                                append(targetText[i])
                            }
                        }
                    },
                    fontSize = fontSize,
                    fontFamily = FontFamily.Monospace,
                    color = MaterialTheme.colorScheme.primary,
                    onTextLayout = { textLayoutResult = it }
                )

                // Draw half-heart bubbles
                if (selectionEnd > 0 && textLayoutResult != null) {
                    val layout = textLayoutResult!!
                    Canvas(modifier = Modifier.matchParentSize()) {
                        val s = 56f         // 🔹 Increased size of heart (was 40f)
                        val quarter = s / 4f
                        val offsetY = 30f   // 🔹 Reduced gap between bubble and text (was 60f)

                        // Left and right half-heart shapes
                        val leftPath = Path().apply {
                            moveTo(quarter, s / 2f)
                            cubicTo(0f, quarter, quarter, 0f, s / 2f, 0f)
                            lineTo(s / 2f, quarter * 2.5f)
                            close()
                        }
                        val rightPath = Path().apply {
                            moveTo(s - quarter, s / 2f)
                            cubicTo(s, quarter, s - quarter, 0f, s / 2f, 0f)
                            lineTo(s / 2f, quarter * 2.5f)
                            close()
                        }

                        // --- Start heart half ---
                        val startBounds = layout.getBoundingBox(0)
                        val startX = startBounds.left
                        val startY = startBounds.top - offsetY
                        withTransform({
                            translate(startX - s / 2f, startY)
                        }) {
                            drawPath(leftPath, Color(0xFF9C27B0))
                        }

                        // --- End heart half ---
                        if (selectionEnd <= targetText.length) {
                            val endBounds = layout.getBoundingBox(
                                if (selectionEnd == targetText.length) selectionEnd - 1 else selectionEnd
                            )
                            val endX = if (selectionEnd == targetText.length)
                                endBounds.right
                            else endBounds.left
                            val endY = endBounds.top - offsetY
                            withTransform({
                                translate(endX - s / 2f, endY)
                            }) {
                                drawPath(rightPath, Color(0xFF9C27B0))
                            }
                        }
                    }
                }
            }
        }
    }
}
