package users.middleware;

import java.io.IOException;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import users.clients.AuthenticationClient;


/**
 * The class AuthenticationInterceptor.
 */
public class AuthenticationInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private AuthenticationClient authenticationClient;

    /**
     * Method, executed before the handler for the request. This deals with whether the user
     * is authorized to perform an action.
     *
     * @param request  The request to handle
     * @param response The response to the request
     * @param handler  Handler object
     * @return True iff the handler of this request should be executed, i.e. when the user
     *     is authorized.
     */
    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) throws IOException {
        String[] url = request.getRequestURL().toString().split("/");
        String method = url[url.length - 1].toLowerCase(Locale.ROOT);

        if (method.equals("exists") || method.equals("canteambook")
                || method.equals("addreservation")
                || method.equals("deletereservation") || method.equals("adduser")
                || method.equals("delete") || method.equals("add")) {
            return true;
        }

        String jwt = request.getHeader("Authorization");
        if (jwt == null) {
            setUnauthorizedResponse(response);
            return false;
        }

        ResponseEntity<String> role = authenticationClient.permit(jwt);
        String roleString = role.getBody();

        if (roleString.equals("NONE")) {
            setUnauthorizedResponse(response);
            return false;
        } else if (roleString.equals("USER")) {
            if (method.equals("changesubscription")) {
                setUnauthorizedResponse(response);
                return false;
            }
        } else if (!roleString.equals("ADMIN")) {
            setUnauthorizedResponse(response);
            return false;
        }

        return true;
    }

    /**
     * Sets the body and status of a response object to correspond to an unauthorized response.
     *
     * @param response The response object to set the body and status to
     * @throws IOException the io exception
     */
    public void setUnauthorizedResponse(HttpServletResponse response) throws IOException {
        response.getWriter().println("You are not authorized to perform this action.");
        response.setStatus(401);
    }
}
