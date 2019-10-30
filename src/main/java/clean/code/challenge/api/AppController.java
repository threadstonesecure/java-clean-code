package clean.code.challenge.api;

import java.util.Arrays;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import clean.code.challenge.dto.TransactionDto;
import clean.code.challenge.dto.UserDto;
import clean.code.challenge.exceptions.AppException;
import clean.code.challenge.exceptions.ValidationException;
import clean.code.challenge.mapper.UserMapper;
import clean.code.challenge.model.User;
import clean.code.challenge.repositories.UserRepository;
import clean.code.challenge.services.AppService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class AppController {

	@Resource
	private UserRepository userRepository;

	@Resource
	private UserMapper userMapper;

	@Resource
	private AppService appService;

	@GetMapping("/")
	public ResponseEntity<String> index() {
		return new ResponseEntity<>("welcome to Clean Code Challenge", HttpStatus.OK);
	}

	@GetMapping("/users/{userId}")
	public ResponseEntity<UserDto> getUser(@NotNull @PathVariable("userId") final Integer userId) {
		final User user = userRepository.getUserWithAllRelations(userId);
		if (user != null) {
			final UserDto userDto = userMapper.toDto(user);
			return new ResponseEntity<>(userDto, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@PostMapping("/users/{userId}/transactions")
	public ResponseEntity<TransactionDto> transactions(@PathVariable Integer userId,
			@RequestBody TransactionDto transactionDto) {
		try {
			final TransactionDto transaction = appService.transactions(userId, transactionDto);
			if (transaction != null) {
				// return new ResponseEntity<>(HttpStatus.OK);
				return new ResponseEntity<>(transaction, HttpStatus.CREATED);
			}
		} catch (final ValidationException validationException) {
			log.error(validationException.getMessage(), validationException);
			transactionDto.setMessages(Arrays.asList(validationException.getMessage()));
			return new ResponseEntity<>(transactionDto, HttpStatus.BAD_REQUEST);
		} catch (final AppException appException) {
			log.error(appException.getMessage(), appException);
			transactionDto.setMessages(Arrays.asList(appException.getMessage()));
			return new ResponseEntity<>(transactionDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
