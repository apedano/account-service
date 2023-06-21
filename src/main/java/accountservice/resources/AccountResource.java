package accountservice.resources;

import accountservice.domain.Account;
import jakarta.annotation.PostConstruct;
import jakarta.json.Json;
import jakarta.json.JsonObjectBuilder;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Path("/accounts")
public class AccountResource {

    Set<Account> accounts = new HashSet<>();

    @Provider //indicates the class is an autodiscovered JAX-RS Provider
    public static class ErrorMapper implements ExceptionMapper<Exception> { //Implements ExceptionMapper for all Exception types
        @Override
        public Response toResponse(Exception exception) {
            int code = 500;
            if (exception instanceof WebApplicationException) {
                    code = ((WebApplicationException)
                    exception).getResponse().getStatus();
            }
            JsonObjectBuilder entityBuilder = Json.createObjectBuilder()
                    .add("exceptionType", exception.getClass().getName())
                    .add("code", code);
            if (exception.getMessage() != null) {
                entityBuilder.add("error", exception.getMessage());
            }
            return Response.status(code)
                    .entity(entityBuilder.build())
                    .build();
        }
    }


    @PostConstruct
    public void initialise() {
        accounts.add(new Account(1l, 1l, "Customer1", BigDecimal.valueOf(123.122)));
        accounts.add(new Account(2l, 1l, "Customer1", BigDecimal.valueOf(123321.122)));
        accounts.add(new Account(3l, 2l, "Customer2", BigDecimal.valueOf(12.122)));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Set<Account> allAccounts() {
        return accounts;
    }

    @GET
    @Path("/{accountNumber}")
    @Produces(MediaType.APPLICATION_JSON)
    public Account getAccount(@PathParam("accountNumber") Long accountNumber) {
        Optional<Account> response = accounts.stream()
                .filter(acct -> acct.getAccountNumber().equals(accountNumber))
                .findFirst();
        return response.orElseThrow(()
                //this exception class will be intercepted by the provider
                -> new WebApplicationException("Account with id of " + accountNumber +
                " does not exist.", 404));
    }


}
