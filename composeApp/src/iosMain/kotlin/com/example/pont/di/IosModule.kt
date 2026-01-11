package com.example.pont.di

import androidx.room.RoomDatabase
import com.example.pont.data.AppDatabase
import org.koin.dsl.module

val iosModule = module {
    // Provide the RoomDatabase.Builder for iOS
    single<RoomDatabase.Builder<AppDatabase>> {
        getDatabaseBuilder() // iOS-specific function
    }
}