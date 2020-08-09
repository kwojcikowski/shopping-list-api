package com.example.antonapi.service;

import com.example.antonapi.model.*;
import com.example.antonapi.repository.*;
import lombok.Getter;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
@Getter
public class RepositoryService {

    @Resource
    private BaseUnitRepository baseUnitRepository;
    @Resource
    private CartItemRepository cartItemRepository;
    @Resource
    private PrefixRepository prefixRepository;
    @Resource
    private ProductRepository productRepository;
    @Resource
    private SectionRepository sectionRepository;
    @Resource
    private StoreRepository storeRepository;
    @Resource
    private StoreSectionRepository storeSectionRepository;
    @Resource
    private UnitRepository unitRepository;

    @Getter
    private static final Map<Class<?>, Repository<?,?>> repos = new HashMap<>();

    @PostConstruct
    private void postConstruct() {
        repos.put(BaseUnit.class, baseUnitRepository);
        repos.put(CartItem.class, cartItemRepository);
        repos.put(Prefix.class, prefixRepository);
        repos.put(Product.class, productRepository);
        repos.put(Section.class, sectionRepository);
        repos.put(Store.class, storeRepository);
        repos.put(StoreSection.class, storeSectionRepository);
        repos.put(Unit.class, unitRepository);
    }
}
