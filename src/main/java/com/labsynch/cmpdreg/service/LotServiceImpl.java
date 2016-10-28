package com.labsynch.cmpdreg.service;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.labsynch.cmpdreg.domain.Lot;
import com.labsynch.cmpdreg.domain.Scientist;
import com.labsynch.cmpdreg.dto.configuration.MainConfigDTO;
import com.labsynch.cmpdreg.utils.Configuration;
import com.labsynch.cmpdreg.utils.SecurityUtil;


@Service
public class LotServiceImpl implements LotService {

	@Autowired
	private ChemStructureService chemService;
	
	@Autowired
	private SaltFormService saltFormService;
		
	private static final MainConfigDTO mainConfig = Configuration.getConfigInfo();

	private static final Logger logger = LoggerFactory.getLogger(LotServiceImpl.class);

	@Override
	public Lot updateLotWeight(Lot lot) {
		saltFormService.updateSaltWeight(lot.getSaltForm());
		lot.setModifiedBy(SecurityUtil.getLoginUser());
		lot.setLotMolWeight(Lot.calculateLotMolWeight(lot));
		lot.setModifiedDate(new Date());
		lot.merge();
		logger.debug("updated lot weight for "+lot.getCorpName()+" to: "+lot.getLotMolWeight());
		return Lot.findLot(lot.getId());
	}
	


}
