package com.labsynch.cmpdreg.dto;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;


@RooJavaBean
@RooToString
@RooJson
public class ContainerBatchCodeDTO {
	
	private String batchCode;
	
	private String containerCodeName;
	
	private String containerBarcode;
	
	private String wellCodeName;
	
	private String wellName;
		
	public ContainerBatchCodeDTO(){
	}
	
	public ContainerBatchCodeDTO(String batchCode, String containerCodeName, String containerBarcode, String wellCodeName, String wellName){
		this.batchCode = batchCode;
		this.containerCodeName = containerCodeName;
		this.containerBarcode = containerBarcode;
		this.wellCodeName = wellCodeName;
		this.wellName = wellName;
	}

}


