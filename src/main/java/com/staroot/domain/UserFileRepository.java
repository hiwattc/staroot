package com.staroot.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserFileRepository extends JpaRepository<UserFile,Long>{
	List<UserFile> findByUser(User user);
	void deleteByUser(User user);
}
