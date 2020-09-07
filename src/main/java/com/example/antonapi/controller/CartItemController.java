package com.example.antonapi.controller;

import com.example.antonapi.model.CartItem;
import com.example.antonapi.repository.CartItemRepository;
import com.example.antonapi.service.SmartUnits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.PersistentEntityResource;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RepositoryRestController
@CrossOrigin
public class CartItemController {

    @Autowired
    private CartItemRepository cartItemRepository;

    @PostMapping(path = "/cartItems")
    public ResponseEntity<EntityModel<PersistentEntityResource>> addCartItem(@RequestBody CartItem cartItemFromRequest, PersistentEntityResourceAssembler assembler){
        //Check whether cart entry with same product and unit exists, if so add quantities
        CartItem existing = cartItemRepository
                .findByProductAndUnit_BaseUnit(cartItemFromRequest.getProduct(), cartItemFromRequest.getUnit().getBaseUnit());
        CartItem itemToSave = existing != null
                ? SmartUnits.evaluateBestUnit(existing, cartItemFromRequest)
                : SmartUnits.evaluateBestUnit(cartItemFromRequest);
        CartItem cartItem = cartItemRepository.saveAndFlush(itemToSave);

        return ResponseEntity.ok(EntityModel.of(assembler.toModel(cartItem)));
    }

    @PatchMapping(path = "/cartItems")
    public ResponseEntity<CollectionModel<PersistentEntityResource>> updateCartItems(@RequestBody List<CartItem> cartItemsFromRequest, PersistentEntityResourceAssembler assembler) {
        cartItemRepository.saveAll(cartItemsFromRequest);
        cartItemRepository.flush();
        return ResponseEntity.ok(CollectionModel.of(assembler.toCollectionModel(cartItemRepository.findAll())));
    }
}
