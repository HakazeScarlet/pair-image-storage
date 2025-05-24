package github.com.pairimagestorage.exception;

public class IOResourceException extends RuntimeException{
    public IOResourceException(String message, Exception e) {
        super(message, e);
    }
}
