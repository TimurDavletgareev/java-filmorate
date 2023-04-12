package ru.yandex.practicum.filmorate.controller;

public class frValidationException extends RuntimeException {

    public frValidationException(String message) {
        super(message);
    }

    /*
        Не разобрался, как с помощью написанного исключения выводить в консоль краткое описание ошибки,
        а не весь стэк-трэйс. Хотелось бы, чтобы в консоль выводилось сообщение как при использовании
        spring-boot-starter-validation

        И хотелось бы знать, как без использования исключений обрабатывать неподходящие запросы в контроллерах
        и возвращать клиенту правильный статус-код и тело ответа. Подозреваю, что через аннотацию @ResponseStatus,
        но дальше не разобрался.
     */


}
