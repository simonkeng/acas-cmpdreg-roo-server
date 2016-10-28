package com.labsynch.cmpdreg.service;

import java.util.ArrayList;
import java.util.Set;

import com.labsynch.cmpdreg.domain.IsoSalt;
import com.labsynch.cmpdreg.domain.Lot;
import com.labsynch.cmpdreg.domain.Parent;
import com.labsynch.cmpdreg.domain.SaltForm;
import com.labsynch.cmpdreg.exceptions.DupeSaltFormStructureException;
import com.labsynch.cmpdreg.exceptions.SaltFormMolFormatException;


public interface SaltFormService {

	public void updateSaltWeight(SaltForm saltForm);
	
	public SaltForm updateSaltForm(Parent parent, SaltForm saltForm, Set<IsoSalt> isoSalts, Lot lot, double totalSaltWeight, ArrayList<ErrorMessage> errors) throws SaltFormMolFormatException, DupeSaltFormStructureException;


}
