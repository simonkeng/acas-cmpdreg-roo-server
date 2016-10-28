package com.labsynch.cmpdreg.service;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.cmpdreg.domain.Lot;
import com.labsynch.cmpdreg.domain.LotAlias;

@Service
public class LotAliasServiceImpl implements LotAliasService {

	Logger logger = LoggerFactory.getLogger(LotAliasServiceImpl.class);

	@Override
	@Transactional
	public Lot updateLotAliases(Lot lot) {
		Set<LotAlias> aliasesToBeSaved = lot.getLotAliases();
		logger.debug(LotAlias.toJsonArray(aliasesToBeSaved));
		Set<LotAlias> savedAliases = new HashSet<LotAlias>();
		if (aliasesToBeSaved != null && !aliasesToBeSaved.isEmpty()){
			for (LotAlias aliasToBeSaved : aliasesToBeSaved){
				logger.debug(aliasToBeSaved.toJson());
				aliasToBeSaved.setLot(lot);
				if (aliasToBeSaved.getId() == null) aliasToBeSaved.persist();
				else aliasToBeSaved.merge();
				savedAliases.add(aliasToBeSaved);
				logger.debug(aliasToBeSaved.toJson());
				logger.debug(LotAlias.toJsonArray(savedAliases));
			}
		}
		lot.setLotAliases(savedAliases);
		return lot;
	}

	@Override
	public Lot updateLotAliases(Lot lot,
			Set<LotAlias> lotAliases) {
		Set<LotAlias> aliasesToBeSaved = lotAliases;
		Set<LotAlias> savedAliases = new HashSet<LotAlias>();
		if (aliasesToBeSaved != null && !aliasesToBeSaved.isEmpty()){
			for (LotAlias aliasToBeSaved : aliasesToBeSaved){
				aliasToBeSaved.setLot(lot);
				if (aliasToBeSaved.getId() == null) aliasToBeSaved.persist();
				else aliasToBeSaved.merge();
				savedAliases.add(aliasToBeSaved);
			}
		}
		lot.setLotAliases(savedAliases);
		return lot;
	}

	
}

