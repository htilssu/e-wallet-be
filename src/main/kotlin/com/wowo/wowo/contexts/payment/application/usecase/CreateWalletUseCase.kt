package com.wowo.wowo.contexts.payment.application.usecase

import com.wowo.wowo.contexts.payment.application.dto.*
import com.wowo.wowo.contexts.payment.domain.entity.*
import com.wowo.wowo.contexts.payment.domain.repository.*
import com.wowo.wowo.shared.domain.*
import com.wowo.wowo.shared.valueobject.*
import org.slf4j.*
import org.springframework.stereotype.*
import org.springframework.transaction.annotation.*

/**
 * Use Case: Create a new wallet
 */
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
class CreateWalletUseCase(
    private val walletRepository: WalletRepository, private val eventPublisher: DomainEventPublisher
) {
    private val logger = LoggerFactory.getLogger(CreateWalletUseCase::class.java)

    fun execute(command: CreateWalletCommand): WalletDTO {
        val currency = Currency.valueOf(command.currency)

        val wallet = Wallet.create(command.ownerId, command.ownerType, currency)

        logger.debug(
            "Creating wallet for ownerId={}, ownerType={} with currency={}",
            command.ownerId,
            command.ownerType,
            command.currency
        )
        val savedWallet = walletRepository.save(wallet)
        logger.debug("Wallet saved with id={}", savedWallet.id)


        val events = wallet.getDomainEvents()
        logger.debug("Publishing ${events.size} domain events for wallet id=${wallet.id}")
        eventPublisher.publish(events)
        wallet.clearDomainEvents()

        return WalletDTO.fromDomain(savedWallet)
    }
}
