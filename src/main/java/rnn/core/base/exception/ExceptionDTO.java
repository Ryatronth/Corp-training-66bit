package rnn.core.base.exception;

import lombok.Builder;

@Builder
public record ExceptionDTO(String message, Object error) {
}
