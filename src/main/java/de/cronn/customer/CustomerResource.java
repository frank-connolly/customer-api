package de.cronn.customer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import javax.print.attribute.standard.Media;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Path("/api/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes({MediaType.APPLICATION_JSON})
@Slf4j
@AllArgsConstructor
public class CustomerResource {

    private CustomerService customerService;

    @GET
    @APIResponses(value = {
            @APIResponse(
                    responseCode = "200",
                    description = "Get all customers",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = SchemaType.OBJECT, implementation = Customer.class)))
    })
    public Response get() {
        return Response.ok(customerService.findAll()).build();
    }

    @GET
    @Path("/{customerId}")
    @APIResponses(value = {
            @APIResponse(
                    responseCode = "200",
                    description = "Get customer by customerId",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = SchemaType.OBJECT, implementation = Customer.class))),
            @APIResponse(
                    responseCode = "404",
                    description = "No customer found for customerId provided",
                    content = @Content(mediaType = "application/json"))
    })
    public Response getById(@PathParam("customerId") Integer customerId) {
        Optional<Customer> optionalCustomer = customerService.findById(customerId);
        return !optionalCustomer.isEmpty() ?
                Response.ok(optionalCustomer.get()).build() : Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @APIResponses(value = {
            @APIResponse(
                    responseCode = "201",
                    description = "Customer created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = SchemaType.OBJECT, implementation = Customer.class))),
            @APIResponse(
                    responseCode = "400",
                    description = "Customer already exists for customerId",
                    content = @Content(mediaType = "application/json"))
    })
    public Response post(@Valid Customer customer) {
        final Customer saved = customerService.save(customer);
        return Response.status(Response.Status.CREATED).entity(saved).build();
    }

    @PUT
    @APIResponses(value = {
            @APIResponse(
                    responseCode = "200",
                    description = "Customer updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = SchemaType.OBJECT, implementation = Customer.class))),
            @APIResponse(
                    responseCode = "404",
                    description = "No customer found for customerId provided",
                    content = @Content(mediaType = "application/json"))
    })
    public Response put(@Valid Customer customer) {
        final Customer saved = customerService.update(customer);
        return Response.ok(saved).build();
    }

}
