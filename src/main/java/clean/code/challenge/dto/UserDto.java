package clean.code.challenge.dto;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import clean.code.challenge.util.Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class UserDto {
	 @NotBlank(message = "UserId is mandatory")
	 @Min(1)
	private Integer id;

	private String firstName;

	private String lastName;

	private String email;

	private Status status;

	private Date createdDate;

	private Date modifiedDate;

	private List<AddressDto> addresses;

	private List<AccountDto> accounts;

	private List<NoteDto> notes;

	private List<TransactionDto> transactions;

}
