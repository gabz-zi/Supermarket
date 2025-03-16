package org.example.services;

import org.example.exceptions.CashierNotRealWorkerInShopException;
import org.example.exceptions.CustomerNotInQueueException;
import org.example.exceptions.InsufficientFundsException;
import org.example.models.*;

import java.math.BigDecimal;

public class CashierService {
    private final ReceiptService receiptService;
    private final ShopService shopService;
    private final ProductService productService;
    private final CustomerService customerService;
    private final CashDeskService cashDeskService;

    public CashierService(ReceiptService receiptService, ShopService shopService, ProductService productService, CustomerService customerService, CashDeskService cashDeskService) {
        this.receiptService = receiptService;
        this.shopService = shopService;
        this.productService = productService;
        this.customerService = customerService;
        this.cashDeskService = cashDeskService;
    }

    public Receipt processPurchase(Customer customer, Cashier cashier, Shop shop) {
        CashDesk cashDesk = cashier.getAssignedDesk();
        if (!cashDesk.getQueue().contains(customer)) {
            throw new CustomerNotInQueueException("Customer is not in the queue");
        }
        if (!shopService.getCashiers(shop).contains(cashier)) {
            throw new CashierNotRealWorkerInShopException("Cashier isn't a worker in that shop.");
        }
        BigDecimal total = receiptService.calculateTotal(shop, customer.getBasket());
        if (!customerService.canAfford(customer, total)) {
            throw new InsufficientFundsException("Customer has insufficient funds.");
        }
        customerService.deduct(customer, total);
        customer.getBasket().forEach((product, quantity) -> shopService.sellProduct(shop, product, quantity));
        cashDeskService.checkout(cashDesk, customer);
        Receipt issuedReceipt = receiptService.createReceipt(shop, customer, cashier);
        customer.getBasket().clear();
        return issuedReceipt;
    }
}

