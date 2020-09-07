package com.example.antonapi.model.projections;

import com.example.antonapi.model.BaseUnit;
import com.example.antonapi.model.Prefix;
import com.example.antonapi.model.Unit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "unitExcerpt", types = {Unit.class})
public interface UnitExcerpt {
    @Value("#{target.toString()}")
    String getAbbreviation();
    Prefix getPrefix();
    BaseUnit getBaseUnit();
    Unit getMasterUnit();
}
