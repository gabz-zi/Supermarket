package org.example.models;

import org.example.exceptions.InvalidCashierNameException;
import org.example.exceptions.InvalidCashierSalaryException;
import org.example.exceptions.InvalidProductNameException;

import java.math.BigDecimal;
import java.util.UUID;

public class Cashier {
    private final String id;
    private final String name;
    private CashDesk assignedDesk;
    //not final as different cashiers may work on different cashDesks depending on certain working schedule
    //or there might be a need for a cashDesk to be closed for
    private BigDecimal salary;

    public Cashier(String name, CashDesk assignedDesk, BigDecimal salary) {
        this.id = UUID.randomUUID().toString();
        if (name == null || name.isBlank()) throw new InvalidCashierNameException("Cashier name cannot be empty.");
        this.name = name;
        this.assignedDesk = assignedDesk;
        this.setSalary(salary);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public CashDesk getAssignedDesk() {
        return assignedDesk;
    }

    public void setAssignedDesk(CashDesk assignedDesk) {
        this.assignedDesk = assignedDesk;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        if (salary.compareTo(BigDecimal.ZERO) != 1) {
            throw new InvalidCashierSalaryException("Cashier salary must be a positive number.");
        }
        this.salary = salary;
    }
}

