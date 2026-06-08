package com.wowo.wowo.shared.valueobject

import com.wowo.wowo.shared.domain.*
import java.math.*

/**
 * Value Object representing monetary amount with currency
 */
data class Money(
    val amount: BigDecimal, val currency: Currency
) : ValueObject {

    init {
        require(amount.scale() <= 2) { "Amount can have at most 2 decimal places" }
    }

    fun add(other: Money): Money {
        require(currency == other.currency) { "Cannot add money with different currencies" }
        return Money(amount.add(other.amount).setScale(2, RoundingMode.HALF_UP), currency)
    }

    fun subtract(other: Money): Money {
        require(currency == other.currency) { "Cannot subtract money with different currencies" }
        return Money(amount.subtract(other.amount).setScale(2, RoundingMode.HALF_UP), currency)
    }

    fun multiply(multiplier: BigDecimal): Money {
        return Money(amount.multiply(multiplier).setScale(2, RoundingMode.HALF_UP), currency)
    }

    fun isPositive(): Boolean = amount > BigDecimal.ZERO
    fun isNegative(): Boolean = amount < BigDecimal.ZERO
    fun isZero(): Boolean = amount.compareTo(BigDecimal.ZERO) == 0

    operator fun plus(other: Money): Money {
        require(currency == other.currency) {
            "Cannot add money with different currencies"
        }

        return Money(
            amount.add(other.amount).setScale(2, RoundingMode.HALF_UP), currency
        )
    }

    operator fun minus(other: Money): Money {
        require(currency == other.currency) {
            "Cannot subtract money with different currencies"
        }

        return Money(
            amount.subtract(other.amount).setScale(2, RoundingMode.HALF_UP), currency
        )
    }

    companion object {
        @JvmStatic
        fun zero(currency: Currency) = Money(BigDecimal.ZERO.setScale(2), currency)
    }
}
