package com.example.pont.ui.home



import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import com.example.pont.data.Puzzle
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.absoluteValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onGoToLibrary: () -> Unit,
    onPlaySelectedPuzzle: (String) -> Unit,
    homeViewModel: HomeViewModel = koinViewModel(),
    modifier: Modifier = Modifier
) {

    val puzzles by homeViewModel.allPuzzles.collectAsState()
    var showTutorial by remember {mutableStateOf(false)}

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        modifier = modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            IconButton(
                onClick = { showTutorial = true },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(all = 16.dp)
            ) {
                Icon(Icons.Default.Info, contentDescription = "How to play")
            }

            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxSize()
            )
            {
                if (puzzles.isNotEmpty()) {
                    PuzzleCarousel(
                        puzzles = puzzles,
                        onPuzzleClick = { puzzleId -> onPlaySelectedPuzzle(puzzleId) },
                        modifier = Modifier.height(250.dp).fillMaxWidth()
                    )
                } else {
                    WordOfDay(word = "Loading...", modifier = Modifier.width(350.dp).height(200.dp))
                }

                MainMenuButtons(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    onGoToLibrary = onGoToLibrary,
                    onPlayRandomPuzzle = {
                        if (puzzles.isNotEmpty()) {
                            val randomId = puzzles.random().id
                            onPlaySelectedPuzzle(randomId)
                        }
                    },
                    clearData = {
                        homeViewModel.resetAllProgress()
                        scope.launch {
                            snackbarHostState.showSnackbar("Puzzle progress has been cleared")
                        }
                    }
                )
            }

            if (showTutorial) {
                TutorialDialog(onDismiss = { showTutorial = false })
            }
        }
    }
}

@Composable
private fun WordOfDay(
    word: String,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 5.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            horizontalAlignment = Alignment.CenterHorizontally
        ){

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Daily Word:",
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(50.dp))

            Text(
                text = word,
                fontSize = 30.sp,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun MainMenuButtons(
    modifier: Modifier = Modifier,
    onGoToLibrary: () -> Unit,
    onPlayRandomPuzzle: () -> Unit,
    clearData: () -> Unit
)
{
    Column(modifier = modifier) {

        val buttonWidth = 200.dp

        Button(
            onClick = onPlayRandomPuzzle,
            modifier = Modifier.width(buttonWidth)
        ) {
            Text(text = "Play Random Puzzle")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onGoToLibrary,
            modifier = Modifier.width(buttonWidth)
        ) {
            Text(text = "Puzzle Library")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = clearData,
            modifier = Modifier.width(buttonWidth)
        ) {
            Text(text = "Clear Puzzle Progress")
        }
    }
}


@Composable
fun PuzzleCarousel(
    puzzles: List<Puzzle>,
    onPuzzleClick: (String) -> Unit,
    modifier: Modifier = Modifier) {
    val pagerState = rememberPagerState(pageCount = { puzzles.size })

    HorizontalPager(
        state = pagerState,
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 40.dp)
    ) { page ->
        val puzzle = puzzles[page]

        Card(
            onClick = { onPuzzleClick(puzzle.id) },
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 5.dp),
            modifier = Modifier
                .width(350.dp)
                .height(200.dp)
                .graphicsLayer {
                    val pageOffset = (
                            (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
                            ).absoluteValue
                    alpha = lerp(0.5f, 1f, 1f - pageOffset.coerceIn(0f, 1f))
                    scaleY = lerp(0.8f, 1f, 1f - pageOffset.coerceIn(0f, 1f))
                }
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    "${puzzle.language}",
                    fontSize = 14.sp,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(12.dp),
                    style = MaterialTheme.typography.labelSmall
                )

                Text(
                    text = puzzle.word,
                    fontSize = 32.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
fun TutorialDialog(onDismiss: () -> Unit) {
    var pageIndex by remember { mutableStateOf(0) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(if (pageIndex == 0) "About Pont" else "How to Play")
        },
        text = {
            if (pageIndex == 0) {
                Text("Pont is a game about finding the hidden connections between words in different languages.")
            } else {
                Text(
                    "Given a word from a different language(with a definition and an example sentence) than English and hints, try to guess the word in English that is etymologically related to it.\n \n" +
                        "Note: The non-English Word may have more meanings, but I chose the definition that is most relevant for guessing the answer.")
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (pageIndex == 0) {
                        pageIndex = 1
                    } else {
                        onDismiss()
                    }
                }
            ) {
                Text(if (pageIndex == 0) "Next →" else "Got it!")
            }
        },
        dismissButton = {
            if (pageIndex == 1) {
                TextButton(onClick = { pageIndex = 0 }) {
                    Text("Back")
                }
            }
        }
    )
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        onPlaySelectedPuzzle = {},
        onGoToLibrary = {}
    )
}