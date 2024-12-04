package it.intesys.orderservice.api.problem;

import it.intesys.orderservice.exception.OrderServiceException;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.*;
import org.springframework.validation.ObjectError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@RestControllerAdvice
@Slf4j
public class ApiErrorHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(@NotNull MethodArgumentNotValidException ex, @NotNull HttpHeaders headers, @NotNull
    HttpStatusCode status, @NotNull WebRequest request) {

        var defaultResponse = super.handleMethodArgumentNotValid(ex, headers, status, request);
        if (defaultResponse != null && defaultResponse.getBody() instanceof ProblemDetail pd) {
            var errors = ex.getBindingResult().getAllErrors().stream().map(ObjectError::toString).toList();
            pd.setProperty("errors", errors);
        }
        return defaultResponse;
    }

    @org.springframework.web.bind.annotation.ExceptionHandler
    protected ResponseEntity<Object> handleOrderServiceException(OrderServiceException ex, WebRequest request) {
        return handleOrderServiceException(ex, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    private ResponseEntity<Object> handleOrderServiceException(OrderServiceException ex, HttpStatus status, WebRequest request) {
        ErrorResponse.Builder builder = ErrorResponse.builder(ex, status, ex.getMessage());
        builder.detailMessageCode(ex.getCode());
        if (ex.getArguments() != null)
            builder.detailMessageArguments(ex.getArguments());
        ErrorResponse errorResponse = builder.build();
        ProblemDetail body = errorResponse.updateAndGetBody(getMessageSource(), LocaleContextHolder.getLocale());
        logger.error(body.getDetail());
        logger.trace(ex.getMessage(), ex);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/json;charset=UTF-8"));
        return createResponseEntity(body, headers, status, request);
    }

}
