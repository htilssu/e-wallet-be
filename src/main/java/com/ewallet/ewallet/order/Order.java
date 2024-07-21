package com.ewallet.ewallet.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@AllArgsConstructor
@Data
@Table("\"order\"")
public class Order {
    @Id
    String id;
    String partnerId;
    double money;

}



