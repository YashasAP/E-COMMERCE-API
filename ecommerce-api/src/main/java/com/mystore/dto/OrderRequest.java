package com.mystore.dto;

import lombok.Data;

@Data
public class OrderRequest {
    // In this simple API, order is created directly from the user's cart.
    // This DTO could be used for more complex order creation scenarios.
}