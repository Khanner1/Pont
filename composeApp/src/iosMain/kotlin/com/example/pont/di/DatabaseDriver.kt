package com.example.pont.di

import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.pont.data.AppDatabase
import platform.Foundation.NSHomeDirectory

fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbFilePath = NSHomeDirectory() + "/pont_database.db"
    return Room.databaseBuilder<AppDatabase>(
        name = dbFilePath
    )
}