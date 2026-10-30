package com.noamm9.noammextras

import com.github.noamm9.utils.ChatUtils
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents
import net.minecraft.client.Minecraft
import kotlin.random.Random

/** Listens for party commands and queues replies through NoammAddons' paced chat helper. */
object PartyCommands {
    private data class CommandDefinition(
        val name: String,
        val handler: (mc: Minecraft, sender: String) -> String,
    )

    private val commands = linkedMapOf(
        "opsec" to CommandDefinition("opsec") { mc, _ -> SystemInfo.summary(mc) },
        "femboy" to CommandDefinition("femboy") { _, sender -> "$sender is ${femboyRoll()}% femboy" },
    )

    private val commandPattern = commands.keys.joinToString("|") { Regex.escape(it) }
    private val PARTY_LINE = Regex(
        "^Party > (?:\\[[^\\]]+\\] )?(\\w{1,16}): !($commandPattern)\\s*$",
        RegexOption.IGNORE_CASE
    )
    private val COLOR_CODES = Regex("§.")

    @Volatile
    var enabled = true

    fun femboyRoll(): Int = if (Random.nextInt(100) < 5) 200 else Random.nextInt(0, 101)

    fun init() {
        ClientReceiveMessageEvents.GAME.register { message, overlay ->
            if (overlay || !enabled) return@register

            val parsed = parsePartyCommand(message.string) ?: return@register
            val (sender, commandName) = parsed
            val command = commands[commandName] ?: return@register

            val mc = Minecraft.getInstance()
            mc.execute { ChatUtils.sendPartyMessage(command.handler(mc, sender)) }
        }
    }

    private fun parsePartyCommand(rawMessage: String): Pair<String, String>? {
        val line = rawMessage.replace(COLOR_CODES, "").trim()
        val match = PARTY_LINE.matchEntire(line) ?: return null
        return match.groupValues[1] to match.groupValues[2].lowercase()
    }
}