package io.ikutsu.osumusic

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform