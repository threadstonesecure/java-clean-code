package clean.code.challenge.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import clean.code.challenge.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {


}
