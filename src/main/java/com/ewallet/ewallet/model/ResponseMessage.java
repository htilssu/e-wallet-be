package com.ewallet.ewallet.model;

import com.ewallet.ewallet.order.Order;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ResponseMessage {

    @JsonIgnore
    public static final ResponseMessage NOT_FOUND = new ResponseMessage("Not Found");
    String message;
}
