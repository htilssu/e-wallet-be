package com.wowo.wowo.contexts.payment.domain.repository

import com.wowo.wowo.contexts.payment.domain.entity.*
import com.wowo.wowo.contexts.payment.domain.valueobject.*
import com.wowo.wowo.shared.domain.*
import com.wowo.wowo.shared.valueobject.*

/**
 * Repository interface for Wallet aggregate (Domain layer)
 */
interface WalletRepository {
    fun save(wallet: Wallet): Wallet
    fun findById(id: WalletId): Wallet?
    fun findByOwnerIdAndOwnerType(ownerId: String, ownerType: OwnerType): List<Wallet>
    fun existsByOwnerIdAndOwnerType(ownerId: String, ownerType: OwnerType): Boolean
    fun existsByOwnerIdAndOwnerTypeAndCurrency(ownerId: String, ownerType: OwnerType, currency: Currency): Boolean
    fun delete(wallet: Wallet)
}

