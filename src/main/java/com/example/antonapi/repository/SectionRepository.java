package com.example.antonapi.repository;


import com.example.antonapi.model.Section;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class SectionRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private SectionJpaRepository sectionJpaRepository;

    public Section create(Section section){
        System.out.println("Adding section " + section.toString());
        return sectionJpaRepository.saveAndFlush(section);
    }
}
