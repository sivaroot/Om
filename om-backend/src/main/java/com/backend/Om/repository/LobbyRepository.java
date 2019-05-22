package com.backend.Om.repository;

import com.backend.Om.model.Lobby;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LobbyRepository extends JpaRepository<Lobby, Long> {
}
