package com.example.pont.ui.puzzle


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.example.pont.data.Article
import com.example.pont.data.ArticleItem
import com.example.pont.data.Hint
import com.example.pont.data.Puzzle
import com.example.pont.data.getDrawable
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun PuzzleScreen(
    puzzleId: String,
    modifier: Modifier = Modifier,
    puzzleViewModel: PuzzleViewModel = koinViewModel()
) {
    LaunchedEffect(puzzleId) {
        puzzleViewModel.loadPuzzle(puzzleId)
    }


    val puzzle by puzzleViewModel.puzzle.collectAsState()
    val currentPuzzle = puzzle ?: return
    //val guessCount by puzzleViewModel.guessCount.collectAsState()
    val showHint2 by puzzleViewModel.showHint2.collectAsState()
    val showAnswer by puzzleViewModel.showAnswer.collectAsState()
    val guessCount by puzzleViewModel.guessCount.collectAsState()

    val maxAttempts = 2
    val attemptsRemaining = maxAttempts - guessCount.coerceAtLeast(0)



    val scrollState = rememberScrollState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    )
    {
        GivenWord(
            puzzle = currentPuzzle
        )

        Spacer(modifier = Modifier.height(16.dp))

        WordInputRow(
            wordLength = currentPuzzle.answer.length,
            onSubmit = puzzleViewModel::submitGuess
        )

        Spacer(modifier = Modifier.height(16.dp))

        Hints(
            puzzle = currentPuzzle,
            revealHint2 = showHint2,
        )

        Spacer(modifier = Modifier.height(80.dp))

        /*ArticleCard(
            articleContent = currentPuzzle.funFact,
            cardTitle = "Answer",
            isRevealed = showAnswer
        )*/

        ArticleView(
            article = currentPuzzle.funFact,
            isRevealed = showAnswer,
            attemptsRemaining = attemptsRemaining,
            modifier = Modifier.padding(16.dp)
        )

        /*AnimatedVisibility(visible = showAnswer) {
            ArticleView(
                firstTestPuzzle.funFact,
                modifier = Modifier.padding(16.dp)
            )
        }*/

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
                content = puzzle.example
            )
        }


    }
}

@Composable
fun ExpandableInfoCard(
    modifier: Modifier = Modifier,
    cardTitle: String,
    content: String,
    initiallyExpanded: Boolean = false
) {
    var expanded by remember {mutableStateOf(initiallyExpanded)}

    Card(
        modifier = modifier,
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 5.dp),
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
                }
            }
        }
    }
}

@Composable
fun WordInputRow(
    wordLength: Int,
    onSubmit: (String) -> Unit
) {
    var enteredWord by remember {mutableStateOf("")}

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
    ) {
        OutlinedTextField(
            value = enteredWord,
            onValueChange = {newValue ->
                if(newValue.length <= wordLength) {
                    enteredWord = newValue.uppercase().filter {!it.isWhitespace()}
                }
            },
            label = {Text("Enter Answer ($wordLength letters)")},
            singleLine = true,
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
                    if(enteredWord.length == wordLength) {
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
            enabled = enteredWord.length == wordLength,
            modifier = Modifier.width(150.dp)
        ) {
            Text(text = "Submit")
        }
    }
}

@Composable
fun Hints(
    modifier : Modifier = Modifier,
    puzzle: Puzzle,
    revealHint2: Boolean
) {
    var hint1Revealed by remember { mutableStateOf(false) }
    //var hint2Revealed by remember { mutableStateOf(false) }

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
    modifier: Modifier = Modifier,
    cardTitle : String,
    hintContent : Hint,
    isRevealed : Boolean,
    cardWidth: Dp = 300.dp,
    cardHeight: Dp = 175.dp
) {
    val rotationY by animateFloatAsState(
        targetValue = if (isRevealed) 180f else 0f,
        animationSpec = tween(durationMillis = 2000),
        label = "hintCardRotation"
    )


    Card(
        modifier = modifier
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
            if(rotationY <= 90f) {
                Text(
                    text = cardTitle
                )
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
                                painter = org.jetbrains.compose.resources.painterResource(hintContent.getDrawable()),
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
    modifier: Modifier = Modifier,
) {

    var articleItems by remember(article) {mutableStateOf(article.items)}
    // KMP-compatible way to get screen width
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

            // Paragraphs, Images, and Bibliography
            articleItems.forEach { item ->
                ArticleItemRenderer(
                    item,
                    onToggle = {
                        // This updates the local state list to toggle expansion
                        articleItems = articleItems.map {
                            if (it === item && it is ArticleItem.Bibliography) {
                                it.copy(isExpanded = !it.isExpanded)
                            } else it
                        }
                    }
                )
            }
        }

        if(offsetX < screenWidth) {
            Surface(
                modifier = Modifier
                    .matchParentSize()
                    .offset(x = offsetX),
                color = MaterialTheme.colorScheme.secondaryContainer
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = if (attemptsRemaining > 0)
                            "$attemptsRemaining attempts remaining"
                        else
                            "Revealing Fact",
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
                style = MaterialTheme.typography.bodyMedium, // Body Font
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
        is ArticleItem.Image -> {
            Column(modifier = Modifier.padding(16.dp)) {
                Image(
                    painterResource(item.getDrawable()),
                    contentDescription = item.caption
                )

                item.caption?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.labelSmall, // Caption Font
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
                            imageVector = if (item.isExpanded) Icons.Default.KeyboardArrowUp
                            else Icons.Default.KeyboardArrowDown,
                            contentDescription = null
                        )
                    }

                    if (item.isExpanded) {
                        item.sources.forEach { source ->
                            Text("• $source", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}


/*@Composable
@Preview
fun PuzzleScreenPreview() {
    PuzzleScreen(puzzleId = "gen_puzzle_001_Cabeza")
}

@Composable
@Preview
fun ArticleViewPreview() {
    ArticleView(
        firstTestPuzzle.funFact,
        isRevealed = true,
        attemptsRemaining = 3
    )
}*/