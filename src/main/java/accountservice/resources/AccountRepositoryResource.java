package accountservice.resources;

import accountservice.domain.AccountR;
import accountservice.repos.AccountRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.math.BigDecimal;
import java.util.List;

@Path("/accounts/repo/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AccountRepositoryResource {

    @Inject
    AccountRepository accountRepository;

    @GET
    public List<AccountR> allAccounts() {
        return accountRepository.listAll();
    }
    @GET
    @Path("/{acctNumber}")
    public AccountR getAccount(@PathParam("acctNumber") Long accountNumber) {
        AccountR account = accountRepository.findByAccountNumber(accountNumber);
        return account;
    }
    @POST
    @Transactional
    public Response createAccount(AccountR account) {
        accountRepository.persist(account);
        return Response.status(201).entity(account).build();
    }
    @PUT
    @Path("{accountNumber}/withdrawal")
    @Transactional
    public AccountR withdrawal(@PathParam("accountNumber") Long accountNumber,
                              String amount) {

        AccountR entity = accountRepository.findByAccountNumber(accountNumber);
        entity.withdrawFunds(new BigDecimal(amount));
        return entity;
    }
}