package com.ewallet.ewallet.repository;

import com.ewallet.ewallet.models.Transaction;
import org.springframework.data.repository.CrudRepository;

public interface TransactionRepository extends CrudRepository<Transaction, String> {


}
