package yeter.ugur.insuranceexample.api;


public class PolicyIsNotFoundException extends RuntimeException {
    public PolicyIsNotFoundException(String message) {
        super(message);
    }
}
