package com.wowo.wowo.contexts.payment.domain.valueobject

import com.wowo.wowo.shared.domain.ValueObject
import java.util.UUID

data class LedgerEntryId(val value: UUID = UUID.randomUUID()) : ValueObject {
    override fun toString(): String = value.toString()

    companion object {
        fun fromString(id: String): LedgerEntryId = LedgerEntryId(UUID.fromString(id))
    }
}