package com.example.antonapi.service.tools;

import com.example.antonapi.model.CartItem;
import com.example.antonapi.model.Unit;
import com.example.antonapi.repository.UnitRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;

public class SmartUnits {

    public static UnitRepository unitRepository;

    public static CartItem evaluateBestUnit(CartItem cartItem1, CartItem cartItem2){
        CartItem normalizedCartItem1 = SmartUnits.normalizeUnitAndQuantity(cartItem1);
        CartItem normalizedCartItem2 = SmartUnits.normalizeUnitAndQuantity(cartItem2);
        return SmartUnits.evaluateBestUnit(
                new CartItem(cartItem1.getId(),
                        cartItem1.getProduct(),
                        normalizedCartItem1.getUnit(),
                        normalizedCartItem1.getQuantity().add(normalizedCartItem2.getQuantity())));
    }

    private static CartItem normalizeUnitAndQuantity(CartItem cartItem){
        Unit cartItemUnit = cartItem.getUnit();
        BigDecimal evaluatedQuantity = cartItem.getQuantity()
                .multiply(BigDecimal.valueOf(cartItemUnit.getPrefix().getScale()), MathContext.UNLIMITED);
        Unit evaluatedUnit = cartItem.getUnit().getMasterUnit();
        return new CartItem(cartItem.getId(), cartItem.getProduct(), evaluatedUnit, evaluatedQuantity);
    }

    public static CartItem evaluateBestUnit(CartItem cartItem){
        List<Unit> availableUnits = unitRepository.findAllByBaseUnitIdOrderByPrefix_ScaleAsc(cartItem.getUnit().getBaseUnit().getId());
        CartItem normalizedCartItem = SmartUnits.normalizeUnitAndQuantity(cartItem);
        Unit evaluatedUnit = normalizedCartItem.getUnit();
        BigDecimal evaluatedQuantity = normalizedCartItem.getQuantity();
        for(Unit u : availableUnits){
            // Ideal value is >= 0.1
            if(evaluatedQuantity.compareTo(new BigDecimal("0.1")) >= 0){
                return new CartItem(cartItem.getId(), cartItem.getProduct(), evaluatedUnit, evaluatedQuantity);
            }
            evaluatedUnit = u;
            evaluatedQuantity = normalizedCartItem.getQuantity()
                    .divide(BigDecimal.valueOf(u.getPrefix().getScale()), MathContext.UNLIMITED);
        }
        return new CartItem(cartItem.getId(), cartItem.getProduct(), evaluatedUnit, evaluatedQuantity);
    }

    public void setUnitRepository(UnitRepository unitRepository){
        SmartUnits.unitRepository = unitRepository;
    }
}
