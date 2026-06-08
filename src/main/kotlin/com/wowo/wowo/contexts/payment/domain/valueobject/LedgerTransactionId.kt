package com.wowo.wowo.contexts.payment.domain.valueobject

import com.wowo.wowo.shared.domain.ValueObject
import java.util.UUID

class LedgerTransactionId(val value: UUID = UUID.randomUUID()) : ValueObject {
    override fun toString(): String = value.toString()

    companion object {
        fun fromString(id: String): LedgerTransactionId = LedgerTransactionId(UUID.fromString(id))
    }
}
