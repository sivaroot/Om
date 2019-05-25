package com.backend.Om.repository;

import com.backend.Om.model.Lobby;
import com.backend.Om.model.User;
import com.backend.Om.model.UserLobby;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserLobbyRepository extends JpaRepository<UserLobby,Long> {
    List<UserLobby> findUserLobbiesByLobby_LobbyNameOrderByActive(String lobbyname);
    Boolean existsUserLobbyByLobby_LobbyNameAndAndUser_Nickname(String lobbyname,String nickname);
    UserLobby findUserLobbyByUser_NicknameAndLobby_LobbyName(String lobbyname,String nickname);

}
