import android.R.attr.endX
import android.R.attr.endY
import android.R.attr.startX
import android.R.attr.startY
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.io.path.Path
@Composable
fun TextSelectionAnimation() {
    var selectionEnd by remember { mutableStateOf(0) }
    var textLayoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }
    val targetText = "ABCD"
    val fontSize = 48.sp

    // Animate selection
    LaunchedEffect(Unit) {
        while (true) {
            for (i in 0..targetText.length) {
                delay(400)
                selectionEnd = i
            }
            delay(1000)
            for (i in targetText.length downTo 0) {
                delay(300)
                selectionEnd = i
            }
            delay(800)
        }
    }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
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
