package com.backend.Om.model;

import javax.persistence.*;

@Entity
public class UserLobby {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_lobby_id")
    private Long userLobbyId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "lobby_id")
    private Lobby lobby;


    private Boolean active = false;


    public UserLobby() {
    }

    public UserLobby(User user, Lobby lobby) {
        this.user = user;
        this.lobby = lobby;
    }

    public Long getUserLobbyId() {
        return userLobbyId;
    }

    public void setUserLobbyId(Long userLobbyId) {
        this.userLobbyId = userLobbyId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Lobby getLobby() {
        return lobby;
    }

    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
