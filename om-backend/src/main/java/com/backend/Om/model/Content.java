package com.backend.Om.model;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.Date;

@Entity
public class Content {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "content_id")
    private Long contentId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "lobby_id")
    private Lobby lobby;

    private ZonedDateTime messageTime;

    public Content() {

    }

    public Content(User user, Lobby lobby, ZonedDateTime messageTime, String content) {
        this.user = user;
        this.lobby = lobby;
        this.messageTime = messageTime;
        this.content = content;
    }

    private String content;

    public Long getContentId() {
        return contentId;
    }

    public void setContentId(Long contentId) {
        this.contentId = contentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public ZonedDateTime getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(ZonedDateTime messageTime) {
        this.messageTime = messageTime;
    }
}
