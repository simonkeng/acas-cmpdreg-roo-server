package com.labsynch.cmpdreg.service;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.cmpdreg.domain.Parent;
import com.labsynch.cmpdreg.domain.ParentAlias;

@Service
public class ParentAliasServiceImpl implements ParentAliasService {

	Logger logger = LoggerFactory.getLogger(ParentAliasServiceImpl.class);

	@Override
	@Transactional
	public Parent updateParentAliases(Parent parent) {
		Set<ParentAlias> aliasesToBeSaved = parent.getParentAliases();
		logger.debug(ParentAlias.toJsonArray(aliasesToBeSaved));
		Set<ParentAlias> savedAliases = new HashSet<ParentAlias>();
		if (aliasesToBeSaved != null && !aliasesToBeSaved.isEmpty()){
			for (ParentAlias aliasToBeSaved : aliasesToBeSaved){
				logger.debug(aliasToBeSaved.toJson());
				aliasToBeSaved.setParent(parent);
				if (aliasToBeSaved.getId() == null) aliasToBeSaved.persist();
				else aliasToBeSaved.merge();
				savedAliases.add(aliasToBeSaved);
				logger.debug(aliasToBeSaved.toJson());
				logger.debug(ParentAlias.toJsonArray(savedAliases));
			}
		}
		parent.setParentAliases(savedAliases);
		return parent;
	}

	@Override
	public Parent updateParentAliases(Parent parent,
			Set<ParentAlias> parentAliases) {
		Set<ParentAlias> aliasesToBeSaved = parentAliases;
		Set<ParentAlias> savedAliases = new HashSet<ParentAlias>();
		if (aliasesToBeSaved != null && !aliasesToBeSaved.isEmpty()){
			for (ParentAlias aliasToBeSaved : aliasesToBeSaved){
				aliasToBeSaved.setParent(parent);
				if (aliasToBeSaved.getId() == null) aliasToBeSaved.persist();
				else aliasToBeSaved.merge();
				savedAliases.add(aliasToBeSaved);
			}
		}
		parent.setParentAliases(savedAliases);
		return parent;
	}

	
}

