package clean.code.challenge.dto;

import org.apache.commons.lang3.StringUtils;

public enum TransactionType {
	CREDIT("CREDIT") {
		@Override
		public String transactionSuccessMsg() {
			return "Amount Credited";
		}

		@Override
		public String transactionLogMsg() {
			return "Credit transaction successfully updated";
		}
	},
	DEBIT("DEBIT") {
		@Override
		public String transactionSuccessMsg() {
			return "Amount Debited";
		}

		@Override
		public String transactionLogMsg() {
			return "Debit transaction successfully updated";
		}
	};
	private String transactionType;

	public String getTransactionType() {
		return transactionType;
	}

	TransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String transactionSuccessMsg() {
		return StringUtils.EMPTY;
	}

	public String transactionLogMsg() {
		return StringUtils.EMPTY;
	}
}
