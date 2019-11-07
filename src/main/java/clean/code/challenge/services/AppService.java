package clean.code.challenge.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;
import javax.persistence.EntityNotFoundException;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import clean.code.challenge.dto.TransactionDto;
import clean.code.challenge.dto.TransactionType;
import clean.code.challenge.dto.UserDto;
import clean.code.challenge.exceptions.AppException;
import clean.code.challenge.exceptions.ValidationException;
import clean.code.challenge.mapper.TransactionMapper;
import clean.code.challenge.mapper.UserMapper;
import clean.code.challenge.model.Account;
import clean.code.challenge.model.Note;
import clean.code.challenge.model.Transaction;
import clean.code.challenge.model.User;
import clean.code.challenge.repositories.UserRepository;
import clean.code.challenge.util.Status;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AppService {

	@Resource
	public UserRepository userRepository;

	@Resource
	public UserMapper userMapper;
	@Resource
	public TransactionMapper transactionMapper;

	public UserDto getUser(Integer userId) {
		return userMapper.toDto(getUserBasedOnUserId(userId));

	}

	private User getUserBasedOnUserId(int userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new EntityNotFoundException("Record not found with userId " + userId));
		return user;
	}

	public TransactionDto performUserTransaction(TransactionDto transactionDto) {
		validateRequest(transactionDto);
		User user = getUserBasedOnUserId(transactionDto.getUser().getId());
		Account userAccount = validateAndGetUserAccount(user.getAccounts(), transactionDto);
		return handleTransaction(user, userAccount, transactionDto);
	}

	private void validateRequest(TransactionDto transactionDto) {
		if (ObjectUtils.isEmpty(transactionDto) || ObjectUtils.isEmpty(transactionDto.getUser())
				|| ObjectUtils.isEmpty(transactionDto.getUser().getId())
				|| ObjectUtils.isEmpty(transactionDto.getAccountId())
				|| (ObjectUtils.isEmpty(transactionDto.getType())
						|| (!(transactionDto.getType().equalsIgnoreCase(TransactionType.DEBIT.getTransactionType()))
								&& !(transactionDto.getType()
										.equalsIgnoreCase(TransactionType.CREDIT.getTransactionType()))))) {
			throw new ValidationException("Invalid request");
		}
	}

	private Account validateAndGetUserAccount(List<Account> accounts, TransactionDto transactionDto) {
		Account activeUserAccount = null;
		if (CollectionUtils.isNotEmpty(accounts)) {
			Optional<Account> userAccount = accounts.stream()
					.filter(account -> account.getId().equals(transactionDto.getAccountId())
							&& (Status.ACTIVE.equals(account.getStatus())))
					.findFirst();
			activeUserAccount = userAccount
					.orElseThrow(() -> new ValidationException("Account Id doesn't exists or in-active"));

		} else {
			throw new AppException("No account is linked to the user");
		}
		return activeUserAccount;
	}

	private TransactionDto handleTransaction(User user, Account userAccount, TransactionDto transactionDto) {
		TransactionType transactionType = TransactionType.valueOf(transactionDto.getType());
		updateBalanceAmount(userAccount, transactionDto, transactionType);
		return commmitTransaction(transactionDto, user, transactionType);
	}

	private void updateBalanceAmount(Account userAccount, TransactionDto transactionDto,
			TransactionType transactionType) {
		BigDecimal balanceAmount = userAccount.getBalance();
		if (TransactionType.DEBIT.equals(transactionType)) {
			if (balanceAmount.compareTo(transactionDto.getAmount()) < 0) {
				throw new ValidationException("Amount is not sufficient to perform the transaction");
			}
			balanceAmount = balanceAmount.subtract(transactionDto.getAmount());
		} else if (TransactionType.CREDIT.equals(transactionType)) {
			balanceAmount = balanceAmount.add(transactionDto.getAmount());
		}
		userAccount.setBalance(balanceAmount);
	}

	private TransactionDto commmitTransaction(TransactionDto transactionDto, final User user,
			TransactionType transactionType) {
		final Transaction transaction = new Transaction();
		transaction.setAccountId(transactionDto.getAccountId());
		transaction.setAmount(transactionDto.getAmount());
		transaction.setDescription(transactionType.transactionSuccessMsg());
		transaction.setType(transactionType.toString());
		transaction.setUser(user);
		final Note note = new Note();
		note.setDescription(transactionType.transactionSuccessMsg());
		note.setUser(user);
		user.getTransactions().add(transaction);
		user.getNotes().add(note);
		userRepository.save(user);
		log.info(transactionType.transactionLogMsg());
		return transactionMapper.toDto(transaction);
	}

}
