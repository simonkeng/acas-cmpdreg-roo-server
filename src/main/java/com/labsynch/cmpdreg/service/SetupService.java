package com.labsynch.cmpdreg.service;

import java.io.FileNotFoundException;

public interface SetupService {

	void loadCorpNames(String corpFileName) throws FileNotFoundException;

	void loadCorpNames() throws FileNotFoundException;


}
