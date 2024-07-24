package com.ewallet.ewallet.payment.request;

import com.ewallet.ewallet.util.ObjectUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/v?/prequest", produces = "application/json; charset=UTF-8")
public class PaymentRequestController {

    PaymentRequestRepository paymentRequestRepository;

    @GetMapping("/{id}")
    public Mono<?> getOrderById(@PathVariable String id,
            Authentication authentication) {
        final Mono<PaymentRequest> orderMono = paymentRequestRepository.findById(id);

        return orderMono.map(
                ResponseEntity::ok
        );
    }

    @PostMapping()
    public Mono<?> createPayment(@RequestBody PaymentRequestData paymentRequest,
            Authentication authentication, HttpServletRequest request) {
        if (paymentRequest == null) {
            return Mono.just(ResponseEntity.badRequest().body(null));
        }

        if (paymentRequest.getMoney() <= 0) {
            return Mono.just(ResponseEntity.badRequest().body(null));
        }
        if (paymentRequest.getVoucherId() == null) {
            return Mono.just(ResponseEntity.badRequest().body(null));
        }
        if (paymentRequest.getVoucherName() == null) {
            return Mono.just(ResponseEntity.badRequest().body(null));
        }


        return paymentRequestRepository.save(PaymentRequest.builder().money(
                                paymentRequest.getMoney())
                        .voucherId(paymentRequest.getVoucherId())
                        .voucherName(paymentRequest.getVoucherName())
                        .voucherCode(paymentRequest.getVoucherCode())
                        .voucherDiscount(paymentRequest.getVoucherDiscount())
                        .status("PENDING")
                        .build())
                .map(savedEntity -> ResponseEntity.ok(ObjectUtil.mergeObjects(savedEntity,
                        ObjectUtil.wrapObject("checkUrl",
                                request.getRequestURL().toString() + "/" + savedEntity.getId()))));
    }
}
