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

@Service
public interface BulkLoadService {


	public BulkLoadPropertiesDTO readSDFPropertiesFromFile(BulkLoadSDFPropertyRequestDTO requestDTO);

	BulkLoadTemplate saveBulkLoadTemplate(BulkLoadTemplate templateToSave);

	BulkLoadRegisterSDFResponseDTO registerSdf(BulkLoadRegisterSDFRequestDTO registerRequestDTO);

	public PurgeFileDependencyCheckResponseDTO checkPurgeFileDependencies(BulkLoadFile bulkLoadFile);

	public PurgeFileResponseDTO purgeFile(BulkLoadFile bulkLoadFile) throws MalformedURLException, IOException;


}
