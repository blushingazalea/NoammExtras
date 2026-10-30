package com.noamm9.noammextras

import net.fabricmc.api.ClientModInitializer

class NoammExtras : ClientModInitializer {
    override fun onInitializeClient() {
        PartyCommands.init()
    }
}