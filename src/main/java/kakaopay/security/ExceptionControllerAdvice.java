package kakaopay.security;

import kakaopay.exception.NotAllowedException;
import kakaopay.exception.TimeFormatException;
import kakaopay.utils.RestResponse;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.annotation.Resource;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestControllerAdvice
public class ExceptionControllerAdvice {

    @Resource(name = "messageSourceAccessor")
    private MessageSourceAccessor msa;

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public RestResponse emptyResultData(EntityNotFoundException exception) {
        return RestResponse.error(exception.getMessage()).build();
    }

    @ExceptionHandler(NotAllowedException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public RestResponse notAllowed(NotAllowedException exception) {
        return RestResponse.error(exception.getField(), exception.getMessage()).build();
    }

    @ExceptionHandler(TimeFormatException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public RestResponse timeFormatterException(TimeFormatException exception) {
        log.info("timeformatter : "+exception.getField());
        log.info("timeformatter : "+exception.getMessage());
        return RestResponse.error(exception.getField(), exception.getMessage()).build();
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public RestResponse badParameterDate(MethodArgumentTypeMismatchException exception){
        String errorField = exception.getName();
        if(errorField.equals("date")){
            return RestResponse.error(errorField, "date time이 옳지 않습니다. ").build();
        }
        return RestResponse.error(errorField,exception.getMessage()).build();

    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public RestResponse badChangeLocalDate(HttpMessageNotReadableException exception){
        log.info("localDate때문에 : " +exception.getLocalizedMessage());
        log.info("localDate때문에2 : " +exception.getMessage());
        return RestResponse.error("date", "시간 입력을 제대로 해주세요").build();

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public RestResponse<?> methodArgumentValidException(MethodArgumentNotValidException exception) {
        List<ObjectError> errors = exception.getBindingResult().getAllErrors();
        RestResponse.ErrorResponseBuilder errorResponseBuilder = RestResponse.error();
        for (ObjectError objectError : errors) {
            log.error("object error : {}", objectError);
            FieldError fieldError = (FieldError) objectError;
            errorResponseBuilder.appendError(fieldError.getField(), getErrorMessage(fieldError));
        }
        return errorResponseBuilder.build();
    }
    private String getErrorMessage(FieldError fieldError) {
        Optional<String> code = getFirstCode(fieldError);
        if (!code.isPresent()) {
            return null;
        }

        String errorMessage = msa.getMessage(code.get(), fieldError.getArguments(), fieldError.getDefaultMessage());
        log.info("error message: {}", errorMessage);
        return errorMessage;
    }

    private Optional<String> getFirstCode(FieldError fieldError) {
        String[] codes = fieldError.getCodes();
        if (codes == null || codes.length == 0) {
            return Optional.empty();
        }
        return Optional.of(codes[0]);
    }
}
