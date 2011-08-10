package dumbledore.example;

import dumbledore.DumbledoreException;
import dumbledore.annotations.Attribute;
import dumbledore.annotations.Sensor;
import dumbledore.metrics.DataType;
import dumbledore.metrics.MetricType;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 */
@Sensor(description = "Sensor for the Servlet")
public class ExampleServlet extends HttpServlet {

    private final AtomicInteger requests;

    public ExampleServlet() {
        requests = new AtomicInteger(0);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("<html>\n<head><title>Hello!</title></head>\n");
        sb.append("<body><h1>Hello!</h1>\n");
        sb.append("<p>You are visitor number ");
        sb.append(requests.incrementAndGet());
        sb.append("</p>\n</body>\n</html>\n");

        try {
            response.setContentType("text/html");
            OutputStreamWriter writer = new OutputStreamWriter(response.getOutputStream());
            writer.write(sb.toString());
            writer.flush();
        } catch(Exception e) {
            throw new DumbledoreException(e) ;
        }
    }

    @Attribute(name = "numRequests",
               description = "Number of requests received by the servlet",
               dataType =  DataType.INTEGER,
               metricType = MetricType.GAUGE)
    public int getNumRequests() {
        return requests.get();
    }
}
