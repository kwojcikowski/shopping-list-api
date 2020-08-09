package com.example.antonapi.controller;

import com.example.antonapi.model.CartItem;
import com.example.antonapi.repository.BaseUnitRepository;
import com.example.antonapi.repository.CartItemRepository;
import com.example.antonapi.repository.PrefixRepository;
import com.example.antonapi.repository.UnitRepository;
import com.example.antonapi.service.SmartUnits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.net.URI;

@RepositoryRestController
public class CartItemController {

    @Autowired
    CartItemRepository cartItemRepository;
    @Autowired
    UnitRepository unitRepository;

    @PostMapping(path = "/cartItems")
    public ResponseEntity<?> addCartItem(@RequestBody CartItem cartItemFromRequest){
        System.out.println(cartItemFromRequest);
        //Check whether cart entry with same product and unit exists, if so add quantities
        CartItem existing = cartItemRepository
                .findByProductAndUnit(cartItemFromRequest.getProduct(), cartItemFromRequest.getUnit());
        if(existing != null){
            return ResponseEntity.ok().body(SmartUnits.evaluateBestUnit(unitRepository, cartItemFromRequest));
        }
        CartItem cartItem = cartItemRepository.save(cartItemFromRequest);
        final URI uri =
                MvcUriComponentsBuilder.fromController(getClass())
                        .path("/{id}")
                        .buildAndExpand(cartItem.getId())
                        .toUri();
        return ResponseEntity.created(uri).body(cartItem);
    }
}
