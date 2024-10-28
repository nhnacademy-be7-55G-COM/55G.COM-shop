package shop.S5G.shop.controller.advice;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import shop.S5G.shop.dto.tag.MessageDto;
import shop.S5G.shop.exception.AlreadyExistsException;
import shop.S5G.shop.exception.BadRequestException;
import shop.S5G.shop.exception.EssentialDataNotFoundException;
import shop.S5G.shop.exception.ResourceNotFoundException;

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
}
