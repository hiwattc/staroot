package com.staroot.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

public interface MessageRepository extends JpaRepository<Message, Long>{
	public List<Message> findAllByOrderByIdDesc();
	
	public List<Message> findTop2ByOrderByIdDesc();
}

/*
@Repository
public interface MessageRepository extends CrudRepository{
	
	@Query("select MSG_ID from message")
	List<?> getMessage();
}
*/