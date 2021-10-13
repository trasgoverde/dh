package com.blocknitive.com.service.mapper;

import com.blocknitive.com.domain.*;
import com.blocknitive.com.service.dto.ConfigVariablesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ConfigVariables} and its DTO {@link ConfigVariablesDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ConfigVariablesMapper extends EntityMapper<ConfigVariablesDTO, ConfigVariables> {}
