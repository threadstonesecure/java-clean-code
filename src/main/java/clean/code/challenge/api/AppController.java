package clean.code.challenge.api;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import clean.code.challenge.dto.TransactionDto;
import clean.code.challenge.dto.UserDto;
import clean.code.challenge.services.AppService;
import clean.code.challenge.util.ChallengeConstants;

@RestController
@Validated
public class AppController {

	@Resource
	private AppService appService;

	@GetMapping(ChallengeConstants.ROOT_ENDPOINT)
	public ResponseEntity<String> index() {
		return new ResponseEntity<>("Welcome to Clean Code Challenge", HttpStatus.OK);
	}

	@GetMapping(ChallengeConstants.USER_ENDPOINT)
	public ResponseEntity<UserDto> getUser(@NotNull @Min(1) @PathVariable("userId") int userId) {
		UserDto userDto = appService.getUser(userId);
		return new ResponseEntity<>(userDto, HttpStatus.OK);
	}

	@PostMapping(ChallengeConstants.USER_TRANSACTIONS_ENDPOINT)
	public ResponseEntity<TransactionDto> userTransaction(@Valid @RequestBody TransactionDto transactionDto) {
		TransactionDto transaction = appService.performUserTransaction(transactionDto);
		return new ResponseEntity<>(transaction, HttpStatus.CREATED);
	}

}
