package dumbledore.servlet;

import dumbledore.DumbledoreException;
import dumbledore.metrics.SensorDescriptor;
import dumbledore.metrics.registry.SensorRegistry;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 *
 */
public class DumbledoreServlet extends HttpServlet {

    private static final long serialVersionUID = 1;

    private static final Logger logger = Logger.getLogger(DumbledoreServlet.class);

    /**
     * The registry object, protected so that the extending classes
     * could set this field via other methods, such as through a custom
     * ServletContext.
     */
    protected SensorRegistry registry;

    public DumbledoreServlet(SensorRegistry registry) {
        this.registry = registry;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        String domain = request.getParameter("domain");
        String type = request.getParameter("type");

        SensorDescriptor sensor = registry.getSensor(domain, type);
        if(sensor == null) {
            logger.warn("No sensor found for domain = "
                        + domain
                        + ", type = "
                        + type);
            response.sendError(HttpServletResponse.SC_NO_CONTENT);
            return;
        }
        if(logger.isDebugEnabled())
            logger.debug(sensor.toString());
        StringBuilder sb = new StringBuilder();
        JsonUtils.sensorToJson(sensor, sb);
        try {
            response.setContentType("text/plain");
            OutputStreamWriter writer = new OutputStreamWriter(response.getOutputStream());
            writer.write(sb.toString());
            writer.flush();
        } catch(Exception e) {
            throw new DumbledoreException(e);
        }
    }
}
