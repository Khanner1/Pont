package com.example.pont.ui.library

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.pont.data.Puzzle
import com.example.pont.ui.theme.PontTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    onSelectPuzzle: (String) -> Unit,
    libraryViewModel: LibraryViewModel = koinViewModel(),
    modifier: Modifier = Modifier
) {
    val puzzles by libraryViewModel.displayedPuzzles.collectAsState()
    val filterOption by libraryViewModel.filter.collectAsState()

    LibraryContent(
        puzzles = puzzles,
        filterOption = filterOption,
        onFilterChanged = libraryViewModel::updateFilter,
        onSelectPuzzle = onSelectPuzzle,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryContent(
    puzzles: List<Puzzle>,
    filterOption: PuzzleFilter,
    onFilterChanged: (PuzzleFilter) -> Unit,
    onSelectPuzzle: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 10.dp)
    ) {
        PuzzleListHeader(
            selectedOption = filterOption,
            onOptionSelected = onFilterChanged
        )
        Spacer(modifier = Modifier.padding(10.dp))
        PuzzleList(
            puzzles = puzzles,
            onPuzzleClicked = onSelectPuzzle
        )
    }
}

@Composable
fun PuzzleList(
    puzzles: List<Puzzle>,
    onPuzzleClicked: (String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    LazyColumn(
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        items(items = puzzles, key = { puzzle -> puzzle.id }) { puzzle ->
            PuzzleListItem(
                puzzle = puzzle,
                onPuzzleClicked = onPuzzleClicked,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 30.dp, end = 30.dp)
                    .height(50.dp)
            )
        }
    }
}

@Composable
fun PuzzleListItem(
    puzzle: Puzzle,
    onPuzzleClicked: (String) -> Unit,
    modifier: Modifier = Modifier,
    highlightColor: Color = MaterialTheme.colorScheme.secondaryContainer
) {
    val statusBackgroundColor = if (puzzle.isSolved) {
        Color(0xFFC8E6C9)
    } else {
        Color(0xFFFFCDD2)
    }

    Card(
        modifier = modifier
            .clickable { onPuzzleClicked(puzzle.id) }
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxHeight()
        ) {
            Box(
                modifier = Modifier
                    .weight(0.3f)
                    .fillMaxHeight()
                    .background(highlightColor)
            ) {
                Text(
                    text = puzzle.word,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            val statusText = if (puzzle.isSolved) "Solved" else "Not Solved"

            Box(
                modifier = Modifier
                    .weight(0.4f)
                    .fillMaxHeight()
                    .background(statusBackgroundColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = statusText,
                    textAlign = TextAlign.Center
                )
            }

            Box(
                modifier = Modifier
                    .weight(0.3f)
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = puzzle.language,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PuzzleListHeader(
    selectedOption: PuzzleFilter,
    onOptionSelected: (PuzzleFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Row(modifier = modifier) {
        Text(
            text = "Order By:",
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 10.dp, end = 10.dp)
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = modifier
        ) {
            OutlinedTextField(
                value = selectedOption.toDisplayName(),
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                PuzzleFilter.entries.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(text = selectionOption.toDisplayName()) },
                        onClick = {
                            onOptionSelected(selectionOption)
                            expanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun LibraryScreenPreview() {
    PontTheme {
        LibraryContent(
            puzzles = emptyList(),
            filterOption = PuzzleFilter.DATE,
            onFilterChanged = {},
            onSelectPuzzle = {}
        )
    }
}