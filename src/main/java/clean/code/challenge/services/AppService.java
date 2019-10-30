package clean.code.challenge.services;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import clean.code.challenge.dto.TransactionDto;
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

	public TransactionDto transactions(Integer userId, TransactionDto transactionDto) {
		//Validate user
		if (((userId != null)
				&& (transactionDto.getUser() != null)
				&& (transactionDto.getUser().getId() != null))
					&& (userId.equals(transactionDto.getUser().getId()))) {
			try {
				final User user = userRepository.getUserWithAccounts(userId);
				if (user == null) {
					throw new ValidationException("User id's doesn't exists");
				}
			} catch (final Exception exception) {
				throw new AppException(exception.getMessage(), exception);
			}
		} else {
			log.info("User id's doesn't match");
			throw new ValidationException("User id's doesn't match");
		}

		//Validate Account
		if (transactionDto.getAccountId() != null) {
			try {
				final User user = userRepository.getUserWithAccounts(userId);
				boolean accountValidBoolean = false;
				for (final Account account: user.getAccounts() ) {
					if (account.getId().equals(transactionDto.getAccountId())) {
						if (account.getStatus().equals(Status.ACTIVE)) {
							accountValidBoolean = true;
							break;
						}
					}
				}
				if (!accountValidBoolean) {
					log.info("Account Id is not valid");
					throw new ValidationException("Account Id doesn't exists");
				}
			} catch (final Exception exception) {
				throw new AppException(exception.getMessage(), exception);
			}
		} else {
			log.info("Account Id is null");
			throw new ValidationException("Account Id is null");
		}

		//Validate Transaction Type
		if ((transactionDto.getType() == null)
				|| !(transactionDto.getType().equalsIgnoreCase("DEBIT")
						|| transactionDto.getType().equalsIgnoreCase("CREDIT"))) {
			throw new ValidationException("Transaction type is not valid");
		}
		
		

		//Is Amount sufficient in case of Debit
		if (transactionDto.getType().equalsIgnoreCase("DEBIT")) {
			if ((transactionDto.getAmount() == null) || !(transactionDto.getAmount().signum() > 0)) {
				throw new ValidationException("Invalid Amount");
			}
			try {
				final User user = userRepository.getUserWithAccounts(userId);
				boolean amountSufficient = false;
				for (final Account account: user.getAccounts() ) {
					if (account.getId().equals(transactionDto.getAccountId())) {
						if (account.getBalance().compareTo(transactionDto.getAmount()) >= 0) {
							amountSufficient = true;
							break;
						}
					}
				}
				if (!amountSufficient) {
					throw new ValidationException("Amount is not sufficient");
				}
			} catch (final Exception exception) {
				throw new AppException(exception.getMessage(), exception);
			}
		}
		//Handling Debit
		if (transactionDto.getType().equalsIgnoreCase("DEBIT")) {
			try {
				final User user = userRepository.getUserWithAllRelations(userId);
				for (int i = 0; i < (user.getAccounts().size() - 1); i++) {
					//Subtract Amount
					user.getAccounts().get(i).setBalance(
							user.getAccounts().get(i).getBalance()
								.subtract(transactionDto.getAmount())
							);
				}

				//Add Transaction
				final Transaction transaction = new Transaction();
				transaction.setAccountId(transactionDto.getAccountId());
				transaction.setAmount(transactionDto.getAmount());
				transaction.setDescription("Amount Debited");
				transaction.setType(transactionDto.getType());
				transaction.setUser(user);

				//Add Note
				final Note note = new Note();
				note.setDescription("Amount Debited");
				note.setUser(user);
				
				//For Future use
				//final Transaction transaction = new Transaction();
				//transaction.setAccountId(transactionDto.getAccountId());
				//transaction.setAmount(transactionDto.getAmount());
				//transaction.setDescription("Amount Debited");
				//transaction.setType(transactionDto.getType());
				//transaction.setUser(user);


				//Save
				user.getTransactions().add(transaction);
				user.getNotes().add(note);

				final User savedUser = userRepository.save(user);
				final UserDto userDto = userMapper.toDto(savedUser);
				log.info("Debit transaction successfully updated");
				final List<TransactionDto> transactions = userDto.getTransactions();
				return transactions.get(transactions.size() - 1);

			} catch (final Exception exception) {
				throw new AppException(exception.getMessage(), exception);
			}

		//Handling Credit
		} else if (transactionDto.getType().equalsIgnoreCase("CREDIT")) {
			try {
				final User user = userRepository.getUserWithAllRelations(userId);
				for (int i = 0; i < (user.getAccounts().size() - 1); i++) {
					//Add Amount
					user.getAccounts().get(i).setBalance(
							user.getAccounts().get(i).getBalance()
								.add(transactionDto.getAmount())
							);
				}

				//Add Transaction
				final Transaction transaction = new Transaction();
				transaction.setAccountId(transactionDto.getAccountId());
				transaction.setAmount(transactionDto.getAmount());
				transaction.setDescription("Amount Credited");
				transaction.setType(transactionDto.getType());
				transaction.setUser(user);

				//Add Note
				final Note note = new Note();
				note.setDescription("Amount Credited");
				note.setUser(user);

				//Save
				user.getTransactions().add(transaction);
				user.getNotes().add(note);
				final User savedUser = userRepository.save(user);
				log.info("Credit transaction successfully updated");
				final UserDto userDto = userMapper.toDto(savedUser);
				final List<TransactionDto> transactions = userDto.getTransactions();
				return transactions.get(transactions.size() - 1);
				
				


			} catch (final Exception exception) {
				exception.printStackTrace();
				throw new AppException(exception.getMessage(), exception);
			}
		}
		return null;
	}
	
	private String getUser() {
		return "user";
	}
	
	public String getAccount() {
		return "ACCOUNT";
	}

}
