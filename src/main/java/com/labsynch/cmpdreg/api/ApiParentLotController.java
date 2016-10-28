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
import org.springframework.web.bind.annotation.ResponseBody;

import com.labsynch.cmpdreg.dto.CodeTableDTO;
import com.labsynch.cmpdreg.dto.ParentLotCodeDTO;
import com.labsynch.cmpdreg.service.ParentLotService;

@RequestMapping(value = {"/api/v1/parentLot"})
@Controller
public class ApiParentLotController {
	
	Logger logger = LoggerFactory.getLogger(ApiParentLotController.class);
	
	@Autowired
	private ParentLotService parentLotService;

	@Transactional
	@RequestMapping(value = "/getLotsByParent", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> getParentByAliasName(@RequestParam String parentCorpName){
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		try{
			Collection<CodeTableDTO> codeTableLots = parentLotService.getCodeTableLotsByParentCorpName(parentCorpName);
			return new ResponseEntity<String>(CodeTableDTO.toJsonArray(codeTableLots), headers, HttpStatus.OK);
		}catch(Exception e){
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
	}
	
	@Transactional
	@RequestMapping(value = "/getLotsByParentAlias", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> getLotCodesByParentAlias(@RequestBody Collection<ParentLotCodeDTO> requestDTOs){
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		try{
			Collection<ParentLotCodeDTO> responseDTO = parentLotService.getLotCodesByParentAlias(requestDTOs);
			return new ResponseEntity<String>(ParentLotCodeDTO.toJsonArray(responseDTO), headers, HttpStatus.OK);
		}catch(Exception e){
			logger.error("Caught exception searching for lots by parent alias",e);
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
	}

}
