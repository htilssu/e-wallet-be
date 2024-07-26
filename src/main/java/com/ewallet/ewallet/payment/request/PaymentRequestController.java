package com.ewallet.ewallet.payment.request;

import com.ewallet.ewallet.dto.mapper.PaymentRequestMapperImpl;
import com.ewallet.ewallet.models.PaymentRequest;
import com.ewallet.ewallet.util.ObjectUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/v?/prequest", produces = "application/json; charset=UTF-8")
public class PaymentRequestController {

    private final PaymentRequestMapperImpl paymentRequestMapperImpl;
    PaymentRequestRepository paymentRequestRepository;

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable String id, Authentication authentication) {
        Optional<PaymentRequest> orderOptional = paymentRequestRepository.findById(id);

        return ResponseEntity.ok(orderOptional.map(paymentRequestMapperImpl::toDto).orElse(null));
    }

    @PostMapping()
    public ResponseEntity<?> createPayment(@RequestBody PaymentRequestData paymentRequest,
            Authentication authentication,
            HttpServletRequest request) {
        if (paymentRequest == null || paymentRequest.getMoney() <= 0 || paymentRequest.getVoucherId() == null || paymentRequest.getVoucherName() == null) {
            return ResponseEntity.badRequest().body(null);
        }

        PaymentRequest newPaymentRequest = paymentRequestMapperImpl.toEntity(paymentRequest);

        PaymentRequest savedEntity = paymentRequestRepository.save(newPaymentRequest);
        return ResponseEntity.ok(
                ObjectUtil.mergeObjects(paymentRequestMapperImpl.toDto(savedEntity),
                        ObjectUtil.wrapObject("checkUrl",
                                request.getRequestURL().toString() + "/" + savedEntity.getId())));
    }
}
