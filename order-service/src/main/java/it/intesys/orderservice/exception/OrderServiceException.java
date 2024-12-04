package it.intesys.orderservice.exception;

import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.LinkedList;

public abstract class OrderServiceException extends RuntimeException{
    private static volatile MessageSource messageSource;
    private final String code;
    private final Object [] arguments;
    private final String message;

    protected OrderServiceException() {
        code = code (getClass());
        arguments = null;
        message = initMessage();
    }


    protected OrderServiceException(Object... arguments) {
        code = code (getClass());
        this.arguments = arguments;
        message = initMessage();
    }

    public static MessageSource getMessageSource() {
        return messageSource;
    }

    private String initMessage() {
        try {
            return messageSource != null ? messageSource.getMessage(code, arguments, LocaleContextHolder.getLocale()) : code;
        } catch (NoSuchMessageException e) {
            return code;
        }
    }

    private static String code(Class<? extends OrderServiceException> cls) {
        LinkedList<String> tokens = splitCamelCaseString(cls.getSimpleName());
        if (!tokens.isEmpty() && tokens.getLast().equals("exception"))
            tokens.removeLast();
        return String.join(".", tokens);
    }

    /*
        https://www.programcreek.com/2011/03/java-method-for-spliting-a-camelcase-string/
     */
    public static LinkedList<String> splitCamelCaseString(String s){
        LinkedList<String> result = new LinkedList<String>();
        for (String w : s.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])")) {
            result.add(w.toLowerCase());
        }
        return result;
    }

    public static void setMessageSource(MessageSource messageSource) {
        OrderServiceException.messageSource = messageSource;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Object[] getArguments() {
        return arguments;
    }

    public String getCode() {
        return code;
    }

    public static void main(String[] args) throws ClassNotFoundException {
        System.out.println(code (Class.forName(args[0]).asSubclass(OrderServiceException.class)));
    }


}
