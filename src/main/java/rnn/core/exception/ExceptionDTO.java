package rnn.core.exception;

import lombok.Builder;

@Builder
public record ExceptionDTO(String message, Object error) {
}
