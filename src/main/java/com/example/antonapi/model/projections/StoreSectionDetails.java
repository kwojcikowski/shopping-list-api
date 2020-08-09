package com.example.antonapi.model.projections;

import com.example.antonapi.model.Section;
import com.example.antonapi.model.Store;
import com.example.antonapi.model.StoreSection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "storeSectionDetails", types = {StoreSection.class})
public interface StoreSectionDetails {
    @Value("#{target.section.id}")
    Long getSectionId();
    Integer getPosition();
}
