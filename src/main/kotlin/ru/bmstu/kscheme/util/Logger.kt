package ru.bmstu.kscheme.util

import java.util.logging.Level
import java.util.logging.Logger

object Logger {
    private val julLogger = Logger.getLogger("kscheme")
    val levelValue
        get() = getLevelValue(julLogger.level)

    fun warning(ex: Exception) {
        julLogger.log(java.util.logging.Level.WARNING, ex) { ex.message }
    }

    fun warning(level: Level, msg: String) {
        julLogger.log(getJulLevel(level.value), msg)
    }

    fun log(level: Level, msg: String) {
        julLogger.log(getJulLevel(level.value), msg)
    }

    private fun getJulLevel(lvl: Int): java.util.logging.Level? {
        var level = lvl
        if (level < 0) level = 0
        val n = when (level) {
            0 -> java.util.logging.Level.ALL
            1 -> java.util.logging.Level.FINE
            2 -> java.util.logging.Level.CONFIG
            3 -> java.util.logging.Level.INFO
            4 -> java.util.logging.Level.WARNING
            5 -> java.util.logging.Level.SEVERE
            else -> java.util.logging.Level.OFF
        }
        return n
    }

    private fun getLevelValue(level: java.util.logging.Level): Int {
        return if (level.intValue() == Int.MAX_VALUE) {
            Level.OFF.value
        } else if (level.intValue() >= JUL_THRESHOLD_ALL) {
            Level.ERROR.value
        } else if (level.intValue() >= JUL_THRESHOLD_WARNING) {
            Level.WARNING.value
        } else if (level.intValue() >= JUL_THRESHOLD_INFO) {
            Level.INFO.value
        } else if (level.intValue() >= JUL_THRESHOLD_CONFIG) {
            Level.CONFIG.value
        } else if (level.intValue() >= JUL_THRESHOLD_FINE) {
            Level.DEBUG.value
        } else {
            Level.ALL.value
        }
    }

    enum class Level(val value: Int) {
        OFF(6), ERROR(5), WARNING(4), INFO(3), CONFIG(2), DEBUG(1), ALL(0)
    }

    private const val JUL_THRESHOLD_ALL = 1000
    private const val JUL_THRESHOLD_WARNING = 900
    private const val JUL_THRESHOLD_INFO = 800
    private const val JUL_THRESHOLD_CONFIG = 700
    private const val JUL_THRESHOLD_FINE = 500
}
