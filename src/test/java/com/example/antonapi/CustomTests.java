package com.example.antonapi;

import com.example.antonapi.model.*;
import com.example.antonapi.service.SmartUnits;

import java.math.BigDecimal;

public class CustomTests {
    public static void main(String[] args) {
        Unit mililiters = new Unit(1L,
                new BaseUnit(1L, "l", "LITERS"),
                new Prefix(1L, "m", "MILI", 0.001));

        Section bakes = new Section(1L, "BAKERY");
        Product milk = new Product(1L, "mleko", mililiters, bakes);
        CartItem entry = new CartItem(1L, milk, mililiters,new BigDecimal(500));

//        System.out.println(SmartUnits.evaluateBestUnit(entry));
    }
}
