package com.ewallet.ewallet.partner;

import com.ewallet.ewallet.models.Partner;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface PartnerRepository extends CrudRepository<Partner, String> {

    Optional<Partner> findPartnerByApiKey(String apiKey);
}
