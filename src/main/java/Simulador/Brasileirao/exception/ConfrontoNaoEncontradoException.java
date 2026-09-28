package Simulador.Brasileirao.exception;

public class ConfrontoNaoEncontradoException extends RuntimeException {
    public ConfrontoNaoEncontradoException(Long id) {
        super("Confronto não encontrado: " + id);
    }
}
