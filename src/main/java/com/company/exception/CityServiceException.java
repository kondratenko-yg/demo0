package com.company.exception;

/**
 * Исключение, возникающее при ошибках в сервисе города.
 */
public class CityServiceException extends RuntimeException {

  public CityServiceException(String message) {
    super(message);
  }

  public CityServiceException(String message, Throwable cause) {
    super(message, cause);
  }
}

