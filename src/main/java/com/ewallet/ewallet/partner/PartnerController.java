package com.ewallet.ewallet.partner;

import com.ewallet.ewallet.model.ResponseMessage;
import com.ewallet.ewallet.util.ApiKeyUtil;
import com.ewallet.ewallet.validator.PartnerValidator;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Data
@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/partner", produces = "application/json; charset=UTF-8")
public class PartnerController {

    PartnerRepository partnerRepository;

    @PostMapping("/register")
    public Mono<ResponseEntity<ResponseMessage>> createPartner(@RequestBody Partner newPartner, BCryptPasswordEncoder bCryptPasswordEncoder) {
        if (newPartner == null) {
            return Mono.just(ResponseEntity.badRequest()
                                     .body(new ResponseMessage(
                                             "Vui lòng kiểm tra lại thông tin")));
        }

        if (!PartnerValidator.validatePartner(newPartner) || newPartner.getApiKey() != null || newPartner.getBalance() > 0) {
            return Mono.just(ResponseEntity.badRequest()
                                     .body(new ResponseMessage(
                                             "Vui lòng kiểm tra lại thông tin")));
        }

        newPartner.setPassword(bCryptPasswordEncoder.encode(newPartner.getPassword()));
        newPartner.setApiKey(ApiKeyUtil.generateApiKey());

        return partnerRepository.save(newPartner)
                .flatMap(savedEntity -> Mono.just(ResponseEntity.ok(new ResponseMessage(
                        "Đăng ký thành công"))))
                .onErrorResume(error -> Mono.just(ResponseEntity.ok()
                                                          .body(new ResponseMessage(
                                                                  "Đăng ký thất bại, Email đã tồn tại"))));

    }
}
