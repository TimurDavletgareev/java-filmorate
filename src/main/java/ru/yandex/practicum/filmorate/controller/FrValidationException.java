package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/*
    По условиям теории мы используем спринг версии 2.7, а в статье о @ControllerAdvice говорится, что нужен Spring 3.2
    Чтобы избежать вывода всего стэктрейса в консоль я добавил аннотацию
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR) перед исключением (благодаря той же статье, как всегда
    спасибо за полезную информацию:)

    Теперь при неправильном запросе получаем в консоли только сообщение из getMessage и три строки про нашу ошибку

 */
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class FrValidationException extends RuntimeException {

    public FrValidationException(String message) {
        super(message);
    }

}
