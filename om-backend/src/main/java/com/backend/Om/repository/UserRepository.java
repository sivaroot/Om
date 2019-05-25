package com.backend.Om.repository;

import com.backend.Om.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByNickname(String nickname);
    Boolean existsUserByNickname(String nickname);
}
