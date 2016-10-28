package com.labsynch.cmpdreg.service;

import java.util.Set;

import com.labsynch.cmpdreg.domain.Parent;
import com.labsynch.cmpdreg.domain.ParentAlias;


public interface ParentAliasService {


	public Parent updateParentAliases(Parent parent);

	public Parent updateParentAliases(Parent parent,
			Set<ParentAlias> parentAliases);

}
