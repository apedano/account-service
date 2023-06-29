package accountservice.repos;

import accountservice.domain.AccountR;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped //only one instace exists in the application
//The PanacheRepository contains all access methods as for JpaRepository in Spring Data Jpa
//All standard methods are all included
public class AccountRepository implements PanacheRepository<AccountR> {

    public AccountR findByAccountNumber(Long accountNumber) {
        return find("accountNumber = ?1", accountNumber).firstResult();
    }
}
