package com.wowo.wowo.contexts.payment.application.usecase

import com.wowo.wowo.contexts.payment.application.dto.WalletDTO
import com.wowo.wowo.contexts.payment.domain.repository.WalletRepository
import com.wowo.wowo.contexts.payment.domain.valueobject.WalletId
import com.wowo.wowo.shared.enrichment.Enricher
import com.wowo.wowo.shared.exception.EntityNotFoundException
import org.springframework.stereotype.*

@Service
class GetWalletDetailUseCase(private val walletRepository: WalletRepository, private val enricher: Enricher) {
    fun execute(walletId: String): WalletDTO {
        val wallet = walletRepository.findById(WalletId.fromString(walletId))
            ?: throw EntityNotFoundException("Wallet not found with ID: $walletId")
        val walletDTO = WalletDTO.fromDomain(wallet)
        enricher.enrich(walletDTO)
        return walletDTO
    }
}