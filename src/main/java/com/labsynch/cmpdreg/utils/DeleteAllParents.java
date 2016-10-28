package com.labsynch.cmpdreg.utils;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.cmpdreg.domain.Parent;

@Transactional
public class DeleteAllParents {
	static Logger logger = LoggerFactory.getLogger(DeleteAllParents.class);
	
	@Transactional
	public static void deleteAllParents(){
		List<Parent> parents = Parent.findAllParents();
		for (Parent parent:parents){
			parent.remove();
		}
	}
}
