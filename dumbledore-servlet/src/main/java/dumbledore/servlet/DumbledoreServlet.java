package dumbledore.servlet;

import dumbledore.metrics.MetricsRepository;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 */
public class DumbledoreServlet extends HttpServlet {

    private static final long serialVersionUID = 1;

    private static final Logger logger = Logger.getLogger(DumbledoreServlet.class);

    /**
     * The repository object, protected so that the extending classes
     * could set this field via other methods, such as through a custom
     * ServletContext.
     */
    protected MetricsRepository repository;

    public DumbledoreServlet(MetricsRepository repository) {
        this.repository = repository;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        // TODO: basic REST API
    }
}
