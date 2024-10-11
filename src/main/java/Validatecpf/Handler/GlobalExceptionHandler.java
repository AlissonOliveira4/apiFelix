package Validatecpf.Handler;

import Validatecpf.Domain.ErrorPersonalizado;
import Validatecpf.Exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler{

    // BAD_REQUEST
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ErrorPersonalizado handler(HttpMessageNotReadableException ex){
        return new ErrorPersonalizado(ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorPersonalizado handler(IllegalArgumentException ex){
        return new ErrorPersonalizado(ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(NumberFormatException.class)
    public ErrorPersonalizado handler(NumberFormatException ex){
        return new ErrorPersonalizado(ex.getMessage());
    }

    // NOT_FOUND

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody//Para o resultado desse exception ir no corpo de resposta
    @ExceptionHandler(NotFoundException.class)//Utilizada para quando ocorrer uma exceção desse tipo ele vai puxar esse método
    public ErrorPersonalizado handler(NotFoundException ex){
        return new ErrorPersonalizado(ex.getMessage());
    }

    //INTERNAL_SERVER_ERROR

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody//Para o resultado desse exception ir no corpo de resposta
    @ExceptionHandler(NullPointerException.class)//Utilizada para quando ocorrer uma exceção desse tipo ele vai puxar esse método
    public ErrorPersonalizado handler(NullPointerException ex){
        return new ErrorPersonalizado(ex.getMessage());
    }

}
