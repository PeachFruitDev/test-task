package test.interview.parser;

import java.util.Collections;
import java.util.List;

public class ParserException extends RuntimeException {
    private final List<String> errors;

    public ParserException(String message, List<String> errors, Throwable throwable) {
        super(message, throwable);
        if (errors == null) {
            this.errors = Collections.emptyList();
        } else {
            this.errors = Collections.unmodifiableList(errors);
        }
    }

    public ParserException(String message, List<String> errors) {
        this(message, errors, null);
    }

    public List<String> getErrors() {
        return errors;
    }
}
