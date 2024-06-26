package io.ikutsu.osumusic.core.presentation.util

fun String.withThousandSeparator(): String {
    return this.reversed().chunked(3).joinToString(",").reversed()
}