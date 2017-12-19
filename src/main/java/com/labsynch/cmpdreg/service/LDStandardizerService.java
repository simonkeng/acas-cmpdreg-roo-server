package com.labsynch.cmpdreg.service;

import java.io.IOException;
import java.net.MalformedURLException;

import org.springframework.stereotype.Service;

import com.labsynch.cmpdreg.domain.BulkLoadFile;
import com.labsynch.cmpdreg.domain.BulkLoadTemplate;
import com.labsynch.cmpdreg.dto.BulkLoadPropertiesDTO;
import com.labsynch.cmpdreg.dto.BulkLoadRegisterSDFRequestDTO;
import com.labsynch.cmpdreg.dto.BulkLoadRegisterSDFResponseDTO;
import com.labsynch.cmpdreg.dto.BulkLoadSDFPropertyRequestDTO;
import com.labsynch.cmpdreg.dto.PurgeFileDependencyCheckResponseDTO;
import com.labsynch.cmpdreg.dto.PurgeFileResponseDTO;
import com.labsynch.cmpdreg.exceptions.CmpdRegMolFormatException;

@Service
public interface LDStandardizerService {

	public String standardizeStructure(String molfile) throws CmpdRegMolFormatException, IOException;

}
