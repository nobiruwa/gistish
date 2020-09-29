package javaee7.jsf.example.http;

import java.security.Principal;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.servlet.http.HttpServletRequest;

@Interceptor
@CheckRequest
public class CheckRequestInterceptor {
    private static final Logger logger = Logger.getLogger(CheckRequestInterceptor.class.getName());

    @Inject
    HttpServletRequest request;

    @AroundInvoke
    public Object checkAccess(InvocationContext context) throws Exception {
        logger.log(Level.SEVERE, "START");
        try {
            Principal principal = FacesContext.getCurrentInstance()
                .getExternalContext()
                .getUserPrincipal();

            if (principal != null) {
                logger.log(Level.INFO, "principal: " + principal.getName());
            } else {
                logger.log(Level.INFO, "princpal: null");
            }

            Object result = context.proceed();

            logger.log(Level.SEVERE, "END");

            return result;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "END with Error");
            throw e;
        }
    }
}
