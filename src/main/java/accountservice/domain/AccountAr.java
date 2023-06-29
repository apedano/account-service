package accountservice.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * The PanacheEntity fro Active record will add the ID field to all subclasses
 * PanacheEntity also implements PanacheEntityBase which contains all the generic business methods like find...
 */
@Entity
public class AccountAr extends PanacheEntity {
    //All fields must be public Panache will generate all getter and setter methods at build time
    // replacing fields access modifier
    public Long accountNumber;
    public Long customerNumber;
    public String customerName;
    public BigDecimal balance;
    public AccountStatus accountStatus = AccountStatus.OPEN;


    public static long totalAccountsForCustomer(Long customerNumber) {
        return find("customerNumber", customerNumber).count();
    }
    public static AccountAr findByAccountNumber(Long accountNumber) {
        return find("accountNumber", accountNumber).firstResult();
    }

    public void withdrawFunds(BigDecimal amount) {
        balance = balance.subtract(amount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountAr accountAr = (AccountAr) o;
        return Objects.equals(accountNumber, accountAr.accountNumber) && Objects.equals(customerNumber, accountAr.customerNumber) && Objects.equals(customerName, accountAr.customerName) && Objects.equals(balance, accountAr.balance) && accountStatus == accountAr.accountStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber, customerNumber, customerName, balance, accountStatus);
    }
}
