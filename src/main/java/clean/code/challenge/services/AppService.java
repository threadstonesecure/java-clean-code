package clean.code.challenge.services;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityNotFoundException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import clean.code.challenge.dto.TransactionDto;
import clean.code.challenge.dto.TransactionType;
import clean.code.challenge.dto.UserDto;
import clean.code.challenge.exceptions.AppException;
import clean.code.challenge.exceptions.ValidationException;
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

	public UserDto getUser(Integer userId) {
		return userMapper.toDto(getUserBasedOnUserId(userId));

	}

	private User getUserBasedOnUserId(int userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new EntityNotFoundException("Record not found with userId " + userId));
		return user;
	}

	public TransactionDto performTransaction(TransactionDto transactionDto) {
		validateRequest(transactionDto);
		switch (TransactionType.valueOf(transactionDto.getType())) {
		case DEBIT:
			handleTransaction(transactionDto, TransactionType.DEBIT);
			break;
		case CREDIT:
			handleTransaction(transactionDto, TransactionType.CREDIT);
			break;
		}

		return null;
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

	private void validateAccount(List<Account> accounts, TransactionDto transactionDto) {
		boolean isAccountActive = accounts.stream()
				.anyMatch(account -> account.getId().equals(transactionDto.getAccountId())
						&& (Status.ACTIVE.equals(account.getStatus())));
		if (!isAccountActive) {
			log.info("Account Id is not valid or in-active");
			throw new ValidationException("Account Id doesn't exists or in-active");
		}
	}

	private TransactionDto handleTransaction(TransactionDto transactionDto, TransactionType transactionType) {
		try {
			User user = getUserBasedOnUserId(transactionDto.getUser().getId());
			if (CollectionUtils.isNotEmpty(user.getAccounts())) {
				validateAccount(user.getAccounts(), transactionDto);
				user.getAccounts().stream().filter(account -> account.getId().equals(transactionDto.getAccountId()))
						.forEach(userAccount -> updateBalanceAmount(transactionDto, transactionType, userAccount));
			}

			return performTransaction(transactionDto, user, transactionType);
		} catch (final Exception exception) {
			throw new AppException(exception.getMessage(), exception);
		}
	}

	private void updateBalanceAmount(TransactionDto transactionDto, TransactionType transactionType,
			Account userAccount) {
		BigDecimal balanceAmount = BigDecimal.ZERO;
		if (TransactionType.DEBIT.equals(transactionType)) {
			if (userAccount.getBalance().compareTo(transactionDto.getAmount()) < 0) {
				throw new ValidationException("Amount is not sufficient to perform the transaction");
			}
			balanceAmount = userAccount.getBalance().subtract(transactionDto.getAmount());
		} else if (TransactionType.CREDIT.equals(transactionType)) {
			balanceAmount = userAccount.getBalance().add(transactionDto.getAmount());
		}
		userAccount.setBalance(balanceAmount);
	}

	private TransactionDto performTransaction(TransactionDto transactionDto, final User user,
			TransactionType transactionType) {
		// Add Transaction
		final Transaction transaction = saveTransaction(transactionDto, user, transactionType);
		return updateTransaction(user, transaction, transactionType);
	}

	private TransactionDto updateTransaction(final User user, final Transaction transaction,
			TransactionType transactionType) {
		// Add Note
		final Note note = new Note();
		note.setDescription(transactionType.transactionSuccessMsg());
		note.setUser(user);

		// Save
		user.getTransactions().add(transaction);
		user.getNotes().add(note);
		final User savedUser = userRepository.save(user);
		log.info(transactionType.transactionLogMsg());
		final UserDto userDto = userMapper.toDto(savedUser);
		final List<TransactionDto> transactions = userDto.getTransactions();
		return transactions.get(transactions.size() - 1);
	}

	private Transaction saveTransaction(TransactionDto transactionDto, final User user,
			TransactionType transactionType) {
		final Transaction transaction = new Transaction();
		transaction.setAccountId(transactionDto.getAccountId());
		transaction.setAmount(transactionDto.getAmount());
		transaction.setDescription(transactionType.transactionSuccessMsg());
		transaction.setType(transactionType.toString());
		transaction.setUser(user);
		return transaction;
	}

}
