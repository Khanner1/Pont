package com.example.pont

import android.app.Application
import com.example.pont.di.androidModule
import com.example.pont.di.dataModule
import com.example.pont.di.initKoin
import com.example.pont.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

class PontApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@PontApp)
            modules(
                androidModule,
                dataModule,
                viewModelModule
            )
        }
    }
}
