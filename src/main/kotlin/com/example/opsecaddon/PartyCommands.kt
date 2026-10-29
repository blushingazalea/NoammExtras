package com.example.opsecaddon

import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents
import net.minecraft.client.Minecraft
import kotlin.random.Random

/**
 * Listens for party commands in party chat (same idea as NoammAddons' PartyHelper) and replies in party chat.
 *
 * Hypixel party line format: "Party > [MVP+] Name: !opsec"
 *
 * Add new commands by extending the registry below without touching the listener logic.
 */
object PartyCommands {
    private data class CommandDefinition(
        val name: String,
        val handler: (mc: Minecraft, sender: String) -> String,
    )

    private val commands = linkedMapOf(
        "opsec" to CommandDefinition("opsec") { mc, _ -> OpsecInfo.summary(mc) },
        "femboy" to CommandDefinition("femboy") { _, sender -> "$sender is ${femboyRoll()}% femboy" },
    )

    private val commandPattern = commands.keys.joinToString("|") { Regex.escape(it) }
    private val PARTY_LINE = Regex(
        "^Party > (?:\\[[^\\]]+\\] )?(\\w{1,16}): !($commandPattern)\\s*$",
        RegexOption.IGNORE_CASE
    )
    private val COLOR_CODES = Regex("§.")
    private const val COOLDOWN_MS = 3_000L

    /** Flip to false to turn all commands off at runtime. */
    @Volatile
    var enabled = true

    private var lastReply = 0L

    /** 0-100, but with a 5% chance the result is 200 instead. */
    fun femboyRoll(): Int = if (Random.nextInt(100) < 5) 200 else Random.nextInt(0, 101)

    fun init() {
        ClientReceiveMessageEvents.GAME.register { message, overlay ->
            if (overlay || !enabled) return@register

            val parsed = parsePartyCommand(message.string) ?: return@register
            val now = System.currentTimeMillis()
            if (now - lastReply < COOLDOWN_MS) return@register
            lastReply = now

            val (sender, commandName) = parsed
            val command = commands[commandName] ?: return@register

            val mc = Minecraft.getInstance()
            mc.execute { sendPartyMessage(mc, command.handler(mc, sender)) }
        }
    }

    private fun parsePartyCommand(rawMessage: String): Pair<String, String>? {
        val line = rawMessage.replace(COLOR_CODES, "").trim()
        val match = PARTY_LINE.matchEntire(line) ?: return null
        return match.groupValues[1] to match.groupValues[2].lowercase()
    }

    /**
     * Vanilla way to send a party message. You can swap this body for
     * NoammAddons' own ChatUtils.sendPartyMessage(text) (what PartyHelper uses).
     */
    private fun sendPartyMessage(mc: Minecraft, text: String) {
        mc.player?.connection?.sendCommand("pc $text")
    }
}
