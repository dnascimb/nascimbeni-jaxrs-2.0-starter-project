package com.restfully.shop.services;

import java.net.URI;
import java.text.DateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.restfully.shop.domain.Customer;

@Path("/customers")
public class CustomerResource
{
   private Map<Integer, Customer> customerDB = new ConcurrentHashMap<Integer, Customer>();
   private AtomicInteger idCounter = new AtomicInteger();

   public CustomerResource()
   {
   }

   /**
    * Call this method to test if everything is working
    * ex. http://localhost:8080/api/customers/1.json
    * 
    * @param id - can be any integer
    * @return A JSON object representation of Bob Villa, a stock customer object
    */
   @GET
   @Path("/{id}.json")
   @Produces(MediaType.APPLICATION_JSON)
   public Response getCustomer(@PathParam("id") int id)
   {
	 ResponseBuilder responseBuilder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
	  
	 Customer customer = new Customer();
	 customer.setId(id);
	 customer.setFirstName("Bob");
	 customer.setLastName("Villa");
	 customer.setCity("San Jose");
	 customer.setCountry("USA");
	 customer.setState("CA");
	 customer.setStreet("1201 Parkmoor Ave");
	 customer.setZip("95126");
	
	 responseBuilder = responseBuilder.status(Response.Status.OK).entity(customer);
	 return responseBuilder.build();
   }
   
   @POST
   @Produces("text/html")
   public Response createCustomer(@FormParam("firstname") String first,
                                  @FormParam("lastname") String last)
   {
      Customer customer = new Customer();
      customer.setId(idCounter.incrementAndGet());
      customer.setFirstName(first);
      customer.setLastName(last);
      customerDB.put(customer.getId(), customer);
      System.out.println("Created customer " + customer.getId());
      String output = "Created customer <a href=\"customers/" + customer.getId() + "\">" + customer.getId() + "</a>";
      String lastVisit = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.LONG).format(new Date());
      URI location = URI.create("/customers/" + customer.getId());
      return Response.created(location)
              .entity(output)
              .cookie(new NewCookie("last-visit", lastVisit))
              .build();

   }

   @GET
   @Path("{id}")
   @Produces("text/plain")
   public Response getCustomer(@PathParam("id") int id,
                               @HeaderParam("User-Agent") String userAgent,
                               @CookieParam("last-visit") String date)
   {
      final Customer customer = customerDB.get(id);
      if (customer == null)
      {
         throw new WebApplicationException(Response.Status.NOT_FOUND);
      }
      String output = "User-Agent: " + userAgent + "\r\n";
      output += "Last visit: " + date + "\r\n\r\n";
      output += "Customer: " + customer.getFirstName() + " " + customer.getLastName();
      String lastVisit = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.LONG).format(new Date());
      return Response.ok(output)
              .cookie(new NewCookie("last-visit", lastVisit))
              .build();
   }

   @PUT
   @Path("{id}")
   @Consumes(MediaType.APPLICATION_JSON)
   public void updateCustomer(@PathParam("id") int id, Customer update)
   {
      Customer current = customerDB.get(id);
      if (current == null) throw new WebApplicationException(Response.Status.NOT_FOUND);

      current.setFirstName(update.getFirstName());
      current.setLastName(update.getLastName());
      current.setStreet(update.getStreet());
      current.setState(update.getState());
      current.setZip(update.getZip());
      current.setCountry(update.getCountry());
   }
}
