package com.github.noamm9.untitled

import com.github.noamm9.NoammAddons
import net.fabricmc.api.ClientModInitializer

object Untitled: ClientModInitializer {
    override fun onInitializeClient() {
        NoammAddons.logger.info("Hi from Addon!")
    }
}