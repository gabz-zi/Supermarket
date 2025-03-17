package org.example.models;

import org.example.exceptions.InvalidCashierNameException;
import org.example.exceptions.InvalidCashierSalaryException;
import org.example.exceptions.InvalidProductNameException;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public class Cashier {
    private final String id;
    private final String name;
    private CashDesk assignedDesk;
    //not final as different cashiers may work on different cashDesks depending on certain working schedule
    //or there might be a need for a cashDesk to be closed for servicing
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

    @Override
    public boolean equals(Object o) { //we need to implement equals and hashCode as cashiers are stored into a set
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cashier cashier = (Cashier) o;
        return Objects.equals(id, cashier.id) && Objects.equals(name, cashier.name) && Objects.equals(assignedDesk, cashier.assignedDesk) && Objects.equals(salary, cashier.salary);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, assignedDesk, salary);
    }
}

