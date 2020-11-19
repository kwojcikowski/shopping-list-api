package com.example.antonapi.service.tools;

import com.example.antonapi.model.CartItem;
import com.example.antonapi.model.Unit;
import com.example.antonapi.repository.UnitRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.util.Pair;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;
import java.util.stream.Collectors;

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
        Unit evaluatedUnit = cartItem.getUnit();
        while(evaluatedUnit.getMasterUnit() != null)
            evaluatedUnit = evaluatedUnit.getMasterUnit();
        return new CartItem(cartItem.getId(), cartItem.getProduct(), evaluatedUnit, evaluatedQuantity);
    }

    public static CartItem evaluateBestUnit(CartItem cartItem){
        List<Unit> availableUnits = unitRepository.findAllByBaseUnitIdOrderByPrefix_ScaleAsc(cartItem.getUnit().getBaseUnit().getId());
        CartItem normalizedCartItem = SmartUnits.normalizeUnitAndQuantity(cartItem);
        List<Pair<Unit, BigDecimal>> unitRepresentationOptions = availableUnits.stream()
                .map(u -> Pair.of(u, normalizedCartItem.getQuantity()
                        .divide(BigDecimal.valueOf(u.getPrefix().getScale()), MathContext.UNLIMITED)))
                .sorted(Comparator.comparing(Pair::getSecond)).collect(Collectors.toList());

        Pair<Unit, BigDecimal> finestUnitRepresentation = getTheFinestUnitRepresentationFromList(unitRepresentationOptions);
        return new CartItem(cartItem.getId(), cartItem.getProduct(),
                finestUnitRepresentation.getFirst(), finestUnitRepresentation.getSecond());
    }

    private static Pair<Unit, BigDecimal> getTheFinestUnitRepresentationFromList(List<Pair<Unit, BigDecimal>> options){
        //Get first value >= 0.1 if such value does not exist, take the highest (in this case last list element)
        for(Pair<Unit, BigDecimal> pair : options)
            if(pair.getSecond().compareTo(new BigDecimal("0.1")) >= 0)
                return pair;
        return options.get(options.size() - 1);
    }

    public void setUnitRepository(UnitRepository unitRepository){
        SmartUnits.unitRepository = unitRepository;
    }
}
