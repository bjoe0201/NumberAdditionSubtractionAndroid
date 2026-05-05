package com.example.numberadditionsubtractionandroid.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.coerceAtMost
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.numberadditionsubtractionandroid.data.Animal
import com.example.numberadditionsubtractionandroid.ui.theme.SkyBlue
import com.example.numberadditionsubtractionandroid.ui.theme.WrongRed

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AdditionAnimalGrid(
    animals1: List<Animal>,
    animals2: List<Animal>,
    tappedIndices1: Set<Int>,
    tappedIndices2: Set<Int>,
    onTapGroup1: (Int) -> Unit,
    onTapGroup2: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 左側：第一組動物
        AnimalFlowGrid(
            animals = animals1,
            tappedIndices = tappedIndices1,
            onTap = onTapGroup1,
            modifier = Modifier.weight(1f).fillMaxHeight()
        )

        // 中間：加號
        Box(
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .size(48.dp)
                .background(Color(0xFF43A047), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("+", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        // 右側：第二組動物
        AnimalFlowGrid(
            animals = animals2,
            tappedIndices = tappedIndices2,
            onTap = onTapGroup2,
            modifier = Modifier.weight(1f).fillMaxHeight()
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SubtractionAnimalGrid(
    animals: List<Animal>,
    crossedOutIndices: Set<Int>,
    maxCrossOut: Int,
    onCrossOut: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val remainingMarkedAnimals = (maxCrossOut - crossedOutIndices.size)
        .coerceAtLeast(0)

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CrossOutAnimalFlowGrid(
            animals = animals,
            crossedOutIndices = crossedOutIndices,
            maxCrossOut = maxCrossOut,
            onCrossOut = onCrossOut,
            modifier = Modifier.weight(1f).fillMaxHeight()
        )

        Box(
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .size(48.dp)
                .background(WrongRed, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("−", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        StaticMarkedAnimalGrid(
            animals = animals.take(remainingMarkedAnimals),
            modifier = Modifier.weight(1f).fillMaxHeight()
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CrossOutAnimalFlowGrid(
    animals: List<Animal>,
    crossedOutIndices: Set<Int>,
    maxCrossOut: Int,
    onCrossOut: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        val count = animals.size
        val cellPadding = 4.dp
        val cols = when {
            count == 1 -> 1
            count <= 4 -> count
            count <= 6 -> 3
            count <= 10 -> 5
            count <= 20 -> 5
            count <= 50 -> 8
            else -> 10
        }
        val rows = (count + cols - 1) / cols
        val cellByWidth: Dp = (maxWidth / cols) - cellPadding * 2
        val cellByHeight: Dp = (maxHeight / rows) - cellPadding * 2
        val cellSize: Dp = cellByWidth
            .coerceAtMost(cellByHeight)
            .coerceAtMost(96.dp)
            .coerceAtLeast(28.dp)
        val fontSize = (cellSize.value * 0.65f).sp

        FlowRow(
            maxItemsInEachRow = cols,
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.Center
        ) {
            animals.forEachIndexed { index, animal ->
                val isCrossed = index in crossedOutIndices
                val canCross = crossedOutIndices.size < maxCrossOut
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(cellPadding)
                        .size(cellSize)
                        .clip(CircleShape)
                        .background(
                            if (isCrossed) WrongRed.copy(alpha = 0.15f)
                            else Color.Transparent
                        )
                        .border(
                            width = if (isCrossed) 2.dp else 0.dp,
                            color = if (isCrossed) WrongRed else Color.Transparent,
                            shape = CircleShape
                        )
                        .clickable(enabled = !isCrossed && canCross) { onCrossOut(index) }
                ) {
                    Text(
                        text = animal.emoji,
                        fontSize = fontSize,
                        modifier = Modifier.alpha(if (isCrossed) 0.4f else 1f)
                    )
                    if (isCrossed) {
                        Text(
                            text = "X",
                            fontSize = fontSize,
                            fontWeight = FontWeight.Bold,
                            color = WrongRed
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun StaticMarkedAnimalGrid(
    animals: List<Animal>,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        val count = animals.size.coerceAtLeast(1)
        val cellPadding = 4.dp
        val cols = when {
            count == 1 -> 1
            count <= 4 -> count
            count <= 6 -> 3
            count <= 10 -> 5
            count <= 20 -> 5
            count <= 50 -> 8
            else -> 10
        }
        val rows = (count + cols - 1) / cols
        val cellByWidth: Dp = (maxWidth / cols) - cellPadding * 2
        val cellByHeight: Dp = (maxHeight / rows) - cellPadding * 2
        val cellSize: Dp = cellByWidth
            .coerceAtMost(cellByHeight)
            .coerceAtMost(80.dp)
            .coerceAtLeast(28.dp)
        val fontSize = (cellSize.value * 0.65f).sp

        FlowRow(
            maxItemsInEachRow = cols,
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.Center
        ) {
            animals.forEach { animal ->
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(cellPadding)
                        .size(cellSize)
                        .clip(CircleShape)
                        .background(WrongRed.copy(alpha = 0.12f))
                        .border(2.dp, WrongRed.copy(alpha = 0.5f), CircleShape)
                ) {
                    Text(
                        text = animal.emoji,
                        fontSize = fontSize,
                        modifier = Modifier.alpha(0.45f)
                    )
                    Text(
                        text = "X",
                        fontSize = fontSize,
                        fontWeight = FontWeight.Bold,
                        color = WrongRed
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AnimalFlowGrid(
    animals: List<Animal>,
    tappedIndices: Set<Int>,
    onTap: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        val count = animals.size
        val cellPadding = 4.dp
        val cols = when {
            count == 1 -> 1
            count <= 4 -> count
            count <= 6 -> 3
            count <= 10 -> 5
            count <= 20 -> 5
            count <= 50 -> 8
            else -> 10
        }
        val rows = (count + cols - 1) / cols
        val cellByWidth: Dp = (maxWidth / cols) - cellPadding * 2
        val cellByHeight: Dp = (maxHeight / rows) - cellPadding * 2
        val cellSize: Dp = cellByWidth
            .coerceAtMost(cellByHeight)
            .coerceAtMost(80.dp)
            .coerceAtLeast(28.dp)
        val fontSize = (cellSize.value * 0.65f).sp

        FlowRow(
            maxItemsInEachRow = cols,
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.Center
        ) {
            animals.forEachIndexed { index, animal ->
                val isTapped = index in tappedIndices
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(cellPadding)
                        .size(cellSize)
                        .clip(CircleShape)
                        .background(
                            if (isTapped) SkyBlue.copy(alpha = 0.3f)
                            else Color.Transparent
                        )
                        .border(
                            width = if (isTapped) 2.dp else 0.dp,
                            color = if (isTapped) SkyBlue else Color.Transparent,
                            shape = CircleShape
                        )
                        .clickable { onTap(index) }
                ) {
                    Text(text = animal.emoji, fontSize = fontSize)
                }
            }
        }
    }
}

