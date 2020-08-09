package com.example.antonapi.model;

import com.example.antonapi.deserializer.ProductDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
//@JsonDeserialize(using = ProductDeserializer.class)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    private Long id;

    @NonNull
    private String name;

    @ManyToOne
    @NonNull
    private Unit defaultUnit;

    @ManyToOne
    @NonNull
    private Section section;
}
