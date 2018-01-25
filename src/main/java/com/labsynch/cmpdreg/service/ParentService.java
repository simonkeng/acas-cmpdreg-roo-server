package com.labsynch.cmpdreg.service;

import java.io.IOException;
import java.util.Collection;
import java.util.List;


import com.labsynch.cmpdreg.domain.Parent;
import com.labsynch.cmpdreg.dto.CodeTableDTO;
import com.labsynch.cmpdreg.dto.ParentEditDTO;
import com.labsynch.cmpdreg.dto.ParentValidationDTO;
import com.labsynch.cmpdreg.exceptions.CmpdRegMolFormatException;
import com.labsynch.cmpdreg.exceptions.StandardizerException;



public interface ParentService {

	ParentValidationDTO validateUniqueParent(Parent queryParent) throws CmpdRegMolFormatException, StandardizerException;

	Collection<CodeTableDTO> updateParent(Parent parent);

	public int restandardizeAllParentStructures() throws CmpdRegMolFormatException, StandardizerException, IOException;
	Parent updateParentMeta(ParentEditDTO parentDTO, String modifiedByUser);

	void qcCheckParentStructures() throws CmpdRegMolFormatException, StandardizerException, IOException;

	void dupeCheckQCStructures() throws CmpdRegMolFormatException;

	int findPotentialDupeParentStructures(String dupeCheckFile);

	int findDupeParentStructures(String dupeCheckFile);

	int restandardizeParentStructures(List<Long> parentIds) throws CmpdRegMolFormatException, IOException, StandardizerException;

	int restandardizeParentStructsWithDisplayChanges() throws CmpdRegMolFormatException, IOException, StandardizerException;


	String updateParentMetaArray(String jsonInput, String modifiedByUser);


}
