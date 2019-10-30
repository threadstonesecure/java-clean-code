package clean.code.challenge.dto;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import clean.code.challenge.util.Status;

@Getter
@Setter
public class UserDto {

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
