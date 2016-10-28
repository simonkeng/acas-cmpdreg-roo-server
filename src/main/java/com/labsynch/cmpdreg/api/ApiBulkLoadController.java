package com.labsynch.cmpdreg.api;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.labsynch.cmpdreg.domain.BulkLoadFile;
import com.labsynch.cmpdreg.domain.BulkLoadTemplate;
import com.labsynch.cmpdreg.dto.BulkLoadPropertiesDTO;
import com.labsynch.cmpdreg.dto.BulkLoadRegisterSDFResponseDTO;
import com.labsynch.cmpdreg.dto.BulkLoadSDFPropertyRequestDTO;
import com.labsynch.cmpdreg.dto.BulkLoadRegisterSDFRequestDTO;
import com.labsynch.cmpdreg.dto.PurgeFileDependencyCheckResponseDTO;
import com.labsynch.cmpdreg.dto.PurgeFileResponseDTO;
import com.labsynch.cmpdreg.service.BulkLoadService;

@RequestMapping(value = {"/api/v1/bulkload"})
@Controller
public class ApiBulkLoadController {
	
	Logger logger = LoggerFactory.getLogger(ApiBulkLoadController.class);
	
	@Autowired
	private BulkLoadService bulkLoadService;
	
	@RequestMapping(value = "/templates", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseEntity<String> getAllTemplates() {
		HttpHeaders headers= new HttpHeaders();
        headers.add("Content-Type", "application/text");
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
		try{
        	Collection<BulkLoadTemplate> templates = BulkLoadTemplate.findAllBulkLoadTemplates();
            return new ResponseEntity<String>(BulkLoadTemplate.toJsonArray(templates), headers, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<String>(e.toString(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
		
	@RequestMapping(value = "/templates/saveTemplate", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> saveOrUpdateTemplate(@RequestBody BulkLoadTemplate templateToSave) {
		HttpHeaders headers= new HttpHeaders();
        headers.add("Content-Type", "application/text");
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
		try{
        	BulkLoadTemplate savedTemplate = bulkLoadService.saveBulkLoadTemplate(templateToSave);
            return new ResponseEntity<String>(savedTemplate.toJson(), headers, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<String>(e.toString(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
	
	@RequestMapping(value = "/getSdfProperties", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> getSdfProperties(@RequestBody BulkLoadSDFPropertyRequestDTO requestDTO){
		HttpHeaders headers= new HttpHeaders();
        headers.add("Content-Type", "application/text");
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
		try{
        	BulkLoadPropertiesDTO responseDTO = bulkLoadService.readSDFPropertiesFromFile(requestDTO);
            return new ResponseEntity<String>(responseDTO.toJson(), headers, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<String>(e.toString(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
	}
	
//	@Transactional
	@RequestMapping(value = "/registerSdf", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> registerSdf(@RequestBody BulkLoadRegisterSDFRequestDTO requestDTO){
		HttpHeaders headers= new HttpHeaders();
        headers.add("Content-Type", "application/text");
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
		try{
        	BulkLoadRegisterSDFResponseDTO result = bulkLoadService.registerSdf(requestDTO);
            return new ResponseEntity<String>(result.toJson(), headers, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<String>(e.toString(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
	}
	
	@RequestMapping(value = "/files", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseEntity<String> getAllBulkLoadFiles() {
		HttpHeaders headers= new HttpHeaders();
        headers.add("Content-Type", "application/text");
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
		try{
        	Collection<BulkLoadFile> files = BulkLoadFile.findAllBulkLoadFiles();
            return new ResponseEntity<String>(BulkLoadFile.toJsonArray(files), headers, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<String>(e.toString(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
	
	@RequestMapping(value = "/checkDependencies", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> checkPurgeDependencies(@RequestBody BulkLoadFile bulkLoadFile) {
		HttpHeaders headers= new HttpHeaders();
        headers.add("Content-Type", "application/text");
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
		try{
        	BulkLoadFile fileToCheck = BulkLoadFile.findBulkLoadFile(bulkLoadFile.getId());
        	PurgeFileDependencyCheckResponseDTO responseDTO = bulkLoadService.checkPurgeFileDependencies(fileToCheck);
            return new ResponseEntity<String>(responseDTO.toJson(), headers, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<String>(e.toString(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
	
	@RequestMapping(value = "/purge", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> purgeFile(@RequestBody BulkLoadFile bulkLoadFile) {
		HttpHeaders headers= new HttpHeaders();
        headers.add("Content-Type", "application/text");
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
		try{
        	BulkLoadFile fileToPurge = BulkLoadFile.findBulkLoadFile(bulkLoadFile.getId());
        	PurgeFileResponseDTO responseDTO = bulkLoadService.purgeFile(fileToPurge);
            return new ResponseEntity<String>(responseDTO.toJson(), headers, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<String>(e.toString(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
