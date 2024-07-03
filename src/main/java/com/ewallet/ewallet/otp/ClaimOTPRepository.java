package com.ewallet.ewallet.otp;

import lombok.AllArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;

import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
public class ClaimOTPRepository {

    DynamoDbAsyncTable<ClaimOTPModel> claimTable;

    public CompletableFuture<Void> save(ClaimOTPModel claimOTPModel) {
        return claimTable.putItem(claimOTPModel);
    }

    public CompletableFuture<ClaimOTPModel> deleteByUserId(String id) {
        Key key = Key.builder().partitionValue(id).build();
        return claimTable.deleteItem(key);
    }

    public CompletableFuture<ClaimOTPModel> findByUserId(String userId) {
        Key key = Key.builder().partitionValue(userId).build();
        return claimTable.getItem(key);
    }
}
