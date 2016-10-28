package com.labsynch.cmpdreg.service;

public interface StructureImageService {

	public String convertMolfilesToSDFile(String molfileJson);

	public byte[] convertMolToImage(String molStructure, Integer hSize, Integer wSize, String format);

	public byte[] displayImage(String molStructure);

}
