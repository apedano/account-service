package accountservice.resources;

import accountservice.domain.Account;
import accountservice.domain.AccountStatus;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import banking.api.service.AccountService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;


@Path("/api/accounts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AccountServiceApiImpl extends AccountRepositoryResource implements AccountService {


    @Override
    @GET
    @Path("/{acctNumber}/balance")
    public BigDecimal getBalance(@PathParam("acctNumber") Long accountNumber) {
        return findAccount(accountNumber).getBalance();
    }

    @Override
    @POST
    @Path("{accountNumber}/transaction")
    @Transactional
    public void transact(@PathParam("accountNumber") Long accountNumber,
                         BigDecimal amount) {
        Account account = findAccount(accountNumber);
        if (account.getAccountStatus().equals(AccountStatus.OVERDRAWN)) {
            throw new WebApplicationException("Account is overdrawn, no further withdrawals permitted", 409);
        }
        account.addFunds(amount);
    }

    @Override
    public CompletionStage<Void> transactAsync(Long accountNumber, BigDecimal amount) {
        return CompletableFuture.runAsync(() -> this.transact(accountNumber, amount));
    }


    @POST
    @Path("{accountNumber}/transaction-headers")
    public Map<String, List<String>> transactionHeaders(@PathParam("accountNumber") Long
                                                                accountNumber, BigDecimal amount,
                                                        @Context HttpHeaders httpHeaders) { //@Context is equivalent to @Inject for CDI in the JAX-RS
        //returns the headers from the incoming call including the class and method level ones defined in the AcccountService interface
        return httpHeaders.getRequestHeaders();
    }


    private Account findAccount(Long accountNumber) {
        return Optional.ofNullable(accountRepository.findByAccountNumber(accountNumber))
                .orElseThrow(() -> new WebApplicationException("Account with " + accountNumber + " does not exist.", 404));

    }

}
