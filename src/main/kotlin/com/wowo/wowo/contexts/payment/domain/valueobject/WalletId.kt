package com.wowo.wowo.contexts.payment.domain.valueobject

import com.wowo.wowo.shared.domain.ValueObject
import java.util.*

/**
 * Value Object representing Wallet ID
 */
data class WalletId(val value: UUID = UUID.randomUUID()) : ValueObject {
    override fun toString(): String = value.toString()

    companion object {
        fun fromString(id: String): WalletId = WalletId(UUID.fromString(id))
    }
}


