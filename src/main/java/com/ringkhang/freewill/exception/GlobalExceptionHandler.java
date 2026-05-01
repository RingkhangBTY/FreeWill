package com.ringkhang.freewill.exception;

import com.ringkhang.freewill.DTO.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // To handle exception thrown when program fails to fetch user/account or simply user/account doesn't exist.
    @ExceptionHandler(NoUserFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNoUserFoundException(NoUserFound noUserFound){
        String details = """
                This is an error caused while trying to find the user/account but
                no user exist. (Try again with valid user id)
               """.replace("\n"," ").trim();
        return new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                noUserFound.getMessage(),
                details
        );
    }

    // To handle exception thrown when a requested resource is not available
    @ExceptionHandler(ResourceNotAvailable.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleResourceNotFoundException(ResourceNotAvailable resourceNotAvailable){
        String details = """
                 Fails to fetch the needed resource due to system failure or 
                 invalid inputs.
                """.replace("\n","").trim();
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), resourceNotAvailable.getMessage(),details);
    }

    //To handle any general exception and code braks.
    @ExceptionHandler(GenericException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handlesAnyGenericError(GenericException exception){
        String details = """
                Faces unexpected error cause by system failure.
                """.replace("\n","").trim();
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage(),details);
    }

    // use to handle when a user/account is not authorize to perform some task.
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleUnauthorizeException (UnauthorizedException e){
        String details = """
                
                """.replace("\n","").trim();
        return new ErrorResponse(HttpStatus.UNAUTHORIZED.value(),e.getMessage(),details);
    }

    // Global fallback handler
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleUnhandledException(Exception ex) {
        return new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Unexpected error occurred",
                ex.getMessage()
        );
    }

    // Validation error handler
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationErrors(MethodArgumentNotValidException ex) {

        String details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err ->
                        err.getField() + ": " + err.getDefaultMessage()
                )
                .collect(Collectors.joining(", "));

        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation failed",
                details);
    }

    //If fails to create any new resources like new entries etc
    @ExceptionHandler(CreateNewResourceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleFailedNewResourcesCreatedException(
            CreateNewResourceException ex){

        String details = ex.getMessage();
        return new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Failed to create new resources",
                details);
    }

    // handle method arguments validations
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolation(ConstraintViolationException ex) {

        String details = ex.getConstraintViolations()
                .stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .collect(Collectors.joining(", "));

        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation failed",
                details
        );
    }

}