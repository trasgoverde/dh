package com.blocknitive.com.service.mapper;

import com.blocknitive.com.domain.*;
import com.blocknitive.com.service.dto.FrontpageconfigDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Frontpageconfig} and its DTO {@link FrontpageconfigDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FrontpageconfigMapper extends EntityMapper<FrontpageconfigDTO, Frontpageconfig> {}
