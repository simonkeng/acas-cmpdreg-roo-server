package com.labsynch.cmpdreg.service;

import java.util.Collection;

import com.labsynch.cmpdreg.domain.Project;
import com.labsynch.cmpdreg.dto.SearchFormReturnDTO;


public interface ProjectService {

	Collection<Project> getACASProjectsByUser(String userName);

	SearchFormReturnDTO filterSearchResultsByProject(
			SearchFormReturnDTO searchResults, String loggedInUser);



}
