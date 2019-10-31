package clean.code.challenge.api.exceptionhandler;

import java.util.List;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class ApiError {
	private HttpStatus status;
	   private String message;
	   private String debugMessage;
	   private List<ApiSubError> subErrors;

	   private ApiError() {
	   }

	   ApiError(HttpStatus status) {
	       this();
	       this.status = status;
	   }

	   ApiError(HttpStatus status, Throwable ex) {
	       this();
	       this.status = status;
	       this.message = "Unexpected error";
	       this.debugMessage = ex.getLocalizedMessage();
	   }

	   ApiError(HttpStatus status, String message, Throwable ex) {
	       this();
	       this.status = status;
	       this.message = message;
	       this.debugMessage = ex.getLocalizedMessage();
	   }
	}
