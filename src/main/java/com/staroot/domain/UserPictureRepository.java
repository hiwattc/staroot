package com.staroot.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPictureRepository extends JpaRepository<UserPicture,Long>{
	UserPicture findByUser(User user);
	void deleteByUser(User user);
}
