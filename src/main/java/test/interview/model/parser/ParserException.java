package test.interview.model.parser;

import java.util.List;

public class ParserException extends RuntimeException {
    private final List<String> errors;

    public ParserException(String message, List<String> errors) {
        super(message);
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}
