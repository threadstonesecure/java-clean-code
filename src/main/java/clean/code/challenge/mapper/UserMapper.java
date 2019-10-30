package clean.code.challenge.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import clean.code.challenge.dto.UserDto;
import clean.code.challenge.model.User;

@Mapper(componentModel = "spring", uses = {AddressMapper.class, AccountMapper.class,
		NoteMapper.class, TransactionMapper.class})
public interface UserMapper {

	User toEntity(UserDto userDto);

	@Mappings({
		@Mapping(target = "createdDate", ignore = true),
		@Mapping(target = "modifiedDate", ignore = true)
	})
	UserDto toDto(User user);

}
