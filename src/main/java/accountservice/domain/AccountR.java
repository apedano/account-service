package accountservice.domain;

import accountservice.domain.AccountStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
public class AccountR extends Account {
    @Id
    @GeneratedValue
    private Long id;

}
