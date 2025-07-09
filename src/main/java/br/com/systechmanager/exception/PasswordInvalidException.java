package br.com.systechmanager.exception;

public class PasswordInvalidException extends RuntimeException {
    public PasswordInvalidException() {
        super("Usuário e/ou senha inválida");
    }
}
