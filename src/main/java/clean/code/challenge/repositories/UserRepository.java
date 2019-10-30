package clean.code.challenge.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import clean.code.challenge.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	@Query("select user from User user where user.id = :id")
	User getUserWithAllRelations(@Param("id") Integer id);

	@Query("select user from User user JOIN FETCH user.accounts where user.id = :id")
	User getUserWithAccounts(@Param("id") Integer id);

}
