package com.labsynch.cmpdreg.service;

import com.labsynch.cmpdreg.domain.Lot;
import com.labsynch.cmpdreg.dto.LotDTO;


public interface LotService {

	public Lot updateLotWeight(Lot lot);

	Lot updateLotMeta(LotDTO lotDTO, String modifiedByUser);

	String updateLotMetaArray(String jsonArray, String modifiedByUser);

	Lot reparentLot(String lotCorpName, String parentCorpName, String modifiedByUser);


}
