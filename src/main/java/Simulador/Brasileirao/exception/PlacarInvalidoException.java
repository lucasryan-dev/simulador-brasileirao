package Simulador.Brasileirao.exception;

public class PlacarInvalidoException extends RuntimeException{
    public PlacarInvalidoException (String mensagem){
        super(mensagem);
    }
}
