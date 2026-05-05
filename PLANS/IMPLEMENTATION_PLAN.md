# NumberAdditionSubtractionAndroid - 實作計畫

## 概述

將 `CountNumberZoo_Android`（動物數數遊戲）移植到 `NumberAdditionSubtractionAndroid`，新增加法/減法/混合遊戲模式。舊專案位於 `D:\GitHub\Other2025\CountNumberZoo_Android`。

**套件名稱**: `com.example.numberadditionsubtractionandroid`
**舊套件名稱**: `com.example.countnumber`

---

## 階段 1：建置設定

### 1.1 `gradle/libs.versions.toml`

新增以下 3 個項目（從舊專案複製）：

```toml
# 新增到 [versions]
datastorePreferences = "1.1.4"

# 新增到 [libraries]
androidx-lifecycle-viewmodel-compose = { group = "androidx.lifecycle", name = "lifecycle-viewmodel-compose", version.ref = "lifecycleRuntimeKtx" }
androidx-datastore-preferences = { group = "androidx.datastore", name = "datastore-preferences", version.ref = "datastorePreferences" }
androidx-compose-material-icons-extended = { group = "androidx.compose.material", name = "material-icons-extended" }
```

### 1.2 `app/build.gradle.kts`

新增依賴並更新版號：

```kotlin
// 修改版號
versionCode = 1
versionName = "1.0.0"

// 新增到 dependencies 區塊：
implementation(libs.androidx.lifecycle.viewmodel.compose)
implementation(libs.androidx.datastore.preferences)
implementation(libs.androidx.compose.material.icons.extended)
```

---

## 階段 2：資料層檔案

所有檔案放在 `app/src/main/java/com/example/numberadditionsubtractionandroid/data/`

### 2.1 `Animal.kt`

從舊專案複製，僅修改 package：

```kotlin
package com.example.numberadditionsubtractionandroid.data

data class Animal(
    val emoji: String,
    val nameChinese: String,
    val nameJapanese: String,
    val nameEnglish: String
)

fun Animal.localizedName(language: AppLanguage) = when (language) {
    AppLanguage.CHINESE -> nameChinese
    AppLanguage.ENGLISH -> nameEnglish
    AppLanguage.JAPANESE -> nameJapanese
}

val ALL_ANIMALS = listOf(
    Animal("\uD83D\uDC36", "狗", "いぬ", "Dog"),
    Animal("\uD83D\uDC31", "貓", "ねこ", "Cat"),
    Animal("\uD83D\uDC30", "兔子", "うさぎ", "Rabbit"),
    Animal("\uD83D\uDC3B", "熊", "くま", "Bear"),
    Animal("\uD83D\uDC3C", "熊貓", "パンダ", "Panda"),
    Animal("\uD83D\uDC37", "豬", "ぶた", "Pig"),
    Animal("\uD83D\uDC2E", "牛", "うし", "Cow"),
    Animal("\uD83D\uDC38", "青蛙", "かえる", "Frog"),
    Animal("\uD83D\uDC35", "猴子", "さる", "Monkey"),
    Animal("\uD83E\uDD81", "獅子", "ライオン", "Lion"),
    Animal("\uD83D\uDC2F", "老虎", "とら", "Tiger"),
    Animal("\uD83D\uDC14", "雞", "にわとり", "Chicken"),
    Animal("\uD83D\uDC27", "企鵝", "ペンギン", "Penguin"),
    Animal("\uD83D\uDC33", "鯨魚", "くじら", "Whale"),
    Animal("\uD83D\uDC18", "大象", "ぞう", "Elephant"),
    Animal("\uD83E\uDD92", "長頸鹿", "キリン", "Giraffe"),
    Animal("\uD83D\uDC11", "羊", "ひつじ", "Sheep"),
    Animal("\uD83D\uDC34", "馬", "うま", "Horse"),
    Animal("\uD83E\uDD8B", "蝴蝶", "ちょうちょ", "Butterfly"),
    Animal("\uD83D\uDC22", "烏龜", "かめ", "Turtle")
)
```

### 2.2 `GameSettings.kt`

**移除** `LayoutMode`，**新增** `GameMode`：

```kotlin
package com.example.numberadditionsubtractionandroid.data

enum class GameMode { ADDITION, SUBTRACTION, MIXED }

enum class VoiceMode { NONE, NUMBER, NUMBER_WITH_ANIMAL }

data class GameSettings(
    val rounds: Int = 5,
    val maxCount: Int = 10,
    val voiceMode: VoiceMode = VoiceMode.NUMBER,
    val language: AppLanguage = AppLanguage.CHINESE,
    val gameMode: GameMode = GameMode.ADDITION
)
```

### 2.3 `AppVersion.kt`

```kotlin
package com.example.numberadditionsubtractionandroid.data

const val APP_VERSION = "1.0.0"
const val APP_BUILD_DATE = "2026-05-05"
```

### 2.4 `GameResult.kt`

```kotlin
package com.example.numberadditionsubtractionandroid.data

data class LeaderboardEntry(
    val playerName: String,
    val score: Int,
    val correctCount: Int,
    val wrongCount: Int,
    val date: String
)
```

### 2.5 `LeaderboardRepository.kt`

從舊專案複製，將 package 改為 `com.example.numberadditionsubtractionandroid.data`。邏輯完全不變。

### 2.6 `AppPreferencesRepository.kt`

從舊專案複製，將 package 改為 `com.example.numberadditionsubtractionandroid.data`。邏輯完全不變。

### 2.7 `AppStrings.kt`

從舊專案複製，修改 package，**移除** `layout()`、`grid()`、`scattered()` 函式，**新增**以下函式：

```kotlin
package com.example.numberadditionsubtractionandroid.data

enum class AppLanguage { CHINESE, ENGLISH, JAPANESE }

object AppStrings {
    // 保留舊專案所有函式（除了 layout/grid/scattered）
    // ... （所有既有函式不變）...

    // 修改 appTitle：
    fun appTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "數字加減"
        AppLanguage.ENGLISH -> "Number Math"
        AppLanguage.JAPANESE -> "かずのたしひき"
    }

    // 新增以下函式：
    fun addition(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "加法"
        AppLanguage.ENGLISH -> "Addition"
        AppLanguage.JAPANESE -> "たしざん"
    }

    fun subtraction(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "減法"
        AppLanguage.ENGLISH -> "Subtraction"
        AppLanguage.JAPANESE -> "ひきざん"
    }

    fun mixed(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "混合"
        AppLanguage.ENGLISH -> "Mixed"
        AppLanguage.JAPANESE -> "ミックス"
    }

    fun gameMode(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "遊戲模式"
        AppLanguage.ENGLISH -> "Game Mode"
        AppLanguage.JAPANESE -> "ゲームモード"
    }

    fun tapToAdd(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "點動物來加加看！"
        AppLanguage.ENGLISH -> "Tap to add!"
        AppLanguage.JAPANESE -> "タップしてたそう！"
    }

    fun tapToCross(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "點動物畫X來減！"
        AppLanguage.ENGLISH -> "Tap to cross out!"
        AppLanguage.JAPANESE -> "タップしてけそう！"
    }
}
```

---

## 階段 3：TTS 層

### 3.1 `tts/TtsManager.kt`

從舊專案複製，修改 package 和 import：

```kotlin
package com.example.numberadditionsubtractionandroid.tts

import android.content.Context
import android.speech.tts.TextToSpeech
import com.example.numberadditionsubtractionandroid.data.Animal
import com.example.numberadditionsubtractionandroid.data.AppLanguage
import com.example.numberadditionsubtractionandroid.data.VoiceMode
import com.example.numberadditionsubtractionandroid.data.localizedName
import java.util.Locale

// 保留舊專案 TtsManager 所有程式碼不變
// （speakGameCount, speakAnimalName, speakText, speakFeedback, shutdown, localeFor, countPhrase）
```

---

## 階段 4：遊戲層

### 4.1 `game/GameState.kt`

**大幅改寫**以支援加減法：

```kotlin
package com.example.numberadditionsubtractionandroid.game

import com.example.numberadditionsubtractionandroid.data.Animal
import com.example.numberadditionsubtractionandroid.data.GameMode

data class RoundState(
    val animal: Animal,
    val num1: Int,
    val num2: Int,
    val operator: GameMode,           // ADDITION 或 SUBTRACTION（不會是 MIXED）
    val animals1: List<Animal>,       // 第一組（num1 隻動物）
    val animals2: List<Animal>,       // 第二組（加法時 num2 隻，減法時為空）
    val correctAnswer: Int,
    val answers: List<Int>,
    val tappedIndices1: Set<Int> = emptySet(),
    val tappedIndices2: Set<Int> = emptySet(),
    val crossedOutIndices: Set<Int> = emptySet(),
    val selectedAnswer: Int? = null,
    val isCorrect: Boolean? = null
)

data class GameUiState(
    val currentRound: Int = 0,
    val totalRounds: Int = 5,
    val round: RoundState? = null,
    val score: Int = 0,
    val correctCount: Int = 0,
    val wrongCount: Int = 0,
    val isGameOver: Boolean = false,
    val showFeedback: Boolean = false
)
```

### 4.2 `game/GameViewModel.kt`

**大幅改寫**：

```kotlin
package com.example.numberadditionsubtractionandroid.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.numberadditionsubtractionandroid.data.ALL_ANIMALS
import com.example.numberadditionsubtractionandroid.data.Animal
import com.example.numberadditionsubtractionandroid.data.GameMode
import com.example.numberadditionsubtractionandroid.data.GameSettings
import com.example.numberadditionsubtractionandroid.data.VoiceMode
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.random.Random

class GameViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private lateinit var settings: GameSettings
    private var onSpeakRoundCount: ((Int, Animal) -> Unit)? = null
    private var onSpeakFeedback: ((Boolean) -> Unit)? = null

    fun init(
        gameSettings: GameSettings,
        speakRoundCount: (Int, Animal) -> Unit,
        speakFeedback: (Boolean) -> Unit
    ) {
        settings = gameSettings
        onSpeakRoundCount = speakRoundCount
        onSpeakFeedback = speakFeedback
        _uiState.value = GameUiState(totalRounds = gameSettings.rounds)
        startNextRound()
    }

    private fun startNextRound() {
        val state = _uiState.value
        if (state.currentRound >= state.totalRounds) {
            _uiState.value = state.copy(isGameOver = true)
            return
        }

        val operator = when (settings.gameMode) {
            GameMode.ADDITION -> GameMode.ADDITION
            GameMode.SUBTRACTION -> GameMode.SUBTRACTION
            GameMode.MIXED -> if (Random.nextBoolean()) GameMode.ADDITION else GameMode.SUBTRACTION
        }

        val animal = ALL_ANIMALS.random()
        val num1: Int
        val num2: Int
        val correctAnswer: Int

        when (operator) {
            GameMode.ADDITION -> {
                num1 = Random.nextInt(1, (settings.maxCount / 2).coerceAtLeast(2) + 1)
                num2 = Random.nextInt(1, (settings.maxCount - num1).coerceAtLeast(1) + 1)
                correctAnswer = num1 + num2
            }
            GameMode.SUBTRACTION -> {
                num1 = Random.nextInt(2, settings.maxCount.coerceAtLeast(3) + 1)
                num2 = Random.nextInt(1, num1)
                correctAnswer = num1 - num2
            }
            else -> throw IllegalStateException()
        }

        val animals1 = List(num1) { animal }
        val animals2 = if (operator == GameMode.ADDITION) List(num2) { animal } else emptyList()
        val answers = generateAnswers(correctAnswer, settings.maxCount)

        _uiState.value = state.copy(
            currentRound = state.currentRound + 1,
            round = RoundState(
                animal = animal,
                num1 = num1,
                num2 = num2,
                operator = operator,
                animals1 = animals1,
                animals2 = animals2,
                correctAnswer = correctAnswer,
                answers = answers
            ),
            showFeedback = false
        )
    }

    fun tapAnimalGroup1(index: Int) {
        val round = _uiState.value.round ?: return
        if (round.selectedAnswer != null) return
        if (index in round.tappedIndices1) return
        val newTapped = round.tappedIndices1 + index
        val newRound = round.copy(tappedIndices1 = newTapped)
        _uiState.value = _uiState.value.copy(round = newRound)
        if (settings.voiceMode != VoiceMode.NONE) {
            val total = newTapped.size + round.tappedIndices2.size
            onSpeakRoundCount?.invoke(total, round.animal)
        }
    }

    fun tapAnimalGroup2(index: Int) {
        val round = _uiState.value.round ?: return
        if (round.selectedAnswer != null) return
        if (index in round.tappedIndices2) return
        val newTapped = round.tappedIndices2 + index
        val newRound = round.copy(tappedIndices2 = newTapped)
        _uiState.value = _uiState.value.copy(round = newRound)
        if (settings.voiceMode != VoiceMode.NONE) {
            val total = round.tappedIndices1.size + newTapped.size
            onSpeakRoundCount?.invoke(total, round.animal)
        }
    }

    fun crossOutAnimal(index: Int) {
        val round = _uiState.value.round ?: return
        if (round.selectedAnswer != null) return
        if (index in round.crossedOutIndices) return
        if (round.crossedOutIndices.size >= round.num2) return
        val newCrossed = round.crossedOutIndices + index
        val newRound = round.copy(crossedOutIndices = newCrossed)
        _uiState.value = _uiState.value.copy(round = newRound)
        if (settings.voiceMode != VoiceMode.NONE) {
            val remaining = round.num1 - newCrossed.size
            onSpeakRoundCount?.invoke(remaining, round.animal)
        }
    }

    fun selectAnswer(answer: Int) {
        val state = _uiState.value
        val round = state.round ?: return
        if (round.selectedAnswer != null) return

        val isCorrect = answer == round.correctAnswer
        val newRound = round.copy(selectedAnswer = answer, isCorrect = isCorrect)
        val newCorrect = if (isCorrect) state.correctCount + 1 else state.correctCount
        val newWrong = if (!isCorrect) state.wrongCount + 1 else state.wrongCount
        val newScore = max(0, settings.rounds * settings.maxCount * (newCorrect - newWrong))

        _uiState.value = state.copy(
            round = newRound,
            correctCount = newCorrect,
            wrongCount = newWrong,
            score = newScore,
            showFeedback = true
        )

        viewModelScope.launch {
            if (settings.voiceMode != VoiceMode.NONE) {
                onSpeakRoundCount?.invoke(answer, round.animal)
                delay(900)
                onSpeakFeedback?.invoke(isCorrect)
            }
            delay(1500)
            startNextRound()
        }
    }

    private fun generateAnswers(correct: Int, maxCount: Int): List<Int> {
        val distractors = mutableSetOf<Int>()
        var attempts = 0
        while (distractors.size < 4 && attempts < 200) {
            attempts++
            val delta = if (maxCount <= 20) Random.nextInt(1, 6) else max(1, (maxCount * 0.1).toInt())
            val sign = if (Random.nextBoolean()) 1 else -1
            val candidate = correct + sign * delta
            if (candidate > 0 && candidate != correct && candidate <= maxCount + delta) {
                distractors.add(candidate)
            }
        }
        var fill = 1
        while (distractors.size < 4) {
            if (fill != correct && !distractors.contains(fill)) distractors.add(fill)
            fill++
        }
        return (distractors.take(4) + correct).shuffled()
    }
}
```

---

## 階段 5：UI 主題

### 5.1 `ui/theme/Color.kt`

```kotlin
package com.example.numberadditionsubtractionandroid.ui.theme

import androidx.compose.ui.graphics.Color

val SunshineYellow = Color(0xFFFFD600)
val SkyBlue = Color(0xFF29B6F6)
val GrassGreen = Color(0xFF66BB6A)
val CoralRed = Color(0xFFFF5252)
val LavenderPurple = Color(0xFFCE93D8)
val OrangePeel = Color(0xFFFF9800)
val MintGreen = Color(0xFF80CBC4)
val BabyPink = Color(0xFFF48FB1)

val PrimaryColor = SkyBlue
val PrimaryDark = Color(0xFF0288D1)
val SecondaryColor = SunshineYellow
val TertiaryColor = GrassGreen
val BackgroundColor = Color(0xFFFFF9E6)
val SurfaceColor = Color(0xFFFFFFFF)
val CorrectGreen = Color(0xFF43A047)
val WrongRed = Color(0xFFE53935)

val AnswerColors = listOf(
    Color(0xFF42A5F5),
    Color(0xFFEF5350),
    Color(0xFF66BB6A),
    Color(0xFFFF7043),
    Color(0xFFAB47BC)
)
```

### 5.2 `ui/theme/Type.kt`

從舊專案複製，將 package 改為 `com.example.numberadditionsubtractionandroid.ui.theme`。

### 5.3 `ui/theme/Theme.kt`

```kotlin
package com.example.numberadditionsubtractionandroid.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val KidsColorScheme = lightColorScheme(
    primary = PrimaryColor,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    primaryContainer = SkyBlue.copy(alpha = 0.2f),
    secondary = SecondaryColor,
    onSecondary = androidx.compose.ui.graphics.Color.Black,
    tertiary = TertiaryColor,
    background = BackgroundColor,
    surface = SurfaceColor,
    onBackground = androidx.compose.ui.graphics.Color(0xFF1A1A1A),
    onSurface = androidx.compose.ui.graphics.Color(0xFF1A1A1A),
    error = WrongRed
)

@Composable
fun NumberAdditionSubtractionAndroidTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = KidsColorScheme,
        typography = Typography,
        content = content
    )
}
```

---

## 階段 6：UI 元件

### 6.1 `ui/components/AnimatedAnimal.kt`

從舊專案複製，將 package 改為 `com.example.numberadditionsubtractionandroid.ui.components`。無其他修改。

### 6.2 `ui/components/AnswerButtons.kt`

從舊專案複製，修改 package 和 import 為 `com.example.numberadditionsubtractionandroid.ui.components` / `...ui.theme`。

### 6.3 `ui/components/AnimalGrid.kt`

**大幅改寫** - 移除 SCATTERED 模式，新增加法/減法格子：

```kotlin
package com.example.numberadditionsubtractionandroid.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 第一組
        AnimalFlowGrid(
            animals = animals1,
            tappedIndices = tappedIndices1,
            onTap = onTapGroup1,
            modifier = Modifier.weight(1f).fillMaxWidth()
        )

        // 運算符號
        Box(
            modifier = Modifier
                .padding(vertical = 4.dp)
                .size(48.dp)
                .background(Color(0xFF43A047), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("+", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        // 第二組
        AnimalFlowGrid(
            animals = animals2,
            tappedIndices = tappedIndices2,
            onTap = onTapGroup2,
            modifier = Modifier.weight(1f).fillMaxWidth()
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
    BoxWithConstraints(
        modifier = modifier.fillMaxSize(),
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
```

---

## 階段 7：UI 畫面

### 7.1 `ui/screens/LanguageScreen.kt`

從舊專案複製，修改 package 和 import。

### 7.2 `ui/screens/HomeScreen.kt`

**大幅改寫** - 3 個模式按鈕取代單一「開始」按鈕：

```kotlin
package com.example.numberadditionsubtractionandroid.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.numberadditionsubtractionandroid.data.ALL_ANIMALS
import com.example.numberadditionsubtractionandroid.data.Animal
import com.example.numberadditionsubtractionandroid.data.AppLanguage
import com.example.numberadditionsubtractionandroid.data.AppStrings
import com.example.numberadditionsubtractionandroid.data.APP_VERSION
import com.example.numberadditionsubtractionandroid.data.GameMode
import com.example.numberadditionsubtractionandroid.ui.components.FloatingAnimal
import com.example.numberadditionsubtractionandroid.ui.theme.BackgroundColor
import com.example.numberadditionsubtractionandroid.ui.theme.GrassGreen
import com.example.numberadditionsubtractionandroid.ui.theme.LavenderPurple
import com.example.numberadditionsubtractionandroid.ui.theme.OrangePeel
import com.example.numberadditionsubtractionandroid.ui.theme.PrimaryColor
import com.example.numberadditionsubtractionandroid.ui.theme.SunshineYellow
import com.example.numberadditionsubtractionandroid.ui.theme.WrongRed

private val LANG_CYCLE = listOf(
    AppLanguage.CHINESE,
    AppLanguage.ENGLISH,
    AppLanguage.JAPANESE
)

@Composable
fun HomeScreen(
    language: AppLanguage,
    onStart: (GameMode) -> Unit,
    onSettings: () -> Unit,
    onLeaderboard: () -> Unit,
    onChangeLanguage: () -> Unit,
    onSpeakAnimal: (Animal, AppLanguage) -> Unit = { _, _ -> }
) {
    val startIdx = remember(language) { LANG_CYCLE.indexOf(language).coerceAtLeast(0) }
    var cycleOffset by remember(language) { mutableIntStateOf(0) }
    var reshuffleKey by remember { mutableIntStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFE3F2FD), BackgroundColor, Color(0xFFFFF9C4))
                )
            )
    ) {
        // 浮動動物背景
        ALL_ANIMALS.forEachIndexed { index, animal ->
            FloatingAnimal(
                emoji = animal.emoji,
                index = index,
                reshuffleKey = reshuffleKey,
                onClick = {
                    val langToSpeak = LANG_CYCLE[(startIdx + cycleOffset) % 3]
                    onSpeakAnimal(animal, langToSpeak)
                    cycleOffset = (cycleOffset + 1) % 3
                }
            )
        }

        // 重新排列按鈕
        Button(
            onClick = { reshuffleKey++ },
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(12.dp)
                .height(36.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.75f)),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 0.dp)
        ) {
            Text(
                "\uD83D\uDD00 " + when (language) {
                    AppLanguage.CHINESE -> "動物換位置"
                    AppLanguage.ENGLISH -> "Shuffle"
                    AppLanguage.JAPANESE -> "シャッフル"
                }, fontSize = 12.sp, color = Color(0xFF555555)
            )
        }

        // 版號標籤
        Text(
            text = "v$APP_VERSION",
            fontSize = 12.sp,
            color = Color(0xFF888888),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(12.dp)
        )

        // 前景 UI
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Text(
                    text = AppStrings.appTitle(language),
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryColor,
                    modifier = Modifier
                        .background(Color.White.copy(alpha = 0.7f), RoundedCornerShape(16.dp))
                        .padding(horizontal = 24.dp, vertical = 12.dp)
                )

                Spacer(Modifier.height(48.dp))

                // 3 個遊戲模式按鈕
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
                ) {
                    // 加法按鈕
                    Button(
                        onClick = { onStart(GameMode.ADDITION) },
                        modifier = Modifier.weight(1f).height(80.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GrassGreen)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("+", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text(AppStrings.addition(language), fontSize = 14.sp, color = Color.White)
                        }
                    }

                    // 減法按鈕
                    Button(
                        onClick = { onStart(GameMode.SUBTRACTION) },
                        modifier = Modifier.weight(1f).height(80.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = OrangePeel)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("\u2212", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text(AppStrings.subtraction(language), fontSize = 14.sp, color = Color.White)
                        }
                    }

                    // 混合按鈕
                    Button(
                        onClick = { onStart(GameMode.MIXED) },
                        modifier = Modifier.weight(1f).height(80.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = LavenderPurple)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("+\u2212", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text(AppStrings.mixed(language), fontSize = 14.sp, color = Color.White)
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Button(
                        onClick = onSettings,
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = OrangePeel),
                        modifier = Modifier.height(56.dp)
                    ) {
                        Icon(Icons.Default.Settings, contentDescription = null, tint = Color.White)
                        Text(" ${AppStrings.settings(language)}", fontSize = 18.sp, color = Color.White)
                    }

                    Button(
                        onClick = onLeaderboard,
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SunshineYellow),
                        modifier = Modifier.height(56.dp)
                    ) {
                        Text("\uD83C\uDFC6", fontSize = 20.sp)
                        Text(" ${AppStrings.leaderboard(language)}", fontSize = 18.sp, color = Color(0xFF5D4037))
                    }
                }

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = onChangeLanguage,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.8f)),
                    modifier = Modifier.height(44.dp)
                ) {
                    Text("\uD83C\uDF0D ", fontSize = 16.sp)
                    Text(AppStrings.changeLanguage(language), fontSize = 16.sp, color = Color(0xFF555555))
                }
            }

            // 提示文字
            Text(
                text = when (language) {
                    AppLanguage.CHINESE -> "\uD83D\uDCA1 可點選動物發音"
                    AppLanguage.ENGLISH -> "\uD83D\uDCA1 Tap animals to hear pronunciation"
                    AppLanguage.JAPANESE -> "\uD83D\uDCA1 動物をタップして発音を聞く"
                },
                fontSize = 13.sp,
                color = Color(0xFF666666),
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .background(Color.White.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }
    }
}
```

### 7.3 `ui/screens/SettingsScreen.kt`

從舊專案複製，**移除** LayoutMode 區塊，**新增** GameMode 選擇器。修改 package 和 import。將「排列方式」SettingsCard 替換為：

```kotlin
// 遊戲模式選擇器（取代排列方式）
SettingsCard {
    Text(AppStrings.gameMode(lang), fontSize = 18.sp, fontWeight = FontWeight.Bold)
    Spacer(Modifier.height(8.dp))
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        listOf(
            GameMode.ADDITION to AppStrings.addition(lang),
            GameMode.SUBTRACTION to AppStrings.subtraction(lang),
            GameMode.MIXED to AppStrings.mixed(lang)
        ).forEach { (mode, label) ->
            val selected = settings.gameMode == mode
            Button(
                onClick = { onSettingsChanged(settings.copy(gameMode = mode)) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selected) PrimaryColor else Color.LightGray
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(label, color = Color.White, fontSize = 14.sp)
            }
        }
    }
}
```

移除 `LayoutMode` 的 import，新增 `GameMode` 的 import。

### 7.4 `ui/screens/GameScreen.kt`

**大幅改寫** - 顯示算式，使用加法/減法動物格子：

```kotlin
package com.example.numberadditionsubtractionandroid.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.numberadditionsubtractionandroid.data.AppLanguage
import com.example.numberadditionsubtractionandroid.data.AppStrings
import com.example.numberadditionsubtractionandroid.data.GameMode
import com.example.numberadditionsubtractionandroid.data.GameSettings
import com.example.numberadditionsubtractionandroid.game.GameViewModel
import com.example.numberadditionsubtractionandroid.ui.components.AdditionAnimalGrid
import com.example.numberadditionsubtractionandroid.ui.components.AnswerButtons
import com.example.numberadditionsubtractionandroid.ui.components.SubtractionAnimalGrid
import com.example.numberadditionsubtractionandroid.ui.theme.BackgroundColor
import com.example.numberadditionsubtractionandroid.ui.theme.CorrectGreen
import com.example.numberadditionsubtractionandroid.ui.theme.GrassGreen
import com.example.numberadditionsubtractionandroid.ui.theme.PrimaryColor
import com.example.numberadditionsubtractionandroid.ui.theme.WrongRed

@Composable
fun GameScreen(
    viewModel: GameViewModel,
    settings: GameSettings,
    onGameOver: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val lang = settings.language
    var showExitDialog by remember { mutableStateOf(false) }

    if (uiState.isGameOver) {
        onGameOver()
        return
    }

    val round = uiState.round ?: return

    val operatorSymbol = if (round.operator == GameMode.ADDITION) "+" else "\u2212"
    val operatorColor = if (round.operator == GameMode.ADDITION) GrassGreen else WrongRed

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // 頂部：進度 + 算式
            Column {
                Text(
                    text = AppStrings.roundProgress(lang, uiState.currentRound, uiState.totalRounds),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryColor
                )
                Spacer(Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = { uiState.currentRound.toFloat() / uiState.totalRounds },
                    modifier = Modifier.fillMaxWidth(),
                    color = PrimaryColor
                )
                Spacer(Modifier.height(8.dp))

                // 算式列 + 退出按鈕
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // 算式：num1 op num2 = ?
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${round.num1}",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1565C0)
                        )
                        Text(text = " ", fontSize = 32.sp)
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(operatorColor, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = operatorSymbol,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        Text(text = " ", fontSize = 32.sp)
                        Text(
                            text = "${round.num2}",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFE65100)
                        )
                        Text(
                            text = " = ?",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF333333)
                        )
                    }

                    Button(
                        onClick = { showExitDialog = true },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0E0E0)),
                        modifier = Modifier.height(36.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 0.dp)
                    ) {
                        Text(
                            text = when (lang) {
                                AppLanguage.CHINESE -> "退出測試"
                                AppLanguage.ENGLISH -> "Quit"
                                AppLanguage.JAPANESE -> "終了"
                            },
                            fontSize = 13.sp,
                            color = Color(0xFF555555)
                        )
                    }
                }

                // 提示文字
                Text(
                    text = if (round.operator == GameMode.ADDITION) AppStrings.tapToAdd(lang)
                           else AppStrings.tapToCross(lang),
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            // 中間：動物格子
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                when (round.operator) {
                    GameMode.ADDITION -> AdditionAnimalGrid(
                        animals1 = round.animals1,
                        animals2 = round.animals2,
                        tappedIndices1 = round.tappedIndices1,
                        tappedIndices2 = round.tappedIndices2,
                        onTapGroup1 = { viewModel.tapAnimalGroup1(it) },
                        onTapGroup2 = { viewModel.tapAnimalGroup2(it) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    GameMode.SUBTRACTION -> SubtractionAnimalGrid(
                        animals = round.animals1,
                        crossedOutIndices = round.crossedOutIndices,
                        maxCrossOut = round.num2,
                        onCrossOut = { viewModel.crossOutAnimal(it) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    else -> {}
                }
            }

            // 底部：答案按鈕
            Column {
                AnswerButtons(
                    answers = round.answers,
                    correctAnswer = round.correctAnswer,
                    selectedAnswer = round.selectedAnswer,
                    onSelect = { viewModel.selectAnswer(it) }
                )
            }
        }

        // 退出確認對話框
        if (showExitDialog) {
            AlertDialog(
                onDismissRequest = { showExitDialog = false },
                title = {
                    Text(
                        text = when (lang) {
                            AppLanguage.CHINESE -> "退出測試"
                            AppLanguage.ENGLISH -> "Quit Game"
                            AppLanguage.JAPANESE -> "ゲームを終了"
                        },
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Text(
                        text = when (lang) {
                            AppLanguage.CHINESE -> "確定要退出測試嗎？\n目前的進度將不會被記錄。"
                            AppLanguage.ENGLISH -> "Are you sure you want to quit?\nYour current progress will not be saved."
                            AppLanguage.JAPANESE -> "本当にゲームを終了しますか？\n現在の進捗は記録されません。"
                        },
                        fontSize = 15.sp
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showExitDialog = false
                            onGameOver()
                        }
                    ) {
                        Text(
                            text = when (lang) {
                                AppLanguage.CHINESE -> "確定退出"
                                AppLanguage.ENGLISH -> "Quit"
                                AppLanguage.JAPANESE -> "終了する"
                            },
                            color = WrongRed,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showExitDialog = false }) {
                        Text(
                            text = when (lang) {
                                AppLanguage.CHINESE -> "繼續遊戲"
                                AppLanguage.ENGLISH -> "Keep Playing"
                                AppLanguage.JAPANESE -> "続ける"
                            },
                            color = PrimaryColor
                        )
                    }
                },
                shape = RoundedCornerShape(20.dp)
            )
        }

        // 回饋覆蓋層
        AnimatedVisibility(
            visible = uiState.showFeedback,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut(),
            modifier = Modifier.align(Alignment.Center)
        ) {
            val isCorrect = round.isCorrect == true
            Box(
                modifier = Modifier
                    .background(
                        color = if (isCorrect) CorrectGreen else WrongRed,
                        shape = RoundedCornerShape(24.dp)
                    )
                    .padding(horizontal = 48.dp, vertical = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = if (isCorrect) "\u2B50" else "\uD83D\uDE22",
                        fontSize = 48.sp
                    )
                    Text(
                        text = if (isCorrect) AppStrings.correct(lang) else AppStrings.wrong(lang),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    if (!isCorrect) {
                        Text(
                            text = "${round.correctAnswer}",
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
```

### 7.5 `ui/screens/ResultScreen.kt`

從舊專案複製，將所有 `com.example.countnumber` 的 import 改為 `com.example.numberadditionsubtractionandroid`。

---

## 階段 8：MainActivity

```kotlin
package com.example.numberadditionsubtractionandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.numberadditionsubtractionandroid.data.AppPreferencesRepository
import com.example.numberadditionsubtractionandroid.data.GameMode
import com.example.numberadditionsubtractionandroid.data.GameSettings
import com.example.numberadditionsubtractionandroid.data.LeaderboardRepository
import com.example.numberadditionsubtractionandroid.game.GameUiState
import com.example.numberadditionsubtractionandroid.game.GameViewModel
import com.example.numberadditionsubtractionandroid.tts.TtsManager
import com.example.numberadditionsubtractionandroid.ui.screens.GameScreen
import com.example.numberadditionsubtractionandroid.ui.screens.HomeScreen
import com.example.numberadditionsubtractionandroid.ui.screens.LanguageScreen
import com.example.numberadditionsubtractionandroid.ui.screens.ResultScreen
import com.example.numberadditionsubtractionandroid.ui.screens.SettingsScreen
import com.example.numberadditionsubtractionandroid.ui.theme.NumberAdditionSubtractionAndroidTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

enum class Screen { LANGUAGE, HOME, SETTINGS, GAME, RESULT }

class MainActivity : ComponentActivity() {

    private lateinit var ttsManager: TtsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ttsManager = TtsManager(this)
        enableEdgeToEdge()

        val repository = LeaderboardRepository(this)
        val appPreferencesRepository = AppPreferencesRepository(this)

        setContent {
            NumberAdditionSubtractionAndroidTheme {
                var screen by remember { mutableStateOf(Screen.HOME) }
                var settings by remember { mutableStateOf(GameSettings()) }
                var lastGameState by remember { mutableStateOf(GameUiState()) }
                var startupResolved by remember { mutableStateOf(false) }
                val gameViewModel: GameViewModel = viewModel()
                val scope = rememberCoroutineScope()

                LaunchedEffect(Unit) {
                    val hasSavedLanguage = appPreferencesRepository.hasSavedLanguage.first()
                    val savedLanguage = appPreferencesRepository.language.first()
                    settings = settings.copy(language = savedLanguage)
                    screen = if (hasSavedLanguage) Screen.HOME else Screen.LANGUAGE
                    startupResolved = true
                }

                if (!startupResolved) return@NumberAdditionSubtractionAndroidTheme

                when (screen) {
                    Screen.LANGUAGE -> LanguageScreen { lang ->
                        settings = settings.copy(language = lang)
                        scope.launch {
                            appPreferencesRepository.saveLanguage(lang)
                        }
                        screen = Screen.HOME
                    }

                    Screen.HOME -> HomeScreen(
                        language = settings.language,
                        onSpeakAnimal = { animal, lang ->
                            ttsManager.speakAnimalName(animal, lang)
                        },
                        onStart = { gameMode ->
                            val gameSettings = settings.copy(gameMode = gameMode)
                            val gameLanguage = gameSettings.language
                            val voiceMode = gameSettings.voiceMode
                            gameViewModel.init(
                                gameSettings = gameSettings,
                                speakRoundCount = { num, animal ->
                                    ttsManager.speakGameCount(num, animal, gameLanguage, voiceMode)
                                },
                                speakFeedback = { correct ->
                                    ttsManager.speakFeedback(correct, gameLanguage)
                                }
                            )
                            screen = Screen.GAME
                        },
                        onSettings = { screen = Screen.SETTINGS },
                        onLeaderboard = { screen = Screen.RESULT },
                        onChangeLanguage = { screen = Screen.LANGUAGE }
                    )

                    Screen.SETTINGS -> SettingsScreen(
                        settings = settings,
                        onSettingsChanged = { updatedSettings ->
                            val previousLanguage = settings.language
                            settings = updatedSettings
                            if (previousLanguage != updatedSettings.language) {
                                scope.launch {
                                    appPreferencesRepository.saveLanguage(updatedSettings.language)
                                }
                            }
                        },
                        onBack = { screen = Screen.HOME },
                        leaderboardRepository = repository
                    )

                    Screen.GAME -> GameScreen(
                        viewModel = gameViewModel,
                        settings = settings,
                        onGameOver = {
                            lastGameState = gameViewModel.uiState.value
                            screen = Screen.RESULT
                        }
                    )

                    Screen.RESULT -> ResultScreen(
                        gameState = lastGameState,
                        language = settings.language,
                        repository = repository,
                        onPlayAgain = {
                            val gameLanguage = settings.language
                            val voiceMode = settings.voiceMode
                            gameViewModel.init(
                                gameSettings = settings,
                                speakRoundCount = { num, animal ->
                                    ttsManager.speakGameCount(num, animal, gameLanguage, voiceMode)
                                },
                                speakFeedback = { correct ->
                                    ttsManager.speakFeedback(correct, gameLanguage)
                                }
                            )
                            screen = Screen.GAME
                        },
                        onHome = { screen = Screen.HOME }
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ttsManager.shutdown()
    }
}
```

---

## 階段 9：資源檔

### 9.1 `app/src/main/res/values/strings.xml`

修改 `app_name`：
```xml
<resources>
    <string name="app_name">數字加減</string>
</resources>
```

### 9.2 應用程式圖示

暫時保留現有的 `ic_launcher_foreground.xml` 和 `ic_launcher_background.xml`（之後可更新為數學主題設計）。

---

## 階段 10：刪除舊檔案

刪除將被新版取代的範本檔案：
- `app/src/main/java/com/example/numberadditionsubtractionandroid/MainActivity.kt`（被新版取代）
- `app/src/main/java/com/example/numberadditionsubtractionandroid/ui/theme/Color.kt`（被新版取代）
- `app/src/main/java/com/example/numberadditionsubtractionandroid/ui/theme/Type.kt`（被新版取代）
- `app/src/main/java/com/example/numberadditionsubtractionandroid/ui/theme/Theme.kt`（被新版取代）

---

## 階段 11：文件

### 11.1 更新 `CLAUDE.md`

更新架構說明以反映新的檔案結構、GameMode 和所有新路徑。

### 11.2 建立 `README.md`

專案說明（中文/英文/日文）、功能介紹、建置指令、MIT 授權。

### 11.3 建立 `CHANGELOG.md`

```markdown
# 變更紀錄

## v1.0.0 (2026-05-05)
- 初版發佈，基於 CountNumberZoo_Android 移植
- 加法模式：點擊動物數加總
- 減法模式：畫 X 去除動物求差
- 混合模式：每題隨機加法或減法
- 三語支援：中文、英文、日文
- TTS 語音回饋
- 排行榜（使用表情符號命名）
```

---

## 檔案建立順序（確保編譯成功）

1. 更新 `gradle/libs.versions.toml`
2. 更新 `app/build.gradle.kts`
3. 建立 `data/` 檔案（Animal、AppStrings、AppVersion、GameSettings、GameResult、LeaderboardRepository、AppPreferencesRepository）
4. 建立 `tts/TtsManager.kt`
5. 建立 `game/GameState.kt` 和 `game/GameViewModel.kt`
6. 建立 `ui/theme/` 檔案（Color、Type、Theme）- 覆蓋現有檔案
7. 建立 `ui/components/` 檔案（AnimatedAnimal、AnswerButtons、AnimalGrid）
8. 建立 `ui/screens/` 檔案（LanguageScreen、HomeScreen、SettingsScreen、GameScreen、ResultScreen）
9. 覆蓋 `MainActivity.kt`
10. 更新 `res/values/strings.xml`
11. 執行 `./gradlew assembleDebug` 驗證

---

## 與舊專案的主要差異

| 面向 | 舊版（CountNumberZoo） | 新版（NumberAdditionSubtraction） |
|------|----------------------|-------------------------------|
| 遊戲類型 | 數動物 | 動物加減法 |
| 模式 | 單一（數數） | 加法、減法、混合 |
| 排列方式 | 格子或散佈 | 僅格子（加法分兩組） |
| 題目 | 「有幾隻？」 | 「3 + 6 = ?」 |
| 互動 | 點擊計數 | 點擊累加 / 畫 X 減去 |
| 答案 | 總數 | 加總或差值 |
| 開始按鈕 | 單一「開始」 | 3 個模式按鈕 |
| 標題 | 動物數數 | 數字加減 |
| 主題名稱 | CountNumberTheme | NumberAdditionSubtractionAndroidTheme |
