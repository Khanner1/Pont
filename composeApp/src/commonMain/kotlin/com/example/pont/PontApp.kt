package com.example.pont

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.pont.ui.home.HomeScreen
import com.example.pont.ui.library.LibraryScreen
import com.example.pont.ui.puzzle.PuzzleScreen
import com.example.pont.ui.theme.PontTheme
import kotlinx.serialization.Serializable

@Serializable
object Home

@Serializable
object Library

@Serializable
data class PuzzleRoute(val puzzleId: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    println("🟢 AppNavigation: Starting")

    val navController = rememberNavController()
    println("🟢 AppNavigation: NavController created")

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val topBarTitle = when {
        currentDestination?.hasRoute<Home>() == true -> "Pont"
        currentDestination?.hasRoute<Library>() == true -> "Library"
        currentDestination?.hasRoute<PuzzleRoute>() == true -> "Puzzle"
        else -> "Pont"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(topBarTitle) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer),
                navigationIcon = {
                    if (currentDestination?.hasRoute<Home>() == false) {
                        IconButton(onClick = {navController.navigateUp() }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back"
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        NavHost(navController = navController,
            startDestination = Home,
            modifier = Modifier.padding(innerPadding)) {
            composable<Home> {
                HomeScreen(
                    onPlaySelectedPuzzle = { puzzleId ->
                        navController.navigate(PuzzleRoute(puzzleId = puzzleId))
                    },
                    onGoToLibrary = { navController.navigate(Library) })
            }

            composable<Library> {
                LibraryScreen(onSelectPuzzle = { id ->
                    navController.navigate(PuzzleRoute(puzzleId = id))
                })
            }

            composable<PuzzleRoute> { backStackEntry ->
                val route: PuzzleRoute = backStackEntry.toRoute()
                PuzzleScreen(puzzleId = route.puzzleId)
            }
        }
    }
}
