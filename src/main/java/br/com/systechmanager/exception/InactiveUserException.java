package br.com.systechmanager.exception;

public class InactiveUserException  extends RuntimeException {
    public InactiveUserException() {
        super("Usuário Inativo");
    }
}
