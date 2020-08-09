package com.example.antonapi.model.projections;

import com.example.antonapi.model.Section;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "sectionExcerpt", types = Section.class)
public interface SectionExcerpt {
    String getName();
}
