package jpabook.jpashop.common.exception.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import jpabook.jpashop.common.model.response.ErrorCode;
import jpabook.jpashop.common.model.response.ErrorResponse;

@RestControllerAdvice
public class BadRequestHandler {

	@ExceptionHandler({MaxUploadSizeExceededException.class})
	public ResponseEntity<ErrorResponse> uploadException(MaxUploadSizeExceededException exc, 
		      HttpServletRequest request,
		      HttpServletResponse response) {
		
		return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(ErrorResponse.toResponseEntity(ErrorCode.PAYLOAD_TOO_LARGE).getBody());

	}
}