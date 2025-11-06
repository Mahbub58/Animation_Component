package com.hashtag.generator.ai.post.writer.presentation.component


import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties


val PrimaryColor : Color = Color(0xFF6200EE)
@Composable
fun CustomLoader(
    dismissOnBackPress: Boolean = true,
    dismissOnClickOutside: Boolean = true,
    onDismiss: () -> Unit = {}
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = dismissOnBackPress,
            dismissOnClickOutside = dismissOnClickOutside
        )
    ) {
        Box(
            modifier = Modifier
                .size(240.dp)
                .background(Color.Transparent)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            StarAnimation(
                modifier = Modifier.fillMaxSize(),
                size = 220f
            )
        }
    }
}

@Composable
fun StarAnimation(
    modifier: Modifier = Modifier,
    size: Float = 160f,
    text: String? = null
) {
    val infiniteTransition = rememberInfiniteTransition(label = "ShimmerStarLoader")

    val animationProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "AnimationProgress"
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Canvas(modifier = Modifier.size(size.dp)) {
            val canvasSize = size

            drawSparkle(
                trajectory = SparkleTrajectory.Diagonal,
                canvasSize = this.size,
                baseSize = canvasSize * 1.65f,
                progress = animationProgress,
                brightness = 1.0f
            )
            drawSparkle(
                trajectory = SparkleTrajectory.Diagonal,
                canvasSize = this.size,
                baseSize = canvasSize * 1.35f,
                progress = (animationProgress + 0.33f) % 1f,
                brightness = 0.85f
            )
            drawSparkle(
                trajectory = SparkleTrajectory.Diagonal,
                canvasSize = this.size,
                baseSize = canvasSize * 1.05f,
                progress = (animationProgress + 0.33f) % 1f,
                brightness = 0.75f
            )
            drawSparkle(
                trajectory = SparkleTrajectory.Vertical,
                canvasSize = this.size,
                baseSize = canvasSize * 0.84f,
                progress = (animationProgress + 0.66f) % 1f,
                brightness = 0.6f
            )
        }

        text?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = it,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}


// ─────────────────────────────────────────────────────────────────────────────
// Drawing Logic
// ─────────────────────────────────────────────────────────────────────────────

private enum class SparkleTrajectory { Diagonal, Vertical }

private fun DrawScope.drawSparkle(
    trajectory: SparkleTrajectory,
    canvasSize: Size,
    baseSize: Float,
    progress: Float,
    brightness: Float
) {
    val (start, end) = when (trajectory) {
        SparkleTrajectory.Diagonal -> Pair(
            Offset(canvasSize.width * 0.10f, canvasSize.height * 0.10f),
            Offset(canvasSize.width * 0.90f, canvasSize.height * 0.90f)
        )

        SparkleTrajectory.Vertical -> Pair(
            Offset(canvasSize.width * 0.15f, canvasSize.height * 0.50f),
            Offset(canvasSize.width * 0.50f, canvasSize.height * 0.85f)
        )
    }

    val currentPosition = Offset(
        x = start.x + (end.x - start.x) * progress,
        y = start.y + (end.y - start.y) * progress
    )

    val scale = calculateScaleAndOpacity(progress)
    val finalSize = baseSize * scale
    val alpha = (scale * brightness).coerceIn(0.02f, 1f)

    if (finalSize < 1f || alpha < 0.02f) return

    drawStarWithEffects(currentPosition, finalSize, alpha, brightness, scale)
}

private fun calculateScaleAndOpacity(progress: Float): Float {
    val t = if (progress <= 0.5f) progress * 2 else (1f - progress) * 2
    val eased = t * t * (3f - 2f * t) // smoothstep
    return 0.05f + eased * 0.95f
}

private fun DrawScope.drawStarWithEffects(
    center: Offset,
    size: Float,
    alpha: Float,
    brightness: Float,
    scale: Float
) {
    val baseGradient = Brush.radialGradient(
        colors = List(5) { PrimaryColor.copy(alpha = alpha * (1f - it * 0.05f)) },
        center = center,
        radius = size * 0.85f
    )

    drawPath(
        path = createFourPointedStarPath(center, size),
        brush = baseGradient
    )

    if (scale > 0.15f) {
        val glowIntensity = ((scale - 0.15f) / 0.85f) * brightness
        drawPath(
            path = createFourPointedStarPath(center, size),
            brush = Brush.radialGradient(
                colors = listOf(
                    Color.White.copy(alpha = glowIntensity * alpha * 0.3f),
                    Color(0xFFD946EF).copy(alpha = glowIntensity * alpha * 0.25f),
                    Color(0xFFEC4899).copy(alpha = glowIntensity * alpha * 0.15f),
                    Color.Transparent
                ),
                center = center,
                radius = size * 2.0f
            )
        )

        if (scale > 0.7f) {
            val coreIntensity = ((scale - 0.7f) / 0.3f) * brightness
            drawPath(
                path = createFourPointedStarPath(center, size * 0.5f),
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color.White.copy(alpha = coreIntensity * alpha * 0.8f),
                        Color(0xFFFBBF24).copy(alpha = coreIntensity * alpha * 0.6f),
                        Color(0xFFD946EF).copy(alpha = coreIntensity * alpha * 0.3f),
                        Color.Transparent
                    ),
                    center = center,
                    radius = size * 0.3f
                )
            )
        }
    }
}

private fun createFourPointedStarPath(center: Offset, size: Float): Path {
    val path = Path()
    val outer = size / 2f
    val inner = outer * 0.35f

    val points = listOf(
        Offset(center.x, center.y - outer),
        Offset(center.x + inner * 0.4f, center.y - inner * 0.4f),
        Offset(center.x + outer, center.y),
        Offset(center.x + inner * 0.4f, center.y + inner * 0.4f),
        Offset(center.x, center.y + outer),
        Offset(center.x - inner * 0.4f, center.y + inner * 0.4f),
        Offset(center.x - outer, center.y),
        Offset(center.x - inner * 0.4f, center.y - inner * 0.4f)
    )

    path.moveTo(points.first().x, points.first().y)
    points.drop(1).forEach { path.lineTo(it.x, it.y) }
    path.close()

    return path
}

@Preview(showBackground = true)
@Composable
fun LoaderPreview() {
    Column(
        modifier = Modifier.padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomLoader()
    }
}