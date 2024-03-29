package ru.owen.app.model.Cart;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartResponse {
    private String part_number;
    private String fullTitle;
    private int totalAmount;
    private double price;
}
