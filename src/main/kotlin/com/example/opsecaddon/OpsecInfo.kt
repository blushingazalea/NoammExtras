package com.example.opsecaddon

import net.minecraft.client.Minecraft
import java.time.Instant
import java.time.ZoneId

/** Collects the three values the !opsec command reports. */
object OpsecInfo {
    private const val UNKNOWN_OS = "Unknown"

    /** e.g. "Windows 11", "Linux", "Mac OS X" */
    fun os(): String = System.getProperty("os.name")?.takeIf(String::isNotBlank) ?: UNKNOWN_OS

    /** Actual framebuffer resolution in pixels (what the game renders at). */
    fun resolution(mc: Minecraft): String = mc.window.let { "${it.width}x${it.height}" }

    /**
     * Rough Hypixel-style region from the system time zone (offline, no network calls).
     * Uses the *standard* offset so daylight saving doesn't change the result.
     *   UTC-12 .. UTC-3  -> NA
     *   UTC-2  .. UTC+4  -> EU   (Europe, Africa, Middle East)
     *   UTC+4:30 and up  -> ASIA (Asia, Oceania)
     */
    fun region(zone: ZoneId = ZoneId.systemDefault()): String {
        val hours = zone.rules.getStandardOffset(Instant.now()).totalSeconds / 3600.0
        return when {
            hours < -2.5 -> "NA"
            hours < 4.5 -> "EU"
            else -> "ASIA"
        }
    }

    fun summary(mc: Minecraft): String = "OS: ${os()} | Resolution: ${resolution(mc)} | Region: ${region()}"
}
