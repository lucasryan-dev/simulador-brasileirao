package Simulador.Brasileirao.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(TimeNaoEncontradoException.class)
    public ResponseEntity<String> handleTimeNaoEncontrado(TimeNaoEncontradoException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }

    @ExceptionHandler(PlacarInvalidoException.class)
    public ResponseEntity<String> handlePlacarInvalido (PlacarInvalidoException exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleErroGenerico(Exception exception){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro inesperado: " + exception.getMessage());
    }

    @ExceptionHandler(ConfrontoNaoEncontradoException.class)
    public ResponseEntity<String> handleConfrontoNaoEncontrado(ConfrontoNaoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
