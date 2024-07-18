package com.ewallet.ewallet.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class ObjectUtil {

    static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.registerModule(new JavaTimeModule());
    }

    public static ObjectNode mergeObjects(Object obj1, Object obj2) {


        ObjectNode result = objectMapper.createObjectNode();
        result.setAll((ObjectNode) objectMapper.valueToTree(obj1));
        result.setAll((ObjectNode) objectMapper.valueToTree(obj2));

        return result;
    }

    public static ObjectNode wrapObject(String key, Object value) {
        ObjectNode wrapper = objectMapper.createObjectNode();
        wrapper.set(key, objectMapper.valueToTree(value));
        return wrapper;
    }
}
