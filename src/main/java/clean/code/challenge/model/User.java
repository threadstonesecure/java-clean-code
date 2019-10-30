package clean.code.challenge.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

import org.springframework.format.annotation.DateTimeFormat;

import clean.code.challenge.util.Status;

@Getter
@Setter
@Entity
@Table(name = "User")
public class User {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "FIRST_NAME")
	private String firstName;

	@Column(name = "LAST_NAME")
	private String lastName;

	@Column(name = "EMAIL")
	private String email;

	@Column(name = "STATUS")
	@Enumerated(value = EnumType.STRING)
	private Status status;

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

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
	private List<Address> addresses = new ArrayList<>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
	private List<Account> accounts = new ArrayList<>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
	private List<Note> notes = new ArrayList<>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
	private List<Transaction> transactions = new ArrayList<>();

	@Override
	public boolean equals(final Object that) {
		if (this == that) {
			return true;
		}
		return (that instanceof User) && Objects.equals(id, ((User) that).id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

}
