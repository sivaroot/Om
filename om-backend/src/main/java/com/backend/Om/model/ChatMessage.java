package com.backend.Om.model;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;


public class ChatMessage {

  public enum MessageType {
    CHAT, JOIN, LEAVE, CHECK
  }

  private MessageType messageType;
  private String content;
  private String sender;

  private ZonedDateTime messageTime;

  public MessageType getType() {
    return messageType;
  }

  public void setType(MessageType messageType) {
    this.messageType = messageType;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getSender() {
    return sender;
  }

  public void setSender(String sender) {
    this.sender = sender;
  }

  public MessageType getMessageType() {
    return messageType;
  }

  public void setMessageType(MessageType messageType) {
    this.messageType = messageType;
  }

  public ZonedDateTime getMessageTime() {
    return messageTime;
  }

  public void setMessageTime(ZonedDateTime messageTime) {
    this.messageTime = messageTime;
  }
}
