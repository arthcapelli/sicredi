package br.com.teste.sicredi.exception;

public class VotoInvalidoException extends RuntimeException{
    public VotoInvalidoException(String message) {
        super(message);
    }
}
