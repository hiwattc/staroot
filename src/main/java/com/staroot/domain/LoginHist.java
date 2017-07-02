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
public class LoginHist {
	@Id
	@GeneratedValue
	private String id;
	
	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name="fk_loginhist_user"))
	@JsonProperty
	private User user;
	
	private String ip;
	private String referer;
	private String requestURI;
	private String status;
	
	
	private LocalDateTime createDate;

	
	public String getFormattedCreateDate(){
		if(createDate == null){
			return "";
		}
		return createDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));
	}
	
	public LoginHist(){
	}
	
	public LoginHist(User user, String ip, String referer, String requestURI,String status){
		this.user = user;
		this.ip = ip;
		this.referer = referer;
		this.requestURI = requestURI;
		this.status = status;
		this.createDate = LocalDateTime.now();
		
	}
	
}
