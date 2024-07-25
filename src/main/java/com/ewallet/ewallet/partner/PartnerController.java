package com.ewallet.ewallet.partner;

import com.ewallet.ewallet.model.response.ResponseMessage;
import com.ewallet.ewallet.models.Partner;
import com.ewallet.ewallet.util.ApiKeyUtil;
import com.ewallet.ewallet.util.JwtUtil;
import com.ewallet.ewallet.util.ObjectUtil;
import com.ewallet.ewallet.validator.EmailValidator;
import com.ewallet.ewallet.validator.PartnerValidator;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Data
@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/partner", produces = "application/json; charset=UTF-8")
public class PartnerController {

    private final PartnerRepository partnerRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> createPartner(@RequestBody Partner newPartner) {
        if (newPartner == null) {
            return ResponseEntity.badRequest()
                    .body(new ResponseMessage("Vui lòng kiểm tra lại thông tin"));
        }

        if (!PartnerValidator.validatePartner(newPartner) || newPartner.getApiKey() != null ||
                newPartner.getBalance() > 0 || !EmailValidator.isValid(newPartner.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(new ResponseMessage("Vui lòng kiểm tra lại thông tin"));
        }

        newPartner.setPassword(bCryptPasswordEncoder.encode(newPartner.getPassword()));
        newPartner.setApiKey(ApiKeyUtil.generateApiKey());

        try {
            Partner savedEntity = partnerRepository.save(newPartner);
            String token = JwtUtil.generateToken(newPartner);
            return ResponseEntity.ok()
                    .header("Set-Cookie", "token=" + token + "; Path=/; SameSite=None; Secure; Max-Age=9999999;")
                    .body(ObjectUtil.mergeObjects(
                            ObjectUtil.wrapObject("partner", savedEntity),
                            new ResponseMessage("Đăng ký thành công"),
                            ObjectUtil.wrapObject("token", token)));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new ResponseMessage("Đăng ký thất bại"));
        }
    }

    @GetMapping
    public ResponseEntity<Partner> getPartner(Authentication authentication) {
        Partner partner = (Partner) authentication.getPrincipal();
        return ResponseEntity.ok(partner);
    }
}
