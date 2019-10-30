package clean.code.challenge.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import clean.code.challenge.dto.TransactionDto;
import clean.code.challenge.model.Transaction;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

	Transaction toEntity(TransactionDto transactionDto);

	@Mappings({
		@Mapping(target = "createdDate", ignore = true),
		@Mapping(target = "modifiedDate", ignore = true),
		@Mapping(target = "user", ignore = true)
	})
	TransactionDto toDto(Transaction note);
}
