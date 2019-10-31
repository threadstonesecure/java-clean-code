package clean.code.challenge.api.exceptionhandler;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import clean.code.challenge.exceptions.AppException;
import clean.code.challenge.exceptions.ValidationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		String error = "Malformed JSON request";
		return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, error, ex));
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
	  @ResponseStatus(HttpStatus.BAD_REQUEST)
	  @ResponseBody
	  String handleConstraintViolationException(ConstraintViolationException e) {
	    return "not valid due to validation error: " + e.getMessage();
	  }

	@ExceptionHandler(EntityNotFoundException.class)
	protected ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex) {
		return buildResponseEntity(HttpStatus.NOT_FOUND, ex);
	}

	@ExceptionHandler(ValidationException.class)
	protected ResponseEntity<Object> handleValidation(ValidationException validationException) {
		return buildResponseEntity(HttpStatus.BAD_REQUEST, validationException);
	}

	@ExceptionHandler(AppException.class)
	protected ResponseEntity<Object> handleAppException(AppException appException) {
		return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, appException);
	}

	private ResponseEntity<Object> buildResponseEntity(HttpStatus httpStatus, Exception ex) {
		log.error(ex.getMessage(), ex);
		ApiError apiError = new ApiError(httpStatus);
		apiError.setMessage(ex.getMessage());
		return buildResponseEntity(apiError);
	}

	private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
		return new ResponseEntity<>(apiError, apiError.getStatus());
	}

}
