package accountservice.resources;

import accountservice.domain.Account;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.annotation.PostConstruct;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.net.URI;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Path("/accounts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AccountResource {

    public static final Account ACCOUNT1 = new Account(1l, 1l, "Customer1", BigDecimal.valueOf(123.122));

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
            ResourceError resourceError = new ResourceError();
            resourceError.setExceptionType(exception.getClass().getName());
            resourceError.setCode(code);
            if (exception.getMessage() != null) {
                resourceError.setError(exception.getMessage());
            }
            return Response.status(code)
                    .entity(resourceError)
                    .build();
        }
    }

    @Data
    /*
    After native compiling the class not being used by any path in the code gets deleted, this causes at runtime the following:
    org.jboss.resteasy.spi.UnhandledException: com.fasterxml.jackson.databind.exc.InvalidDefinitionException:
     *  No serializer found for class accountservice.resources.AccountResource$ResourceError and no properties discovered to create BeanSerializer (to avoid exception, disable SerializationFeature.FAIL_ON_EMPTY_BEANS). This appears to be a native image, in which case you may need to configure reflection for the class that is to be serialized
    This because, at the moment, when JSON-B or Jackson tries to get the list of fields of a class, if the class is not registered for reflection, no exception will be thrown. GraalVM will simply return an empty list of fields.
     The solution is to apply the annotation to instructs Quarkus to keep the class and its members during the native compilation
     */
    @RegisterForReflection
    @EqualsAndHashCode
    public static class ResourceError {
        private String exceptionType;
        private int code;
        private String error;

    }


    @PostConstruct
    public void initialise() {
        accounts = new HashSet<>();
        accounts.add(ACCOUNT1);
        accounts.add(new Account(2l, 1l, "Customer2", BigDecimal.valueOf(123321.122)));
        accounts.add(new Account(3l, 2l, "Customer2", BigDecimal.valueOf(12.122)));
    }

    @GET
    public Set<Account> allAccounts() {
        return accounts;
    }

    @GET
    @Path("/{accountNumber}")
    public Account getAccount(@PathParam("accountNumber") Long accountNumber) {
        Optional<Account> response = accounts.stream()
                .filter(acct -> acct.getAccountNumber().equals(accountNumber))
                .findFirst();
        return response.orElseThrow(()
                //this exception class will be intercepted by the provider
                -> new WebApplicationException("Account with id of " + accountNumber +
                " does not exist.", 404));
    }

    @POST
    public Response add(Account account, @Context UriInfo uriInfo) {
        accounts.add(account);
        URI uri = uriInfo.getAbsolutePathBuilder().path(Long.toString(account.getAccountNumber())).build();
        return Response.created(uri).entity(account).build();
    }

    @DELETE
    public Response delete(Account account) {
        accounts.removeIf(existingAccount -> existingAccount.getAccountNumber().equals(account.getAccountNumber()));
        return Response.ok().entity(accounts).build();
    }

}
