package com.gabrieldev.data.vo.v1;

import java.io.Serializable;

public class UploadFileResponseVO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String fileName;
	private String fileDownloadUri;
	private String fileType;
	private long fileSize;
	
	public UploadFileResponseVO() {};
	
	public UploadFileResponseVO(String fileName, String fileDownloadUri, String fileType, long fileSize) {
		this.fileName = fileName;
		this.fileDownloadUri = fileDownloadUri;
		this.fileType = fileType;
		this.fileSize = fileSize;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileDownloadUri() {
		return fileDownloadUri;
	}

	public void setFileDownloadUri(String fileDownloadUri) {
		this.fileDownloadUri = fileDownloadUri;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}
	
	

}