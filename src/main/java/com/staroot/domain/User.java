package com.staroot.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class User {
	@Id
	@GeneratedValue
	private Long id; 
	
	@Column(nullable=false, length=20)
	private String userId;
	private String password;
	private String name;
	private String email;
	
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
	
	public void update(User user) {
	    this.name = user.getName();
	    this.email = user.getEmail();
	    //this.password = user.getPassword();
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
	
	

}
