package clean.code.challenge.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import clean.code.challenge.model.Address;

public interface AddressRepository extends JpaRepository<Address, Integer> {

}
