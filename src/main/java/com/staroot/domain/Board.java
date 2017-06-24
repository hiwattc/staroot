package com.staroot.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

@Entity
public class Board {
	@Id
	@GeneratedValue
	//@OrderBy("id DESC") //잘안됨 
	private Long id; 
	private String title;
	
	@Lob
	private String contents;
	
	@OneToMany(mappedBy="board")
	@OrderBy("id DESC")
	private List<Reply> reply;
	
	
	@ManyToOne
	//@JoinColumn(foreignKey=@ForeignKey(name="fk_board_writer"))
	@JoinColumn(name="userId")
	private User writer;
	
	private LocalDateTime createDate;
	private LocalDateTime modifyDate;
	
	public Board(){}
	
	public Board(User writer, String title, String contents){
		this.writer = writer;
		this.title = title;
		this.contents = contents;
		this.createDate = LocalDateTime.now();
		this.modifyDate = LocalDateTime.now();
	}
	
	public String getFormattedCreateDate(){
		if(createDate == null){
			return "";
		}
		return createDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
	}

	public String getFormattedModifyDate(){
		if(modifyDate == null){
			return "";
		}
		return modifyDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	
	@Override
	public String toString() {
		return "Board [id=" + id + ", title=" + title + ", contents=" + contents + ", writer=" + writer
				+ ", createDate=" + createDate + ", modifyDate=" + modifyDate + "]";
	}

	public User getWriter() {
		return writer;
	}

	public void setWriter(User writer) {
		this.writer = writer;
	}

	public boolean isSameWriter(User writer) {
		if(writer == null){
			return false;
		}
		return this.writer.equals(writer);
	}

	public void update(String title, String contents) {
		this.title = title;
		this.contents = contents;
	}
}
