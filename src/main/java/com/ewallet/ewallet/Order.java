package com.ewallet.ewallet;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Table;

@AllArgsConstructor
@Data
@Table("order")
public class Order {

    String id;
    String partnerId;
    double money;
    Status status;



    enum Status {
        PENDING,
        COMPLETED,
        CANCELLED
    }


}



