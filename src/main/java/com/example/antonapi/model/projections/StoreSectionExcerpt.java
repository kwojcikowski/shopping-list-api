package com.example.antonapi.model.projections;

import com.example.antonapi.model.StoreSection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "storeSectionExcerpt", types = {StoreSection.class})
public interface StoreSectionExcerpt {
    SectionExcerpt getSection();
    Integer getPosition();
}
