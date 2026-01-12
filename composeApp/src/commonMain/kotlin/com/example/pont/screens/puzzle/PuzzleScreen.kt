package com.example.pont.ui.puzzle

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.pont.data.Article
import com.example.pont.data.ArticleItem
import com.example.pont.data.Hint
import com.example.pont.data.Puzzle
import com.example.pont.data.getDrawable
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PuzzleScreen(
    puzzleId: String,
    puzzleViewModel: PuzzleViewModel = koinViewModel(),
    modifier: Modifier = Modifier
) {
    LaunchedEffect(puzzleId) {
        puzzleViewModel.loadPuzzle(puzzleId)
    }

    val puzzle by puzzleViewModel.puzzle.collectAsState()
    val currentPuzzle = puzzle ?: return
    val showHint2 by puzzleViewModel.showHint2.collectAsState()
    val showAnswer by puzzleViewModel.showAnswer.collectAsState()
    val guessCount by puzzleViewModel.guessCount.collectAsState()

    var isSuccess by remember { mutableStateOf(false) }
    var shakeTrigger by remember { mutableStateOf(0) }

    val maxAttempts = 2
    val attemptsRemaining = maxAttempts - guessCount.coerceAtLeast(0)

    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        puzzleViewModel.lastGuessResult.collect { correct ->
            if (correct) {
                isSuccess = true
            } else {
                isSuccess = false
                shakeTrigger++
            }
        }
    }

    LaunchedEffect(showAnswer) {
        if (showAnswer) {
            scrollState.animateScrollTo(
                value = scrollState.maxValue,
                animationSpec = tween(durationMillis = 800)
            )
        }
    }

    var panelReveal by remember { mutableStateOf(false) }

    LaunchedEffect(showAnswer) {
        if (showAnswer) {
            delay(600)
            panelReveal = true
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        GivenWord(puzzle = currentPuzzle)

        Spacer(modifier = Modifier.height(16.dp))

        WordInputRow(
            wordLength = currentPuzzle.answer.length,
            attemptsRemaining = attemptsRemaining,
            isSuccess = isSuccess,
            shakeTrigger = shakeTrigger,
            onSubmit = puzzleViewModel::submitGuess
        )

        Spacer(modifier = Modifier.height(16.dp))

        Hints(
            puzzle = currentPuzzle,
            revealHint2 = showHint2
        )

        Spacer(modifier = Modifier.height(80.dp))

        ArticleView(
            article = currentPuzzle.funFact,
            isRevealed = panelReveal,
            attemptsRemaining = attemptsRemaining,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun GivenWord(
    puzzle: Puzzle,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = puzzle.word,
            fontSize = 40.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ExpandableInfoCard(
                cardTitle = "Definition",
                content = puzzle.definition
            )

            Spacer(modifier = Modifier.height(60.dp))

            ExpandableInfoCard(
                cardTitle = "Example Sentence",
                content = puzzle.example,
                optionalContent = puzzle.exampleTranslation
            )
        }
    }
}

@Composable
fun ExpandableInfoCard(
    cardTitle: String,
    content: String,
    optionalContent: String? = null,
    initiallyExpanded: Boolean = false,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(initiallyExpanded) }

    Card(
        modifier = modifier,
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .clickable { expanded = !expanded }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = cardTitle,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Show $cardTitle"
                )
            }

            AnimatedVisibility(visible = expanded) {
                Column {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = content,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Justify
                    )

                    if (optionalContent != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = optionalContent,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontStyle = FontStyle.Italic,
                            textAlign = TextAlign.Justify
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WordInputRow(
    wordLength: Int,
    attemptsRemaining: Int,
    isSuccess: Boolean,
    shakeTrigger: Int,
    onSubmit: (String) -> Unit
) {
    var enteredWord by remember { mutableStateOf("") }
    var flashColor by remember { mutableStateOf<Color?>(null) }
    var isUILocked by remember { mutableStateOf(false) }

    LaunchedEffect(shakeTrigger) {
        if (shakeTrigger > 0) {
            flashColor = Color.Red
            delay(500)
            flashColor = null

            if (attemptsRemaining <= 0) {
                isUILocked = true
            }
        }
    }

    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            flashColor = Color.Green
            delay(500)
            flashColor = null
            isUILocked = true
        }
    }

    val borderColor = flashColor ?: MaterialTheme.colorScheme.outline

    val animationOffset = remember(shakeTrigger) { Animatable(0f) }
    LaunchedEffect(shakeTrigger) {
        if (shakeTrigger > 0) {
            animationOffset.animateTo(
                targetValue = 0f,
                animationSpec = keyframes {
                    durationMillis = 400
                    -10f at 50
                    10f at 100
                    -10f at 150
                    10f at 200
                    -5f at 250
                    5f at 300
                    0f at 400
                }
            )
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
            .offset(x = animationOffset.value.dp)
    ) {
        OutlinedTextField(
            value = enteredWord,
            onValueChange = { newValue ->
                if (newValue.length <= wordLength) {
                    enteredWord = newValue.uppercase().filter { !it.isWhitespace() }
                }
            },
            enabled = !isUILocked,
            label = { Text("Enter Answer ($wordLength letters)") },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = borderColor,
                unfocusedIndicatorColor = borderColor,
                errorIndicatorColor = Color.Red
            ),
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            ),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Characters,
                autoCorrectEnabled = false,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (enteredWord.length == wordLength) {
                        onSubmit(enteredWord)
                    }
                }
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                onSubmit(enteredWord)
            },
            enabled = enteredWord.length == wordLength && !isUILocked,
            modifier = Modifier.width(150.dp)
        ) {
            Text(text = "Submit")
        }
    }
}

@Composable
fun Hints(
    puzzle: Puzzle,
    revealHint2: Boolean,
    modifier: Modifier = Modifier
) {
    var hint1Revealed by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        hint1Revealed = true
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HintCard(
            cardTitle = "Hint 1",
            hintContent = puzzle.hint1,
            isRevealed = hint1Revealed
        )

        Spacer(modifier = Modifier.height(60.dp))

        HintCard(
            cardTitle = "Hint 2",
            hintContent = puzzle.hint2,
            isRevealed = revealHint2
        )
    }
}

@Composable
fun HintCard(
    cardTitle: String,
    hintContent: Hint,
    isRevealed: Boolean,
    cardWidth: Dp = 300.dp,
    cardHeight: Dp = 175.dp,
    modifier: Modifier = Modifier
) {
    val rotationY by animateFloatAsState(
        targetValue = if (isRevealed) 180f else 0f,
        animationSpec = tween(durationMillis = 2000),
        label = "hintCardRotation"
    )

    val cardZIndex = if (rotationY > 0f && rotationY < 180f) 1f else 0f

    Card(
        modifier = modifier
            .zIndex(cardZIndex)
            .size(width = cardWidth, height = cardHeight)
            .graphicsLayer {
                this.rotationY = rotationY
                if (rotationY > 90) {
                    this.scaleX = -1f
                }
            },
        shape = RectangleShape,
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 5.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (rotationY <= 90f) {
                Text(text = cardTitle)
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            if (this@graphicsLayer.scaleX == -1f) {
                                this.scaleX = -1f
                            }
                        }
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    when (hintContent) {
                        is Hint.TextHint -> {
                            Text(text = hintContent.text)
                        }
                        is Hint.ImageHint -> {
                            Image(
                                painter = painterResource(hintContent.getDrawable()),
                                contentDescription = hintContent.caption
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ArticleView(
    article: Article,
    isRevealed: Boolean,
    attemptsRemaining: Int,
    modifier: Modifier = Modifier
) {
    var articleItems by remember(article) { mutableStateOf(article.items) }

    val density = LocalDensity.current
    val windowInfo = LocalWindowInfo.current
    val screenWidth = with(density) { windowInfo.containerSize.width.toDp() }

    val offsetX by animateDpAsState(
        targetValue = if (isRevealed) screenWidth else 0.dp,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "CurtainSlider"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clipToBounds()
    ) {
        Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = article.title,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            articleItems.forEach { item ->
                ArticleItemRenderer(
                    item = item,
                    onToggle = {
                        articleItems = articleItems.map {
                            if (it === item && it is ArticleItem.Bibliography) {
                                it.copy(isExpanded = !it.isExpanded)
                            } else {
                                it
                            }
                        }
                    }
                )
            }
        }

        if (offsetX < screenWidth) {
            Surface(
                modifier = Modifier
                    .matchParentSize()
                    .offset(x = offsetX),
                color = MaterialTheme.colorScheme.secondaryContainer
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = if (attemptsRemaining > 0) {
                            "$attemptsRemaining attempts remaining"
                        } else {
                            "Revealing Answer..."
                        },
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
        }
    }
}

@Composable
fun ArticleItemRenderer(
    item: ArticleItem,
    onToggle: () -> Unit
) {
    when (item) {
        is ArticleItem.Paragraph -> {
            Text(
                text = item.text,
                style = MaterialTheme.typography.bodyMedium.copy(
                    lineHeight = 24.sp,
                    textAlign = TextAlign.Justify
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
        is ArticleItem.Image -> {
            Column(modifier = Modifier.padding(16.dp)) {
                Image(
                    painter = painterResource(item.getDrawable()),
                    contentDescription = item.caption
                )
                item.caption?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
        is ArticleItem.Bibliography -> {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggle() }
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Bibliography", fontWeight = FontWeight.Bold)
                        Spacer(Modifier.weight(1f))
                        Icon(
                            imageVector = if (item.isExpanded) {
                                Icons.Default.KeyboardArrowUp
                            } else {
                                Icons.Default.KeyboardArrowDown
                            },
                            contentDescription = null
                        )
                    }
                    if (item.isExpanded) {
                        item.sources.forEach { source ->
                            Text("• $source", style = MaterialTheme.typography.bodySmall)
                            Spacer(Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}