package net.musecom.comunity.service;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import net.musecom.comunity.model.FileDto;

@Service
public class FileUploadService implements FileUpload {
	
	private String[] allowedExt;
	private long maxSize;
	
	private String path;
	private String orPath;
	
	private String fileExt;
	private long fileSize;
	
	private FileDto fileDto = new FileDto();
	
	@Autowired
	private ServletContext sc;

	@Override
	public String[] getAllowedExt() {
		return allowedExt;
	}

	@Override
	public void setAllowedExt(String[] allowedExt) {
        this.allowedExt = allowedExt;
	}

	@Override
	public void setMaxSize(long maxSize) {
        this.maxSize = maxSize;
	}

	@Override
	public long getMaxSize() {
		return maxSize;
	}

	@Override
	public void setAbsolutePath(String path) {
		this.path =sc.getRealPath("/res/upload/") + path + "/";
		this.orPath = path;
	}

	@Override
	public String getAbsolutePath() {
		return path;
	}

	
	@Override
	public String getFileExt(String filename) {
		if(filename == null || filename.isEmpty()) {
			return "";
		}
		int dotIndex = filename.lastIndexOf(".");
		return (dotIndex != -1 && dotIndex < filename.length() - 1) ? filename.substring(dotIndex + 1) :"";
	}
	
	public FileDto uploadFile(MultipartFile file) throws IOException {
		
		fileExt = getFileExt(file.getOriginalFilename());
		fileSize = file.getSize();
		
		if(file == null || file.isEmpty()) {
			throw new IllegalArgumentException("에러가 발생했습니다.");
		}
		

		if(maxSize > 0 && file.getSize() > maxSize) {
			throw new IllegalArgumentException("전체 파일업로드 제한용량 " + maxSize + "를 초과했습니다.");
		}
		
		if(allowedExt != null && allowedExt.length > 0) {
			boolean isFileOk = false;
			for(String ext : allowedExt) {
				if(fileExt.equals(ext)) {
					isFileOk = true;
					break;
				}
			}
			
			if(!isFileOk) {
				throw new IllegalArgumentException("에러가 발생했습니다.");
			}
		}
		

		String orFilename = file.getOriginalFilename();
		String newFilename = System.currentTimeMillis() + "-"+ orPath +"." + fileExt;
		File dest = new File(path, newFilename);
		
		fileDto.setNewfilename(newFilename);
		fileDto.setOrfilename(orFilename);
		fileDto.setExt(fileExt);
		fileDto.setFilesize(fileSize);
 
		file.transferTo(dest);
		
		
		return fileDto;
	}

}
