package com.petstore.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private long id;            // Unique identifier of the order
    private long petId;         // Unique identifier of the pet
    private int quantity;       // Quantity of pets in the order
    private Date shipDate;      // Shipping date of the order
    private OrderStatus status; // Order status
    private boolean complete;   // Indicates if the order is complete

    public enum OrderStatus {
        PLACED,     // Order placed
        APPROVED,   // Order approved
        DELIVERED   // Order delivered
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", petId=" + petId +
                ", quantity=" + quantity +
                ", shipDate=" + shipDate +
                ", status=" + status +
                ", complete=" + complete +
                '}';
    }
}
