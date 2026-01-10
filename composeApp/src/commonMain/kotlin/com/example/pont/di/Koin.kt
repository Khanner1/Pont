package com.example.pont.di

import com.example.pont.data.AppDatabase
import com.example.pont.data.LocalPuzzleRepository
import com.example.pont.data.PuzzleRepository
import com.example.pont.data.getAppDatabase
import com.example.pont.data.loadPuzzles
import com.example.pont.ui.home.HomeViewModel
import com.example.pont.ui.library.LibraryViewModel
import com.example.pont.ui.puzzle.PuzzleViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.dsl.module


val dataModule = module {
    // 1. Provide the Database
    // get() looks for the RoomDatabase.Builder provided in androidMain
    single<AppDatabase> { getAppDatabase(get()) }

    // 2. Provide the DAO (assuming your Repository needs the Dao, not the DB)
    single { get<AppDatabase>().puzzleDao() }

    // 3. Provide the Repository
    // get() here looks for the PuzzleDao provided above
    single<PuzzleRepository> { LocalPuzzleRepository(get()) }
}

val viewModelModule = module {
    // viewModelOf handles the constructor injection for HomeViewModel(repository)
    viewModelOf(::HomeViewModel)
    viewModelOf(::LibraryViewModel)
    viewModelOf(::PuzzleViewModel)
}

// Update initKoin to accept the platform-specific module (androidModule or iosModule)
fun initKoin(platformModule: Module) {
    startKoin {
        modules(
            platformModule, // This provides the Room Builder
            dataModule,
            viewModelModule,
        )
    }
}
