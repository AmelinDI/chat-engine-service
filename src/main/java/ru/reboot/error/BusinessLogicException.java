package ru.reboot.error;

public class BusinessLogicException extends RuntimeException {

    private final String code;

    public BusinessLogicException(String message, String code) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return String.format("BusinessLogicException{message=%s code=%s}", getMessage(), getCode());
    }
}
