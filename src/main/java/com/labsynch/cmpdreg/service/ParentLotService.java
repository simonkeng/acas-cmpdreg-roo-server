package com.labsynch.cmpdreg.service;

import java.util.Collection;

import javax.persistence.NoResultException;

import com.labsynch.cmpdreg.domain.Lot;
import com.labsynch.cmpdreg.dto.CodeTableDTO;
import com.labsynch.cmpdreg.dto.ParentLotCodeDTO;


public interface ParentLotService {


	public Collection<CodeTableDTO> getCodeTableLotsByParentCorpName(String parentCorpName);

	public Collection<ParentLotCodeDTO> getLotCodesByParentAlias(Collection<ParentLotCodeDTO> requestDTOs);

	public Collection<Lot> getLotsByParentCorpName(String parentCorpName);

}
