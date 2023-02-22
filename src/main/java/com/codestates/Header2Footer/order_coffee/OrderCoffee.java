package com.codestates.Header2Footer.order_coffee;

import com.codestates.Header2Footer.audit.Auditable;
import com.codestates.Header2Footer.coffee.Coffee;
import com.codestates.Header2Footer.order.Order;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class OrderCoffee extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderCoffeeId;

    @Column(nullable = false)
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "ORDER_ID")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "COFFEE_ID")
    private Coffee coffee;

    public void addOrder(Order order) {
        this.order = order;
        if (!this.order.getOrderCoffees().contains(this)) {
            this.order.getOrderCoffees().add(this);
        }
    }

    public void addCoffee(Coffee coffee) {
        this.coffee = coffee;
        if (!this.coffee.getOrderCoffees().contains(this)) {
            this.coffee.addOrderCoffee(this);
        }
    }
}
