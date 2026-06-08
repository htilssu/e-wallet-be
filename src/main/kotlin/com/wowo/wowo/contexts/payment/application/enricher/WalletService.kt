package com.wowo.wowo.contexts.payment.application.enricher

import com.wowo.wowo.contexts.user.domain.repository.UserRepository
import com.wowo.wowo.contexts.payment.domain.repository.WalletRepository
import org.springframework.stereotype.Service

@Service
class WalletService(val userRepository: UserRepository, val walletRepository: WalletRepository) {

}