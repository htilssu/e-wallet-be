package com.wowo.wowo.contexts.payment.domain.entity

import com.wowo.wowo.contexts.payment.domain.valueobject.*
import com.wowo.wowo.shared.domain.*
import com.wowo.wowo.shared.valueobject.*
import java.time.*

class LedgerTransaction(
    val transactionNo: String,
    val type: LedgerTransactionType,
    var status: LedgerTransactionStatus,
    val currency: Currency,
    val totalAmount: Money,
    val referenceType: String?,
    val referenceId: String?,
    val description: String?,
    val idempotencyKey: String?,
    val createdBy: String?,
    var completedAt: Instant?,
    var failedAt: Instant?,
    var failureReason: String?,
    private val _entries: MutableList<LedgerEntry>,
    override val id: LedgerTransactionId
) : AggregateRoot<LedgerTransactionId>() {
    val entries: List<LedgerEntry>
        get() = _entries.toList()

    companion object {
        fun create(
            transactionNo: String,
            type: LedgerTransactionType,
            totalAmount: Money,
            currency: Currency = totalAmount.currency,
            referenceType: String? = null,
            referenceId: String? = null,
            description: String? = null,
            idempotencyKey: String? = null,
            createdBy: String? = null
        ): LedgerTransaction {
            require(totalAmount.isPositive()) {
                "Total amount must be positive"
            }

            return LedgerTransaction(
                id = LedgerTransactionId(),
                transactionNo = transactionNo,
                type = type,
                status = LedgerTransactionStatus.PENDING,
                currency = currency,
                totalAmount = totalAmount,
                referenceType = referenceType,
                referenceId = referenceId,
                description = description,
                idempotencyKey = idempotencyKey,
                createdBy = createdBy,
                completedAt = null,
                failedAt = null,
                failureReason = null,
                _entries = mutableListOf()
            )
        }
    }

    fun addEntry(entry: LedgerEntry) {
        require(status == LedgerTransactionStatus.PENDING) {
            "Cannot add entry when transaction is not pending"
        }

        require(entry.ledgerTransactionId == id) {
            "Entry does not belong to this ledger transaction"
        }

        require(entry.currency == currency) {
            "Entry currency must match transaction currency"
        }

        _entries.add(entry)
    }

    fun complete() {
        require(status == LedgerTransactionStatus.PENDING) {
            "Only pending transaction can be completed"
        }

        require(_entries.isNotEmpty()) {
            "Ledger transaction must have at least one entry"
        }

        validateBalancedEntries()

        status = LedgerTransactionStatus.COMPLETED
        completedAt = Instant.now()
    }

    fun fail(reason: String) {
        require(status == LedgerTransactionStatus.PENDING) {
            "Only pending transaction can be failed"
        }

        status = LedgerTransactionStatus.FAILED
        failedAt = Instant.now()
        failureReason = reason
    }

    fun cancel() {
        require(status == LedgerTransactionStatus.PENDING) {
            "Only pending transaction can be cancelled"
        }

        status = LedgerTransactionStatus.CANCELLED
    }

    fun reverse() {
        require(status == LedgerTransactionStatus.COMPLETED) {
            "Only completed transaction can be reversed"
        }

        status = LedgerTransactionStatus.REVERSED
    }

    private fun validateBalancedEntries() {
        val debitTotal = _entries.filter { it.direction == LedgerEntryDirection.DEBIT }
            .fold(Money.zero(currency)) { total, entry -> total + entry.amount }

        val creditTotal = _entries.filter { it.direction == LedgerEntryDirection.CREDIT }
            .fold(Money.zero(currency)) { total, entry -> total + entry.amount }

        require(debitTotal == creditTotal) {
            "Ledger entries are not balanced"
        }

        require(debitTotal == totalAmount) {
            "Ledger entries total does not match transaction total amount"
        }
    }
}

enum class LedgerTransactionType {
    TRANSFER, DEPOSIT, WITHDRAW, REFUND, FEE, CASHBACK, HOLD, RELEASE, ADJUSTMENT
}

enum class LedgerTransactionStatus {
    PENDING, COMPLETED, FAILED, CANCELLED, REVERSED
}