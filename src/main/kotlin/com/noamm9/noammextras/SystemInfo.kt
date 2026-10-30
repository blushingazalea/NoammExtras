package com.noamm9.noammextras

import net.minecraft.client.Minecraft
import java.nio.file.Files
import java.nio.file.Path
import java.time.Instant
import java.time.ZoneId

/** Collects the values reported by the !opsec command. */
object SystemInfo {
    private const val UNKNOWN_OS = "Unknown"

    /** e.g. "Windows 11", "Linux", "Mac OS X" */
    fun os(): String = System.getProperty("os.name")?.takeIf(String::isNotBlank) ?: UNKNOWN_OS

    private fun linuxDistro(): String? {
        if (!os().contains("linux", ignoreCase = true)) return null

        return runCatching {
            Files.readAllLines(Path.of("/etc/os-release"))
                .firstOrNull { it.startsWith("NAME=") }
                ?.substringAfter('=')
                ?.trim()
                ?.removeSurrounding("\"")
                ?.takeIf(String::isNotBlank)
        }.getOrNull()
    }

    /** Actual framebuffer resolution in pixels (what the game renders at). */
    fun resolution(mc: Minecraft): String = mc.window.let { "${it.width}x${it.height}" }

    /**
     * Rough Hypixel-style region from the system time zone (offline, no network calls).
     * Uses the standard offset so daylight saving doesn't change the result.
     */
    fun region(zone: ZoneId = ZoneId.systemDefault()): String {
        val hours = zone.rules.getStandardOffset(Instant.now()).totalSeconds / 3600.0
        return when {
            hours < -2.5 -> "NA"
            hours < 4.5 -> "EU"
            else -> "ASIA"
        }
    }

    fun summary(mc: Minecraft): String = buildString {
        append("OS: ${os()}")
        linuxDistro()?.let { append(" ($it)") }
        append(" | Resolution: ${resolution(mc)} | Region: ${region()}")
    }
}