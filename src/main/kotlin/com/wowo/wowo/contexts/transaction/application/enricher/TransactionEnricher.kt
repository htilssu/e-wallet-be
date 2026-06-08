package com.wowo.wowo.contexts.transaction.application.enricher

import com.wowo.wowo.contexts.payment.domain.valueobject.*
import com.wowo.wowo.contexts.payment.infrastructure.persistence.*
import com.wowo.wowo.contexts.transaction.application.dto.*
import com.wowo.wowo.shared.enrichment.*
import org.springframework.stereotype.*
import java.util.*

@Component
class TransactionEnricher(val walletRepositoryAdapter: WalletRepositoryAdapter) : IEnricher<TransactionDTO> {
    override fun enrich(data: TransactionDTO): TransactionDTO {
        val sourceWallet = walletRepositoryAdapter.findById(WalletId(UUID.fromString(data.sourceWalletId)))
        val targetWallet = walletRepositoryAdapter.findById(WalletId(UUID.fromString(data.targetWalletId)))

        if (sourceWallet == null || targetWallet == null) {
            throw IllegalArgumentException("Source or Target wallet not found for transaction id '${data.sourceWalletId}'")
        }

        data.sourceOwnerId = sourceWallet.ownerId
        data.sourceOwnerType = sourceWallet.ownerType

        data.targetOwnerId = targetWallet.ownerId
        data.targetOwnerType = targetWallet.ownerType


        return data
    }

    override suspend fun enrichMany(data: List<TransactionDTO>): List<TransactionDTO> {


        return data
    }
}