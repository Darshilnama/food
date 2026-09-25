package com.foodapp.api;

import com.foodapp.dao.RestaurantDAO;
import com.foodapp.entity.Restaurant;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/restaurants")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RestaurantResource {

    @Inject
    private RestaurantDAO restaurantDAO;

    @GET
    public Response getAllRestaurants() {
        List<Restaurant> restaurants = restaurantDAO.findAll();
        return Response.ok(restaurants).build();
    }

    @GET
    @Path("/{id}")
    public Response getRestaurant(@PathParam("id") Long id) {
        Restaurant restaurant = restaurantDAO.findById(id);
        if (restaurant == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(restaurant).build();
    }
}
