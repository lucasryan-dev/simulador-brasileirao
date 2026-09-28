package Simulador.Brasileirao.exception;

public class TimeNaoEncontradoException extends RuntimeException {
    public TimeNaoEncontradoException(String sigla){
        super ("Time não encontrado: + sigla");
    }

}
