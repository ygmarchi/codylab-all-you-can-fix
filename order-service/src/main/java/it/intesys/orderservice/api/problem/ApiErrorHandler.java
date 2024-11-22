package it.intesys.orderservice.api.problem;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
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
}
