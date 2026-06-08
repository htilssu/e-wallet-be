package com.wowo.wowo.contexts.payment.domain.entity

import com.wowo.wowo.contexts.payment.domain.valueobject.LedgerEntryId
import com.wowo.wowo.contexts.payment.domain.valueobject.LedgerTransactionId
import com.wowo.wowo.shared.domain.*
import com.wowo.wowo.shared.valueobject.*

class LedgerEntry(
    val ledgerTransactionId: LedgerTransactionId,
    val walletId: String,
    val direction: LedgerEntryDirection,
    val amount: Money,
    val currency: Currency,
    val balanceBefore: Money,
    val balanceAfter: Money,
    val entryType: LedgerEntryType,
    val description: String?,
    override val id: LedgerEntryId,
) : AggregateRoot<LedgerEntryId>() {
    companion object {
        fun debit(
            ledgerTransactionId: LedgerTransactionId,
            walletId: String,
            amount: Money,
            balanceBefore: Money,
            balanceAfter: Money,
            entryType: LedgerEntryType,
            description: String? = null
        ): LedgerEntry {
            require(amount.isPositive()) {
                "Amount must be positive"
            }

            require(balanceAfter == balanceBefore - amount) {
                "Invalid debit balance calculation"
            }

            return LedgerEntry(
                id = LedgerEntryId(),
                ledgerTransactionId = ledgerTransactionId,
                walletId = walletId,
                direction = LedgerEntryDirection.DEBIT,
                amount = amount,
                currency = amount.currency,
                balanceBefore = balanceBefore,
                balanceAfter = balanceAfter,
                entryType = entryType,
                description = description,
            )
        }

        fun credit(
            ledgerTransactionId: LedgerTransactionId,
            walletId: String,
            amount: Money,
            balanceBefore: Money,
            balanceAfter: Money,
            entryType: LedgerEntryType,
            description: String? = null
        ): LedgerEntry {
            require(amount.isPositive()) {
                "Amount must be positive"
            }

            require(balanceAfter == balanceBefore + amount) {
                "Invalid credit balance calculation"
            }

            return LedgerEntry(
                id = LedgerEntryId(),
                ledgerTransactionId = ledgerTransactionId,
                walletId = walletId,
                direction = LedgerEntryDirection.CREDIT,
                amount = amount,
                currency = amount.currency,
                balanceBefore = balanceBefore,
                balanceAfter = balanceAfter,
                entryType = entryType,
                description = description,
            )
        }
    }
}

enum class LedgerEntryDirection {
    DEBIT, CREDIT
}

enum class LedgerEntryType {
    TRANSFER_OUT, TRANSFER_IN, DEPOSIT, WITHDRAW, FEE, REFUND, HOLD, RELEASE, ADJUSTMENT
}