package com.example.pont

import androidx.compose.ui.window.ComposeUIViewController
import com.example.pont.ui.theme.PontTheme
import platform.Foundation.NSLog

fun MainViewController() = ComposeUIViewController {
    NSLog("🟢 MainViewController: About to render AppNavigation")
    AppNavigation()

}
