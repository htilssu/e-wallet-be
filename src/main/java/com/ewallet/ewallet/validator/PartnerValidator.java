package com.ewallet.ewallet.validator;

import com.ewallet.ewallet.partner.Partner;

public class PartnerValidator {

    public static boolean validatePartner(Partner partner) {
        if (partner.getName() == null || partner.getName().isEmpty()) {
            return false;
        }

        if (partner.getDescription() == null || partner.getDescription().isEmpty()) {
            return false;
        }

        if (partner.getEmail() == null || partner.getEmail().isEmpty()) {
            return false;
        }

        if (partner.getPassword() == null || partner.getPassword().isEmpty()) {
            return false;
        }

        if (partner.getApiBaseUrl() == null || partner.getApiBaseUrl().isEmpty()) {
            return false;
        }

        return true;
    }
}
