package tech.balaji.urlshortener.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CommonException extends RuntimeException {

	/**
	 * SerialVersionUID
	 */
	private static final long serialVersionUID = -5821789144577589829L;

	private final HttpStatus code;

	public CommonException(String m, HttpStatus code) {
		super(m);
		this.code = code;
	}
}
