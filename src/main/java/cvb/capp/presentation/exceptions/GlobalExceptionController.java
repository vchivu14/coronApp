package cvb.capp.presentation.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.io.InvalidClassException;
import java.sql.SQLException;

@ControllerAdvice
@EnableWebMvc
public class GlobalExceptionController {
    protected Logger logger;

    public GlobalExceptionController() {
        logger = LoggerFactory.getLogger(getClass());
    }

    @ResponseStatus(value = HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler({MethodNotAllowedException.class})
    public String notAllowedException(Exception exception) {
        logger.error("Request raised an exception " + exception.getClass().getSimpleName());
        return "403";
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler({EmptyResultDataAccessException.class})
    public String notFoundConflict(Exception exception) {
        logger.error("Request raised an exception " + exception.getClass().getSimpleName());
        return "404";
    }

    @ResponseStatus(value = HttpStatus.CONFLICT)
    @ExceptionHandler({DataIntegrityViolationException.class})
    public String integrityConflict(Exception exception) {
        logger.error("Request raised an exception " + exception.getClass().getSimpleName());
        return "409";
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({SQLException.class, DataAccessException.class})
    public String serverError(Exception exception) {
        logger.error("Request raised an exception " + exception.getClass().getSimpleName());
        return "500";
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler({InvalidClassException.class})
    public String badRequest(Exception exception) {
        logger.error("Request raised an exception " + exception.getClass().getSimpleName());
        return "400";
    }
}
