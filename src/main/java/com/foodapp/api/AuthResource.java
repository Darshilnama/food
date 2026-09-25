package com.foodapp.api;

import com.foodapp.entity.User;
import com.foodapp.service.AuthService;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Map;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    private AuthService authService;

    @POST
    @Path("/login")
    public Response login(Map<String, String> credentials, @Context HttpServletRequest req) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        User user = authService.login(username, password);
        if (user == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(Map.of("error", "Invalid credentials"))
                    .build();
        }

        // Create standard session so WebSocket and other endpoints share auth
        HttpSession session = req.getSession(true);
        session.setAttribute("user", user);
        session.setAttribute("userId", user.getUserId());
        session.setAttribute("username", user.getUsername());
        session.setAttribute("role", user.getRole());

        return Response.ok(Map.of(
                "success", true,
                "userId", user.getUserId(),
                "role", user.getRole().name()
        )).build();
    }
    
    @POST
    @Path("/logout")
    public Response logout(@Context HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return Response.ok(Map.of("success", true)).build();
    }
}
