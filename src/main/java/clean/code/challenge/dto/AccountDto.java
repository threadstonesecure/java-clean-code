package clean.code.challenge.dto;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import clean.code.challenge.util.Status;

@Getter
@Setter
public class AccountDto {

	private Integer id;

	private String type;

	private BigDecimal balance;

	private Status status;

	private Date createdDate;

	private Date modifiedDate;

	private UserDto user;

}
