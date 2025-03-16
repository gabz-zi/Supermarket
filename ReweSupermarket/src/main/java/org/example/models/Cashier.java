package org.example.models;

import java.math.BigDecimal;
import java.util.UUID;

public class Cashier {
    private final String id;
    private final String name;
    private CashDesk assignedDesk;
    private BigDecimal salary;
    //not final as different cashiers may work on different cashDesks depending on certain working schedule
    //or there might be a need for a cashDesk to be closed for

    public Cashier(String name, CashDesk assignedDesk, BigDecimal salary) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.assignedDesk = assignedDesk;
        this.salary = salary;
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
        this.salary = salary;
    }
}

