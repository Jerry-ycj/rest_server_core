package mizuki.project.core.restserver.config.exception;

/**
 * Created by ycj on 2016/12/19.
 */
public class WebMainException extends Exception {
    public WebMainException() {
        super();
    }

    public WebMainException(String message) {
        super(message);
    }

    public WebMainException(Throwable cause) {
        super(cause);
    }
}
