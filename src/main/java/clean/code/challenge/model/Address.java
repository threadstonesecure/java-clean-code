package clean.code.challenge.model;

import java.util.Date;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Getter
@Setter
@Entity
@Table(name = "Address")
public class Address {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "STREET_NAME")
	private String streetName;

	@Column(name = "CITY")
	private String city;

	@Column(name = "STATE")
	private String state;

	@Column(name = "PIN_CODE")
	private Integer pinCode;

	@Setter(lombok.AccessLevel.NONE)
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	@Column(name = "CREATED_DATE", insertable = true, updatable = false)
	private final Date createdDate = new Date();

	@Setter(lombok.AccessLevel.NONE)
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	@Column(name = "MODIFIED_DATE", insertable = true, updatable = false)
	private final Date modifiedDate = new Date();

	@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name="USER_ID")
	private User user;

	@Override
	public boolean equals(final Object that) {
		if (this == that) {
			return true;
		}
		return (that instanceof Address) && Objects.equals(id, ((Address) that).id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}


}
