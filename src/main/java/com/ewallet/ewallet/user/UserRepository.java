package com.ewallet.ewallet.user;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<User, String> {

    Mono<Boolean> existsUserByEmail(String email);
    Mono<User> findByEmail(String email);
    Mono<User> findByUserName(String userName);
    Mono<User> findByPhoneNumber(String phoneNumber);
    @Query("update user set first_name = = :#{user.firstName}, last_name" +
            " = :#{user.lastName}, email = :#{user.email}, password = :#{user.password}," +
            " phone_number = :#{user.phoneNumber}, address = :#{user.address}," +
            " city = :#{user.city}, country = :#{user.country}, dob = :#{user.dob}")
    Mono<User> updateUserById(User user);
}
