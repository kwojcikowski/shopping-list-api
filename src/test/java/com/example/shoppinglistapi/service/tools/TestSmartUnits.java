package com.example.shoppinglistapi.service.tools;

import com.example.shoppinglistapi.model.CartItem;
import com.example.shoppinglistapi.model.Product;
import com.example.shoppinglistapi.model.unit.Unit;
import com.example.shoppinglistapi.service.tools.collector.CustomCollector;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;

public class TestSmartUnits {

    List<Unit> supportedUnits = List.of(Unit.values());

    @Test
    public void testEvaluateBestUnitSingleCartItem() {
        Product mockProduct = mock(Product.class);

        CartItem cartItemGrams = new CartItem(mockProduct, findUnitByAbbreviation("g"), new BigDecimal(1000));
        CartItem cartItemKilograms = new CartItem(mockProduct, findUnitByAbbreviation("kg"), new BigDecimal("0.01"));
        CartItem cartItemPieces = new CartItem(mockProduct, findUnitByAbbreviation("pcs"), new BigDecimal(2000));
        CartItem cartItemLiters = new CartItem(mockProduct, findUnitByAbbreviation("l"), new BigDecimal(3));
        CartItem cartItemMilliliters = new CartItem(mockProduct, findUnitByAbbreviation("ml"), new BigDecimal(300));

        CartItem resultGrams = SmartUnits.evaluateBestUnit(cartItemGrams);
        CartItem resultKilograms = SmartUnits.evaluateBestUnit(cartItemKilograms);
        CartItem resultPieces = SmartUnits.evaluateBestUnit(cartItemPieces);
        CartItem resultLiters = SmartUnits.evaluateBestUnit(cartItemLiters);
        CartItem resultMilliliters = SmartUnits.evaluateBestUnit(cartItemMilliliters);

        assertAll(() -> assertThat(tuple(resultGrams.getUnit().toString(), resultGrams.getQuantity().stripTrailingZeros()))
                        .isEqualTo(tuple("kg", new BigDecimal(1))),
                () -> assertThat(tuple(resultKilograms.getUnit().toString(), resultKilograms.getQuantity().stripTrailingZeros()))
                        .isEqualTo(tuple("g", new BigDecimal(10).stripTrailingZeros())),
                () -> assertThat(tuple(resultPieces.getUnit().toString(), resultPieces.getQuantity().stripTrailingZeros()))
                        .isEqualTo(tuple("pcs", new BigDecimal(2000).stripTrailingZeros())),
                () -> assertThat(tuple(resultLiters.getUnit().toString(), resultLiters.getQuantity().stripTrailingZeros()))
                        .isEqualTo(tuple("l", new BigDecimal(3).stripTrailingZeros())),
                () -> assertThat(tuple(resultMilliliters.getUnit().toString(), resultMilliliters.getQuantity().stripTrailingZeros()))
                        .isEqualTo(tuple("l", new BigDecimal("0.3").stripTrailingZeros()))
        );
    }

    private Unit findUnitByAbbreviation(String abbreviation) {
        return this.supportedUnits
                .stream()
                .filter(unit -> unit.toString().equals(abbreviation))
                .collect(CustomCollector.toSingleton());
    }

    @Test
    public void testEvaluateBestUnitTwoCartItems() {
        Product mockProduct = mock(Product.class);

        CartItem cartItemGrams = new CartItem(mockProduct, findUnitByAbbreviation("g"), new BigDecimal(200));
        CartItem cartItemGrams2 = new CartItem(mockProduct, findUnitByAbbreviation("g"), new BigDecimal(2000));
        CartItem cartItemKilograms = new CartItem(mockProduct, findUnitByAbbreviation("kg"), new BigDecimal(1));

        CartItem cartItemPieces = new CartItem(mockProduct, findUnitByAbbreviation("pcs"), new BigDecimal(3));
        CartItem cartItemPieces2 = new CartItem(mockProduct, findUnitByAbbreviation("pcs"), new BigDecimal(10));

        CartItem cartItemLiters = new CartItem(mockProduct, findUnitByAbbreviation("l"), new BigDecimal("0.01"));
        CartItem cartItemLiters2 = new CartItem(mockProduct, findUnitByAbbreviation("l"), new BigDecimal("0.02"));

        CartItem resultGrams = SmartUnits.evaluateBestUnit(cartItemGrams, cartItemKilograms);
        CartItem resultGrams2 = SmartUnits.evaluateBestUnit(cartItemGrams, cartItemGrams2);

        CartItem resultPieces = SmartUnits.evaluateBestUnit(cartItemPieces, cartItemPieces2);

        CartItem resultLiters = SmartUnits.evaluateBestUnit(cartItemLiters, cartItemLiters2);

        assertAll(() -> assertThat(tuple(resultGrams.getUnit().toString(), resultGrams.getQuantity().stripTrailingZeros()))
                        .isEqualTo(tuple("kg", new BigDecimal("1.2").stripTrailingZeros())),
                () -> assertThat(tuple(resultGrams2.getUnit().toString(), resultGrams2.getQuantity().stripTrailingZeros()))
                        .isEqualTo(tuple("kg", new BigDecimal("2.2").stripTrailingZeros())),
                () -> assertThat(tuple(resultPieces.getUnit().toString(), resultPieces.getQuantity().stripTrailingZeros()))
                        .isEqualTo(tuple("pcs", new BigDecimal(13).stripTrailingZeros())),
                () -> assertThat(tuple(resultLiters.getUnit().toString(), resultLiters.getQuantity().stripTrailingZeros()))
                        .isEqualTo(tuple("ml", new BigDecimal(30).stripTrailingZeros())));
    }
}
