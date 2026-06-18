package com.etiya.elearning.core.exceptions;

/**
 * İş kuralı ihlallerinde fırlatılan exception. Business katmanı (rules/manager)
 * tarafından atılır. Bu hatayı HTTP yanıtına çeviren global handler bir sonraki
 * adımda ({@code @RestControllerAdvice}) eklenecektir.
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}
