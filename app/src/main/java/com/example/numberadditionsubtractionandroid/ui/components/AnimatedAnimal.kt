package com.example.numberadditionsubtractionandroid.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

@Composable
fun FloatingAnimal(
    emoji: String,
    index: Int,
    reshuffleKey: Int = 0,
    onClick: () -> Unit = {}
) {
    val config = LocalConfiguration.current
    val screenW = config.screenWidthDp
    val screenH = config.screenHeightDp

    val startX = remember(reshuffleKey) { Random.nextFloat() * screenW }
    val startY = remember(reshuffleKey) { Random.nextFloat() * screenH }
    val duration = remember(reshuffleKey) { 3000 + Random.nextInt(3000) }
    val bounceDuration = remember(reshuffleKey) { 500 + Random.nextInt(500) }
    val rotateDuration = remember(reshuffleKey) { 2000 + Random.nextInt(2000) }

    val infiniteTransition = rememberInfiniteTransition(label = "animal_$index")

    val offsetX by infiniteTransition.animateFloat(
        initialValue = startX,
        targetValue = (startX + Random.nextFloat() * 80f - 40f).coerceIn(0f, screenW.toFloat()),
        animationSpec = infiniteRepeatable(
            animation = tween(duration, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offsetX_$index"
    )

    val offsetY by infiniteTransition.animateFloat(
        initialValue = startY,
        targetValue = (startY + Random.nextFloat() * 60f - 30f).coerceIn(0f, screenH.toFloat()),
        animationSpec = infiniteRepeatable(
            animation = tween(bounceDuration, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offsetY_$index"
    )

    val rotation by infiniteTransition.animateFloat(
        initialValue = -15f,
        targetValue = 15f,
        animationSpec = infiniteRepeatable(
            animation = tween(rotateDuration),
            repeatMode = RepeatMode.Reverse
        ),
        label = "rotation_$index"
    )

    Text(
        text = emoji,
        fontSize = 72.sp,
        modifier = Modifier
            .offset(offsetX.dp, offsetY.dp)
            .rotate(rotation)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() }
    )
}

