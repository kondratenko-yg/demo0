package com.company.exception;

/**
 * Исключение, возникающее при ошибках в сервисе пользователя.
 */
public class UserServiceException extends RuntimeException {

  public UserServiceException(String message) {
    super(message);
  }

  public UserServiceException(String message, Throwable cause) {
    super(message, cause);
  }
}

