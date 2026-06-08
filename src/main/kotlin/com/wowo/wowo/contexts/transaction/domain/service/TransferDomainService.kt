package com.wowo.wowo.contexts.transaction.domain.service

import com.wowo.wowo.contexts.transaction.domain.acl.WalletACL
import com.wowo.wowo.contexts.transaction.domain.entity.Transaction
import com.wowo.wowo.contexts.transaction.domain.valueobject.TransactionType
import com.wowo.wowo.shared.exception.EntityNotFoundException
import com.wowo.wowo.shared.exception.InsufficientBalanceException
import com.wowo.wowo.shared.valueobject.Money
import org.springframework.dao.CannotAcquireLockException
import org.springframework.dao.PessimisticLockingFailureException
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable

/**
 * Domain Service for Transfer Operations
 * Encapsulates business rules and invariants for money transfers
 */
class TransferDomainService(
    private val walletACL: WalletACL
) {
    /**
     * Execute a transfer between two wallets
     * @return Transaction entity with transfer details
     * @throws EntityNotFoundException if either wallet not found
     * @throws InsufficientBalanceException if source wallet has insufficient balance
     */
    @Retryable(
        retryFor = [CannotAcquireLockException::class, PessimisticLockingFailureException::class],
        maxAttempts = 3,
        backoff = Backoff(delay = 100, multiplier = 2.0)
    )
    fun executeTransfer(
        fromWalletId: String, toWalletId: String, amount: Money, description: String?
    ): Transaction {

        val transaction = Transaction.create(
            sourceWalletId = fromWalletId,
            targetWalletId = toWalletId,
            amount = amount,
            type = TransactionType.TRANSFER,
            description = description
        )

        try { // Execute the transfer through ACL
            walletACL.transfer(fromWalletId, toWalletId, amount)

            // Mark transaction as complete
            transaction.complete()

        } catch (e: Exception) { // If any error occurs, mark transaction as failed
            if (e is InsufficientBalanceException) {
                throw e
            }
            transaction.fail(e.message ?: "Transfer failed")
            throw e
        }

        return transaction
    }
}

