package com.example.pont

import androidx.compose.ui.window.ComposeUIViewController
import com.example.pont.ui.theme.PontTheme
import platform.Foundation.NSLog

fun MainViewController() = ComposeUIViewController {
    NSLog("🟢 MainViewController: ComposeUIViewController created")
    try {
        NSLog("🟢 About to call AppNavigation()")
        AppNavigation()
        NSLog("🟢 AppNavigation() returned successfully")
    } catch (e: Exception) {
        NSLog("🔴 ERROR in AppNavigation: ${e.message}")
        throw e
    }
}
