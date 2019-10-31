package clean.code.challenge.dto;

public interface ChallengeConstants {
	String ROOT_ENDPOINT = "/";
	String USERS_ENDPOINT = "/users/";
	String USER_ENDPOINT = USERS_ENDPOINT+"{userId}";
	String USER_TRANSACTIONS_ENDPOINT = USERS_ENDPOINT + "transactions";
}
