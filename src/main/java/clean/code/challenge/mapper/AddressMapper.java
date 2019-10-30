package clean.code.challenge.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import clean.code.challenge.dto.AddressDto;
import clean.code.challenge.model.Address;

@Mapper(componentModel = "spring")
public interface AddressMapper {

	Address toEntity(AddressDto addressDto);

	@Mappings({
		@Mapping(target = "createdDate", ignore = true),
		@Mapping(target = "modifiedDate", ignore = true),
		@Mapping(target = "user", ignore = true)
	})
	AddressDto toDto(Address address);
}
