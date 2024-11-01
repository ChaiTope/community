package net.musecom.comunity.service;

public interface FileUpload {
   
	String[] getAllowedExt();
   
 	void setAllowedExt(String[] ext);
	
	void setMaxSize(long maxSize);
	
	long getMaxSize();
	
	void setAbsolutePath(String path);
	
	String getAbsolutePath();
	
	String getFileExt(String filename);
}
