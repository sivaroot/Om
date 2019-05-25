package com.backend.Om.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
//@Table(name = "lobbies")
public class Lobby {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lobby_id")
    private Long lobbyId;
    private String lobbyName;
//
//    @OneToMany(mappedBy = "lobby")
//    private Set<UserLobby> userLobbies;


    public Lobby() {
    }

    public Lobby(String lobbyName) {
        this.lobbyName = lobbyName;
    }

    public Long getLobbyId() {
        return lobbyId;
    }

    public void setLobbyId(Long lobbyId) {
        this.lobbyId = lobbyId;
    }

    public String getLobbyName() {
        return lobbyName;
    }

    public void setLobbyName(String lobbyName) {
        this.lobbyName = lobbyName;
    }


}
