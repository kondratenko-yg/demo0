package com.company.exception;

/**
 * Исключение, выбрасываемое, когда город не найден или не удалось создать новую запись.
 */
public class CityNotFoundException extends RuntimeException {

  /**
   * Создает новое исключение с указанным сообщением.
   *
   * @param message Сообщение об ошибке
   */
  public CityNotFoundException(String message) {
    super(message);
  }

  /**
   * Создает новое исключение с указанным сообщением и причиной.
   *
   * @param message Сообщение об ошибке
   * @param cause Причина, приведшая к возникновению ошибки
   */
  public CityNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
