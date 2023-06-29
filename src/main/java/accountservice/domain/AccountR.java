package accountservice.domain;

import accountservice.domain.AccountStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
public class AccountR {
    @Id
    @GeneratedValue
    private Long id;
    private Long accountNumber;
    private Long customerNumber;
    private String customerName;
    private BigDecimal balance;
    private AccountStatus accountStatus = AccountStatus.OPEN;

    public void withdrawFunds(BigDecimal amount) {
        balance = balance.subtract(amount);
    }

}
