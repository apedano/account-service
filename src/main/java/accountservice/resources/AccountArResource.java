package accountservice.resources;

import accountservice.domain.AccountAr;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.math.BigDecimal;
import java.util.List;

@Path("/accounts/ar/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AccountArResource {

    @GET
    public List<AccountAr> allAccounts() {
        return AccountAr.listAll(); //method from the PanacheBaseEntity
    }

    @GET
    @Path("/{acctNumber}")
    public AccountAr getAccount(@PathParam("acctNumber") Long accountNumber) {
        return AccountAr.findByAccountNumber(accountNumber);
    }

    @POST
    @Transactional
    public Response createAccount(AccountAr account) {
        //Adds a new Account instance into the persistence context. On transaction commit, the record will be added to the database
        account.persist();
        return Response.status(201).entity(account).build();
    }

    @PUT
    @Path("{accountNumber}/withdrawal")
    @Transactional
    public AccountAr withdrawal(@PathParam("accountNumber") Long accountNumber,
                              String amount) {
        AccountAr entity = AccountAr.findByAccountNumber(accountNumber);
        //When modifying an existing instance, it will be persisted on transaction completion
        entity.withdrawFunds(new BigDecimal(amount));
        return entity;
    }
}
