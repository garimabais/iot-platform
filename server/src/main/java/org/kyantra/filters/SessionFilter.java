package org.kyantra.filters;

import org.kyantra.beans.UserBean;
import org.kyantra.dao.UserDAO;
import org.kyantra.interfaces.Session;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;

@Session
@Provider
@Priority(Priorities.AUTHORIZATION)
public class SessionFilter implements ContainerRequestFilter {

    @Context
    ResourceInfo resourceInfo;

    private boolean isSessionNeeded(AnnotatedElement annotatedElement) {
        if (annotatedElement == null) {
            return false;
        } else {
            Session secured = annotatedElement.getAnnotation(Session.class);
            if (secured != null) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        Method resourceMethod = resourceInfo.getResourceMethod();
        if(!isSessionNeeded(resourceMethod)){
            return;
        }

        String authorizationCookie = requestContext.getCookies().getOrDefault("authorization", new Cookie("token", "")).getValue();
        if(!authorizationCookie.isEmpty()){
            UserBean userBean = UserDAO.getInstance().getByToken(authorizationCookie);
            final SecurityContext currentSecurityContext = requestContext.getSecurityContext();
            requestContext.setSecurityContext(new SecurityContext() {

                @Override
                public Principal getUserPrincipal() {
                    return userBean;
                }

                @Override
                public boolean isUserInRole(String role) {
                    return false;
                }

                @Override
                public boolean isSecure() {
                    return currentSecurityContext.isSecure();
                }

                @Override
                public String getAuthenticationScheme() {
                    return "cookie";
                }
            });
            if(userBean==null){
                try {
                    throw new WebApplicationException(Response.temporaryRedirect(new URI("/login")).build());
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        }else {


            try {
                throw new WebApplicationException(Response.temporaryRedirect(new URI("/login")).build());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

        }
    }
}