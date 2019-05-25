package com.backend.Om.repository;

import com.backend.Om.model.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {
    List<Content> findContentsByLobby_LobbyNameOrderByMessageTime(String lobbyname);
}
