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
@Cacheable(false)
public class Reply {
	@Id
	@GeneratedValue
	private String id;
	
	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name="fk_reply_writer"))
	@JsonProperty
	private User writer;
	
	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name="fk_reply_to_board"))
	private Board board;
	
	@Lob
	@JsonProperty
	private String contents;
	
	
	private LocalDateTime createDate;

	
	public String getFormattedCreateDate(){
		if(createDate == null){
			return "";
		}
		return createDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));
	}
	
	public Reply(){
	}
	
	public Reply(User writer,Board board, String contents){
		this.writer = writer;
		this.board = board;
		this.contents = contents;
		this.createDate = LocalDateTime.now();
		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Reply other = (Reply) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Answer [id=" + id + ", writer=" + writer + ", contents=" + contents + ", createDate=" + createDate
				+ "]";
	}
	
	
	
	
}
