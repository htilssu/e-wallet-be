package com.ewallet.ewallet.card;

import com.ewallet.ewallet.atm.AtmController;
import com.ewallet.ewallet.dto.response.AtmCardDto;
import com.ewallet.ewallet.dto.response.ResponseMessage;
import com.ewallet.ewallet.models.Atm;
import com.ewallet.ewallet.models.AtmCard;
import com.ewallet.ewallet.models.AtmCardMapperImpl;
import com.ewallet.ewallet.models.User;
import com.ewallet.ewallet.repository.AtmCardRepository;
import com.ewallet.ewallet.repository.AtmRepository;
import com.ewallet.ewallet.user.UserRepository;
import com.ewallet.ewallet.util.ObjectUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/v1/card", produces = "application/json; charset=UTF-8")
public class CardController {

    private final AtmCardMapperImpl atmCardMapperImpl;
    private final AtmCardRepository atmCardRepository;
    private final UserRepository userRepository;
    private final AtmRepository atmRepository;


    @PostMapping
    public ResponseEntity<?> createCard(@RequestBody AtmCardDto atmCardDto,
            Authentication authentication) {
        if (atmCardDto == null) {
            return ResponseEntity.badRequest().body(new ResponseMessage("Dữ liệu không hợp lệ"));
        }
        String userId = (String) authentication.getPrincipal();
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.ok(new ResponseMessage("Người dùng không tồn tại"));
        }
        Optional<Atm> atm = atmRepository.findById(atmCardDto.getAtmId());
        if (atm.isEmpty()) {
            return ResponseEntity.badRequest().body(new ResponseMessage("ATM không tồn tại"));
        }

        final AtmCard atmCard = atmCardMapperImpl.toEntity(atmCardDto);
        atmCard.setOwner(user);
        atmCard.setAtm(atm.get());


        try {
            atmCardRepository.save(atmCard);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new ResponseMessage("Thẻ đã tồn tại"));
        }

        return ResponseEntity.ok(ObjectUtil.mergeObjects(
                ObjectUtil.wrapObject("card", atmCardMapperImpl.toDto(atmCard)),
                new ResponseMessage("Tạo thẻ thành công")));

    }

}
