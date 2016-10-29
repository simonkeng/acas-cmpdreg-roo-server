package com.labsynch.cmpdreg.service;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import com.labsynch.cmpdreg.domain.Parent;
import com.labsynch.cmpdreg.dto.CodeTableDTO;
import com.labsynch.cmpdreg.dto.ParentValidationDTO;

import chemaxon.formats.MolFormatException;



public interface ParentService {

	ParentValidationDTO validateUniqueParent(Parent queryParent) throws MolFormatException;

	Collection<CodeTableDTO> updateParent(Parent parent);

	public int restandardizeAllParentStructures() throws MolFormatException, IOException;

	void qcCheckParentStructures() throws MolFormatException, IOException;

	void dupeCheckQCStructures();

	int findPotentialDupeParentStructures(String dupeCheckFile);

	int findDupeParentStructures(String dupeCheckFile);

	int restandardizeParentStructures(List<Long> parentIds) throws MolFormatException, IOException;

	int restandardizeParentStructsWithDisplayChanges() throws MolFormatException, IOException;


}
