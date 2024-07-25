package com.ewallet.ewallet.service;

import com.ewallet.ewallet.dto.mapper.WalletMapper;
import com.ewallet.ewallet.dto.response.WalletResponse;
import com.ewallet.ewallet.partner.PartnerRepository;
import com.ewallet.ewallet.user.UserRepository;
import com.ewallet.ewallet.wallet.WalletRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class WalletService {

    private final WalletMapper walletMapper;
    private final WalletRepository walletRepository;
    private final UserRepository userRepository;
    private final PartnerRepository partnerRepository;

    public WalletResponse getWallet(int id) {
        var wallet = walletRepository.findById(String.valueOf(id))
                .orElseThrow(() -> new RuntimeException("Không tìm thấy ví"));

        var walletResponse = walletMapper.toResponse(wallet);

        if ("user".equals(wallet.getOwnerType())) {
            var user = userRepository.findById(wallet.getOwnerId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
            walletResponse.setUser(user);
        } else {
            var partner = partnerRepository.findById(wallet.getOwnerId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy đối tác"));
            walletResponse.setPartner(partner);
        }

        return walletResponse;
    }
}
