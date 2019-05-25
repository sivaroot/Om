package com.backend.Om.repository;

import com.backend.Om.model.Lobby;
import com.backend.Om.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LobbyRepository extends JpaRepository<Lobby, Long> {
    Boolean existsLobbyByLobbyName(String lobbyname);
    Lobby findLobbyByLobbyName(String lobbyname);
}
