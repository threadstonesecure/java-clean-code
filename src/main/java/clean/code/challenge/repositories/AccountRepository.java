package clean.code.challenge.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import clean.code.challenge.model.Account;

public interface AccountRepository extends JpaRepository<Account, Integer> {

}
