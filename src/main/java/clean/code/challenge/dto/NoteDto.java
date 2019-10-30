package clean.code.challenge.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoteDto {

	private Integer id;

	private String description;

	private Date createdDate;

	private Date modifiedDate;

	private UserDto user;
}
