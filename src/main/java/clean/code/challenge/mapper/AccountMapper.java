package clean.code.challenge.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import clean.code.challenge.dto.AccountDto;
import clean.code.challenge.model.Account;

@Mapper(componentModel = "spring")
public interface AccountMapper {

	Account toEntity(AccountDto addressDto);

	@Mappings({
		@Mapping(target = "createdDate", ignore = true),
		@Mapping(target = "modifiedDate", ignore = true),
		@Mapping(target = "user", ignore = true)
	})
	AccountDto toDto(Account account);
}
