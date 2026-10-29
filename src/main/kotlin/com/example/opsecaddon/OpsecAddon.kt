package com.example.opsecaddon

import net.fabricmc.api.ClientModInitializer

class OpsecAddon : ClientModInitializer {
    override fun onInitializeClient() {
        PartyCommands.init()
    }
}
