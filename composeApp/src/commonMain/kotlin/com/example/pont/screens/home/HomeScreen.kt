package com.example.pont.ui.home


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onGoToLibrary: () -> Unit,
    onPlayRandomPuzzle: (String) -> Unit,
    homeViewModel: HomeViewModel = koinViewModel(),
    modifier: Modifier = Modifier
) {

    val puzzles by homeViewModel.allPuzzles.collectAsState()


    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
        .fillMaxSize()
    )
    {
        WordOfDay(
            modifier = Modifier.width(350.dp)
                .align(Alignment.CenterHorizontally)
                .height(200.dp)
        )
        MainMenuButtons(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onGoToLibrary = onGoToLibrary,
            onPlayRandomPuzzle = {
                if (puzzles.isNotEmpty()) {
                    val randomId = puzzles.random().id
                    onPlayRandomPuzzle(randomId)
                }
            },
            clearData = homeViewModel::resetAllProgress
        )
    }
}

@Composable
private fun WordOfDay(
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
                //modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(50.dp))

            Text(
                text = "Cabeza",
                fontSize = 30.sp,
                textAlign = TextAlign.Center,
                //modifier = Modifier.align(Alignment.CenterHorizontally)
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

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        onPlayRandomPuzzle = {},
        onGoToLibrary = {}
    )
}