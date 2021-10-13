package com.blocknitive.com.service.mapper;

import com.blocknitive.com.domain.*;
import com.blocknitive.com.service.dto.NewsletterDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Newsletter} and its DTO {@link NewsletterDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface NewsletterMapper extends EntityMapper<NewsletterDTO, Newsletter> {}
