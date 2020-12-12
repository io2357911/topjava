package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.util.ValidationUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static ru.javawebinar.topjava.web.ExceptionInfoHandler.getDetail;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ModelAndView conflict(HttpServletRequest req, DataIntegrityViolationException e) {
        var rootCause = ValidationUtil.getRootCause(e);
        var message = getDetail(rootCause);
        return logAndGetModelAndView(req, e, rootCause, HttpStatus.UNPROCESSABLE_ENTITY, message);
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        return logAndGetModelAndView(req, e, HttpStatus.INTERNAL_SERVER_ERROR, null);
    }

    private static ModelAndView logAndGetModelAndView(HttpServletRequest req, Exception e, HttpStatus httpStatus, String message) {
        Throwable rootCause = ValidationUtil.getRootCause(e);
        return logAndGetModelAndView(req, e, rootCause, httpStatus, message);
    }

    private static ModelAndView logAndGetModelAndView(HttpServletRequest req, Exception e, Throwable rootCause, HttpStatus httpStatus, String message) {
        log.error("Exception at request " + req.getRequestURL(), e);

        if (message == null) {
            message = rootCause.getMessage();
        }

        ModelAndView mav = new ModelAndView("exception",
                Map.of("exception", rootCause, "message", message, "status", httpStatus));
        mav.setStatus(httpStatus);

        // Interceptor is not invoked, put userTo
        AuthorizedUser authorizedUser = SecurityUtil.safeGet();
        if (authorizedUser != null) {
            mav.addObject("userTo", authorizedUser.getUserTo());
        }
        return mav;
    }
}
