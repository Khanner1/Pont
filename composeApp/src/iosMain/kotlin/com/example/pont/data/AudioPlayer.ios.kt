package com.example.pont.data

import platform.Foundation.NSLog

// A placeholder class that does nothing but satisfies the interface
private class IosAudioPlayer : AudioPlayer {
    override fun play(url: String) {
        // NSLog is the iOS equivalent of Print/Log
        NSLog("iOS AudioPlayer: Requested to play $url")
    }

    override fun stop() {
        NSLog("iOS AudioPlayer: Requested to stop")
    }
}

// The actual function the compiler is looking for
actual fun getAudioPlayer(): AudioPlayer {
    return IosAudioPlayer()
}