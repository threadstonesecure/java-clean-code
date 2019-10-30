package clean.code.challenge.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressDto {

	private Integer id;

	private String streetName;

	private String city;

	private String state;

	private Integer pinCode;

	private Date createdDate;

	private Date modifiedDate;

	private UserDto user;


}
