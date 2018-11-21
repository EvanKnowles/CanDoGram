package za.co.knonchalant.candogram.handlers.exception;

public class CouldNotLookupBeanException extends RuntimeException {
    public CouldNotLookupBeanException(Exception ex) {
        super(ex);
    }
}

