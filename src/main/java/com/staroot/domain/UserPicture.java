package com.staroot.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
//@Cacheable(false)
public class UserPicture {
	@Id
	@GeneratedValue
	private String id;
	
	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name="fk_pic_user"))
	@JsonProperty
	private User user;
	
	@JsonProperty
	private String origFileNm;
	private String chngFileNm;
	private String filePath;
	private Long fileSize;
	private LocalDateTime createDate;

	
	public String getFormattedCreateDate(){
		if(createDate == null){
			return "";
		}
		return createDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));
	}
	
	public UserPicture(){
	}
	
	public UserPicture(User user,String origFileNm, String chngFileNm, String filePath, Long fileSize){
		this.user = user;
		this.origFileNm = origFileNm;
		this.chngFileNm = chngFileNm;
		this.filePath = filePath;
		this.fileSize = fileSize;
		this.createDate = LocalDateTime.now();
		
	}
	
	public void update(UserPicture userPicFile){
		this.origFileNm = userPicFile.getOrigFileNm();
		this.chngFileNm = userPicFile.getChngFileNm();
		this.filePath   = userPicFile.getFilePath();
		this.fileSize   = userPicFile.getFileSize();
		this.createDate = LocalDateTime.now();
		
	}

	public String getOrigFileNm() {
		return origFileNm;
	}

	public void setOrigFileNm(String origFileNm) {
		this.origFileNm = origFileNm;
	}

	public String getChngFileNm() {
		return chngFileNm;
	}

	public void setChngFileNm(String chngFileNm) {
		this.chngFileNm = chngFileNm;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public LocalDateTime getCreateDate() {
		return createDate;
	}

	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
	}

	
}
