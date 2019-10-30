package clean.code.challenge.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import clean.code.challenge.dto.NoteDto;
import clean.code.challenge.model.Note;

@Mapper(componentModel = "spring")
public interface NoteMapper {

	Note toEntity(NoteDto addressDto);

	@Mappings({
		@Mapping(target = "createdDate", ignore = true),
		@Mapping(target = "modifiedDate", ignore = true),
		@Mapping(target = "user", ignore = true)
	})
	NoteDto toDto(Note note);
}
