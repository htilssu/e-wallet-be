package com.ewallet.ewallet.partner;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Table;

@AllArgsConstructor
@Data
@Table("partner")
public class Partner {

    String id;
    String name;
    String description;
    String serviceId;
    String apiKey;
    String balance;
    String created;
}
