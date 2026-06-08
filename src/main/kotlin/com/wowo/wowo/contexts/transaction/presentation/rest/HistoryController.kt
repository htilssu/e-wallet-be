package com.wowo.wowo.contexts.transaction.presentation.rest

import com.wowo.wowo.contexts.payment.domain.entity.*
import com.wowo.wowo.contexts.transaction.application.dto.*
import com.wowo.wowo.contexts.transaction.application.usecase.*
import com.wowo.wowo.contexts.transaction.domain.acl.*
import com.wowo.wowo.contexts.transaction.domain.repository.*
import com.wowo.wowo.contexts.transaction.domain.valueobject.*
import com.wowo.wowo.shared.application.*
import com.wowo.wowo.shared.domain.*
import com.wowo.wowo.shared.exception.*
import com.wowo.wowo.shared.infrastructure.security.*
import com.wowo.wowo.shared.infrastructure.security.annotations.*
import org.springframework.format.annotation.*
import org.springframework.http.*
import org.springframework.security.access.*
import org.springframework.web.bind.annotation.*
import java.time.*

@RestController
@RequestMapping("/history")
class HistoryController(
    private val getTransactionHistoryUseCase: GetTransactionHistoryUseCase,
    private val walletACL: WalletACL,
    private val groupFundACL: GroupFundACL,
    private val securityUtils: SecurityUtils
) {

    @GetMapping
    @RequireAuthenticated
    fun getTransactionHistory(
        @RequestParam walletId: String,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) startDate: Instant?,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) endDate: Instant?,
        @RequestParam(required = false) type: TransactionType?,
        @ModelAttribute paging: PaginationDto?
    ): ResponseEntity<PagedResult<TransactionDTO>> {
        val currentUserId = securityUtils.getCurrentUserId() ?: throw AccessDeniedException("User is not authenticated")

        val wallet =
            walletACL.getWallet(walletId) as? Wallet ?: throw EntityNotFoundException("Wallet not found: $walletId")

        val hasAccess = when (wallet.ownerType) {
            OwnerType.USER -> wallet.ownerId == currentUserId
            OwnerType.FUND_GROUP -> groupFundACL.canAccessFundWallet(wallet.ownerId, currentUserId)
            else -> false
        }

        if (!hasAccess) {
            throw AccessDeniedException("Access denied for wallet: $walletId")
        }

        val criteria = TransactionSearchCriteria(
            walletId = walletId,
            startDate = startDate,
            endDate = endDate,
            type = type,
            page = paging?.page ?: 1,
            size = paging?.size ?: 20
        )

        val result = getTransactionHistoryUseCase.execute(criteria)
        return ResponseEntity.ok(result)
    }
}
