package clean.code.challenge.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import clean.code.challenge.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

}
