package com.example.antonapi.service;

import com.example.antonapi.model.CartItem;
import com.example.antonapi.model.Unit;
import com.example.antonapi.repository.UnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;

@Component
public class SmartUnits {

    public static CartItem evaluateBestUnit(UnitRepository unitRepository, CartItem cartItem){
        Unit cartItemUnit = cartItem.getUnit();
        System.out.println("Repo " + unitRepository);
        System.out.println("cart " + cartItem);
        List<Unit> availableUnits = unitRepository.findAllByBaseUnitId(cartItemUnit.getBaseUnit().getId());
        BigDecimal normalizedQuantity = cartItem.getQuantity()
                .divide(BigDecimal.valueOf(cartItemUnit.getPrefix().getScale()), MathContext.UNLIMITED);
        for(Unit u : availableUnits){
            if(normalizedQuantity.compareTo(new BigDecimal(0)) > 0
            && normalizedQuantity.compareTo(new BigDecimal(1)) < 0){
                return new CartItem(cartItem.getId(), cartItem.getProduct(), cartItemUnit, normalizedQuantity);
            }
            cartItemUnit = u;
            normalizedQuantity = normalizedQuantity.multiply(BigDecimal.valueOf(u.getPrefix().getScale()));
        }
        return new CartItem(cartItem.getId(), cartItem.getProduct(), cartItemUnit, normalizedQuantity);
    }
}
