package com.labsynch.cmpdreg.service;

import java.util.Set;

import com.labsynch.cmpdreg.domain.Lot;
import com.labsynch.cmpdreg.domain.LotAlias;


public interface LotAliasService {


	public Lot updateLotAliases(Lot lot);

	public Lot updateLotAliases(Lot lot,
			Set<LotAlias> lotAliases);

}
