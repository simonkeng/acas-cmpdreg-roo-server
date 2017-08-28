package com.labsynch.cmpdreg.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
public class CodeTableDTO {

	private String code;

	private String name;

	private String comments;

	public static void sortCodeTableByName(ArrayList<CodeTableDTO> codeTableArray){
		Collections.sort(codeTableArray, new Comparator<CodeTableDTO>() {
		    public int compare(CodeTableDTO codeTable1, CodeTableDTO codeTable2) {
		        return codeTable1.name.toUpperCase().compareTo(codeTable2.name.toUpperCase());
		    }
		});
	}
}
