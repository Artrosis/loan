package ru.predictor.loan

import android.os.Build

actual fun getPlatform(): Platform = MobilePlatform()
actual fun isMobile(): Boolean = true