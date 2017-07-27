package mizuki.project.core.restserver.config.exception;

import org.springframework.ui.Model;

/**
 * Created by ycj on 2016/11/12.
 */
public class RestMainException extends Exception {

    private Model model;

    public RestMainException() {
    }

    public RestMainException(String msg,Model model){
        super(msg);
        this.model=model;
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
    public RestMainException(Throwable cause,Model model) {
        super(cause);
        this.model=model;
    }

    public Model getModel() {
        return model;
    }

    public RestMainException setModel(Model model) {
        this.model = model;
        return this;
    }
}
