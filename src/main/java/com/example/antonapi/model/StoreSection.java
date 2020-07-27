package com.example.antonapi.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@IdClass(StoreSection.class)
@Table(name = "store_section")
public class StoreSection implements Serializable {

    @Id
    @ManyToOne
    private Store store;

    @Id
    @ManyToOne
    private Section section;

    @Id
    private Byte position;

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public Byte getPosition() {
        return position;
    }

    public void setPosition(Byte position) {
        this.position = position;
    }
}
