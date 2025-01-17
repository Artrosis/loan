package ru.predictor.loan

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
expect fun isMobile(): Boolean