package mizuki.project.core.restserver.config.exception;

/**
 * Created by ycj on 2016/11/12.
 */
public class RestMainException extends Exception {

    public RestMainException() {
    }

    public RestMainException(String message) {
        super(message);
    }

    public RestMainException(String message, Throwable cause) {
        super(message, cause);
    }

    public RestMainException(Throwable cause) {
        super(cause);
    }
}
