package clean.code.challenge.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionDto {

	private Integer id;

	private String type;
	@JsonIgnore
	private TransactionType transactionType;
	private BigDecimal amount;

	private String description;

	private Integer accountId;

	private Date createdDate;

	private Date modifiedDate;

	@NotNull
	private UserDto user;

	@Override
	public boolean equals(final Object that) {
		if (this == that) {
			return true;
		}
		return (that instanceof TransactionDto) && Objects.equals(id, ((TransactionDto) that).id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

}
