package com.labsynch.cmpdreg.service;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import chemaxon.formats.MolFormatException;

import com.labsynch.cmpdreg.domain.Parent;
import com.labsynch.cmpdreg.dto.CodeTableDTO;
import com.labsynch.cmpdreg.dto.ParentDTO;
import com.labsynch.cmpdreg.dto.ParentEditDTO;
import com.labsynch.cmpdreg.dto.ParentValidationDTO;
import com.labsynch.cmpdreg.exceptions.MissingPropertyException;

import chemaxon.formats.MolFormatException;



public interface ParentService {

	ParentValidationDTO validateUniqueParent(Parent queryParent) throws MolFormatException;

	Collection<CodeTableDTO> updateParent(Parent parent);

	public int restandardizeAllParentStructures() throws MolFormatException, IOException;
	Parent updateParentMeta(ParentEditDTO parentDTO, String modifiedByUser);

	void qcCheckParentStructures() throws MolFormatException, IOException;

	void dupeCheckQCStructures();

	int findPotentialDupeParentStructures(String dupeCheckFile);

	int findDupeParentStructures(String dupeCheckFile);

	int restandardizeParentStructures(List<Long> parentIds) throws MolFormatException, IOException;

	int restandardizeParentStructsWithDisplayChanges() throws MolFormatException, IOException;


	String updateParentMetaArray(String jsonInput, String modifiedByUser);


}
