package shop.s5g.shop.controller.advice;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import shop.s5g.shop.dto.tag.MessageDto;
import shop.s5g.shop.exception.AlreadyDeletedRecordException;
import shop.s5g.shop.exception.AlreadyExistsException;
import shop.s5g.shop.exception.AuthenticationException;
import shop.s5g.shop.exception.BadRequestException;
import shop.s5g.shop.exception.EssentialDataNotFoundException;
import shop.s5g.shop.exception.ForbiddenResourceException;
import shop.s5g.shop.exception.ResourceNotFoundException;

/**
 * 공통 예외 처리 advice
 *
 * @author bobo1006
 */
@RestControllerAdvice
public class RestWebAdvice {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<MessageDto> handleBadRequestException(BadRequestException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageDto(e.getMessage()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<MessageDto> handleResourceNotFoundException(ResourceNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageDto(e.getMessage()));
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<MessageDto> handleAlreadyExistsException(AlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageDto(e.getMessage()));
    }

    @ExceptionHandler(EssentialDataNotFoundException.class)
    public ResponseEntity<MessageDto> handleEssentialDataNotFoundException(EssentialDataNotFoundException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageDto(e.getMessage()));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<MessageDto> handleAuthenticationException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageDto(e.getMessage()));
    }

    @ExceptionHandler(AlreadyDeletedRecordException.class)
    public ResponseEntity<MessageDto> handleAlreadyDeletedRecordException(AlreadyDeletedRecordException e) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new MessageDto(e.getMessage()));
    }

    @ExceptionHandler(ForbiddenResourceException.class)
    public ResponseEntity<MessageDto> handleForbiddenResourceException(ForbiddenResourceException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageDto(e.getMessage()));
    }

}
