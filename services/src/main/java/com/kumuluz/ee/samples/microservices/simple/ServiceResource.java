package com.kumuluz.ee.samples.microservices.simple;

import com.kumuluz.ee.samples.microservices.simple.models.Service;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;


@Path("/services")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ServiceResource {

    @PersistenceContext
    private EntityManager em;

    /**
     * Vrne seznam vseh servicev
     * */
    @GET
    public Response getServices() {

        TypedQuery<Service> query = em.createNamedQuery("Service.findAll", Service.class);

        List<Service> profiles = query.getResultList();

        return Response.ok(profiles).build();
    }

    /**
     * Pridobi posamezen service glede na njegov id
     */

    @GET
    @Path("/{id}")
    public Response getProfile(@PathParam("id") Integer id) {

        Service p = em.find(Service.class, id);

        if (p == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        return Response.ok(p).build();
    }

    /**
     * Omogoča urejanje servica  tako, da staremu servicu nastavi nove vrednosti
     */
    @POST
    @Path("/{id}")
    public Response editProfile(@PathParam("id") Integer id, Service service) {

        Service p = em.find(Service.class, id);

        if (p == null)
            return Response.status(Response.Status.NOT_FOUND).build();

       p.setTitle(service.getTitle());
       p.setDescription(service.getDescription());
       p.setLocation(service.getLocation());
       p.setPrice(service.getPrice());

        em.getTransaction().begin();

        em.persist(p);

        em.getTransaction().commit();

        return Response.status(Response.Status.CREATED).entity(p).build();
    }

    /**
     * Doda nov service (Service p)
     */
    @POST
    public Response createProfile(Service p) {

        p.setId(null);

        em.getTransaction().begin();

        em.persist(p);

        em.getTransaction().commit();

        return Response.status(Response.Status.CREATED).entity(p).build();
    }
}
