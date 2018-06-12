package service;

import java.util.logging.Logger;
import java.io.IOException;
import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.UnavailableException;

public class Initializer extends GenericServlet
{
    private final Logger logger = Logger.getLogger("Initializer");
    private MessageEmitter emitter;

    @Override
    public void init() throws ServletException
    {
        super.init();
        // Create the emitter
        emitter = new MessageEmitter();

        // Retrieve the CometD service instantiated by AnnotationCometdServlet
        CometDService service = (CometDService)getServletContext().getAttribute(CometDService.class.getName());

        // Register the service as a listener of the emitter
        emitter.getListeners().add(service);

        // Start the emitter
        emitter.start();

        logger.severe("Hello from Initializer");
    }

    @Override
    public void destroy()
    {
        // Stop the emitter
        emitter.stop();
    }

    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException
    {
        throw new UnavailableException("Initializer");
    }
}
