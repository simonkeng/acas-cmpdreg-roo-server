// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.cmpdreg.dto;

import com.labsynch.cmpdreg.dto.FileSaveReturnDTO;
import org.springframework.web.multipart.MultipartFile;

privileged aspect FileSaveReturnDTO_Roo_JavaBean {
    
    public String FileSaveReturnDTO.getName() {
        return this.name;
    }
    
    public void FileSaveReturnDTO.setName(String name) {
        this.name = name;
    }
    
    public long FileSaveReturnDTO.getSize() {
        return this.size;
    }
    
    public void FileSaveReturnDTO.setSize(long size) {
        this.size = size;
    }
    
    public String FileSaveReturnDTO.getType() {
        return this.type;
    }
    
    public void FileSaveReturnDTO.setType(String type) {
        this.type = type;
    }
    
    public String FileSaveReturnDTO.getUrl() {
        return this.url;
    }
    
    public void FileSaveReturnDTO.setUrl(String url) {
        this.url = url;
    }
    
    public String FileSaveReturnDTO.getDescription() {
        return this.description;
    }
    
    public void FileSaveReturnDTO.setDescription(String description) {
        this.description = description;
    }
    
    public Boolean FileSaveReturnDTO.getUploaded() {
        return this.uploaded;
    }
    
    public void FileSaveReturnDTO.setUploaded(Boolean uploaded) {
        this.uploaded = uploaded;
    }
    
    public Boolean FileSaveReturnDTO.getIe() {
        return this.ie;
    }
    
    public void FileSaveReturnDTO.setIe(Boolean ie) {
        this.ie = ie;
    }
    
    public String FileSaveReturnDTO.getSubdir() {
        return this.subdir;
    }
    
    public void FileSaveReturnDTO.setSubdir(String subdir) {
        this.subdir = subdir;
    }
    
    public void FileSaveReturnDTO.setFile(MultipartFile file) {
        this.file = file;
    }
    
}
