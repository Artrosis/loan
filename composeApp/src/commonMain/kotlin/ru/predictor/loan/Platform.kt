package ru.predictor.loan

import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset

interface Platform {
    val peopleOffset: Density.() -> IntOffset
    val manufactureOffset: Density.() -> IntOffset
    val marketOffset: Density.() -> IntOffset
    val bankOffset: Density.() -> IntOffset
    val bankMoneyOffset: Density.() -> IntOffset
    val moneyToMarketOffset: Density.() -> IntOffset
    val moneyToPeopleOffset: Density.() -> IntOffset
    val moneyToManufactureOffset: Density.() -> IntOffset
    val movePeopleWorkOffset: Density.() -> IntOffset
    val moveProductsFromManufactureToPeopleOffset: Density.() -> IntOffset
    val moveProductsFromManufactureToMarketOffset: Density.() -> IntOffset
    val moveProductsFromMarketToPeopleOffset: Density.() -> IntOffset
}

open class MobilePlatform: Platform {
    override val peopleOffset: Density.() -> IntOffset = { IntOffset(-380, 130) }
    override val manufactureOffset: Density.() -> IntOffset = { IntOffset(440, 150) }
    override val marketOffset: Density.() -> IntOffset = { IntOffset(-130, -230) }
    override val bankOffset: Density.() -> IntOffset = { IntOffset(50, -10) }
    override val bankMoneyOffset: Density.() -> IntOffset = { IntOffset(0, 30) }
    override val moneyToMarketOffset: Density.() -> IntOffset = { IntOffset(-40, -80) }
    override val moneyToPeopleOffset: Density.() -> IntOffset = { IntOffset(-40, 10) }
    override val moneyToManufactureOffset: Density.() -> IntOffset = { IntOffset(170, 40) }
    override val movePeopleWorkOffset: Density.() -> IntOffset = { IntOffset(-250, 110) }
    override val moveProductsFromManufactureToPeopleOffset: Density.() -> IntOffset = { IntOffset(260, 100) }
    override val moveProductsFromManufactureToMarketOffset: Density.() -> IntOffset = { IntOffset(280, 120) }
    override val moveProductsFromMarketToPeopleOffset: Density.() -> IntOffset = { IntOffset(-220, -220) }
}

open class DesktopPlatform: Platform {
    override val peopleOffset: Density.() -> IntOffset = { IntOffset(-380, 130) }
    override val manufactureOffset: Density.() -> IntOffset = { IntOffset(440, 150) }
    override val marketOffset: Density.() -> IntOffset = { IntOffset(-130, -230) }
    override val bankOffset: Density.() -> IntOffset = { IntOffset(50, -10) }
    override val bankMoneyOffset: Density.() -> IntOffset = { IntOffset(0, 30) }
    override val moneyToMarketOffset: Density.() -> IntOffset = { IntOffset(-40, -80) }
    override val moneyToPeopleOffset: Density.() -> IntOffset = { IntOffset(-40, 10) }
    override val moneyToManufactureOffset: Density.() -> IntOffset = { IntOffset(170, 40) }
    override val movePeopleWorkOffset: Density.() -> IntOffset = { IntOffset(-250, 110) }
    override val moveProductsFromManufactureToPeopleOffset: Density.() -> IntOffset = { IntOffset(260, 100) }
    override val moveProductsFromManufactureToMarketOffset: Density.() -> IntOffset = { IntOffset(280, 120) }
    override val moveProductsFromMarketToPeopleOffset: Density.() -> IntOffset = { IntOffset(-220, -220) }
}

expect fun getPlatform(): Platform
expect fun isMobile(): Boolean