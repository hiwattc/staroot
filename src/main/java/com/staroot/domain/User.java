package com.staroot.domain;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;


import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Cacheable(false)
public class User {
	@Id
	@GeneratedValue
	private Long id; 
	
	@Column(nullable=false, length=20)
	private String userId;
	private String password;
	private String name;
	private String email;
	
	//Cascade Issue : http://postitforhooney.tistory.com/entry/JavaJPAHibernate-CascadeType%EB%9E%80-%EA%B7%B8%EB%A6%AC%EA%B3%A0-%EC%A2%85%EB%A5%98
	@OneToOne(mappedBy="user" ,cascade = {CascadeType.ALL})
	private com.staroot.domain.UserPicture file;
	
	@JsonProperty
	private String dummy;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	public Long getId() {
		return id;
	}  
	
	public void update(User user, String password, com.staroot.domain.UserPicture file) {
	    this.name = user.getName();
	    this.email = user.getEmail();
	    if(file != null) this.file = file;
	    if(password != null && !password.equals("")){
		    this.password = password;
	    }
	}
	
	public boolean matchPassword(String inputPassword){
		if(inputPassword == null){
			return false;
		}
		return inputPassword.equals(this.password);
	}
	
	@Override
	public String toString() {
		return "User [userId=" + userId  + ", name=" + name + ", email=" + email + "]";
	}

	//이클립스 source > hash&equals 로 자동생성
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}

	//이클립스 source > hash&equals 로 자동생성
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}

	public UserPicture getFile() {
		return file;
	}

	public void setFile(UserPicture file) {
		this.file = file;
	}
	
	

}
