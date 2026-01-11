package com.example.pont.di

import android.content.Context
import com.example.pont.data.AppDatabase
import org.koin.dsl.module
import androidx.room.Room
import androidx.room.RoomDatabase
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
val androidModule = module {
    single<RoomDatabase.Builder<AppDatabase>> {
        val context = get<Context>()
        val dbFile = context.getDatabasePath("puzzle_database")

        Room.databaseBuilder<AppDatabase>(
            context = context.applicationContext,
            name = dbFile.absolutePath,
        )
    }
}


