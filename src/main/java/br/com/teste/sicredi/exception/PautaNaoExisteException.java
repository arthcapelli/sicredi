package br.com.teste.sicredi.exception;

public class PautaNaoExisteException extends RuntimeException {

    public PautaNaoExisteException(String message) {
        super(message);
    }
}
