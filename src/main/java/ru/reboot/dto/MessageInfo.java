package ru.reboot.dto;

import java.time.LocalDateTime;

public class MessageInfo {

    private String id;
    private String sender;
    private String recipient;
    private String content;
    private LocalDateTime messageTimestamp;
    private LocalDateTime lastAccessTime;
    private boolean wasRead;
    private LocalDateTime readTime;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getMessageTimestamp() {
        return messageTimestamp;
    }

    public void setMessageTimestamp(LocalDateTime messageTimestamp) {
        this.messageTimestamp = messageTimestamp;
    }

    public LocalDateTime getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(LocalDateTime lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    public boolean wasRead() {
        return wasRead;
    }

    public void setWasRead(boolean wasRead) {
        this.wasRead = wasRead;
    }

    public LocalDateTime getReadTime() {
        return readTime;
    }

    public void setReadTime(LocalDateTime readTime) {
        this.readTime = readTime;
    }

    @Override
    public String toString() {
        return "MessageInfo{" +
                "id='" + id + '\'' +
                ", sender='" + sender + '\'' +
                ", recipient='" + recipient + '\'' +
                ", content='" + content + '\'' +
                ", messageTimestamp=" + messageTimestamp +
                ", lastAccessTime=" + lastAccessTime +
                ", wasRead=" + wasRead +
                ", readTime=" + readTime +
                '}';
    }

    public static class Builder {
        private MessageInfo obj;

        public Builder() {
            obj = new MessageInfo();
        }

        public Builder setId(String id) {
            obj.id = id;
            return this;
        }

        public Builder setSender(String sender) {
            obj.sender = sender;
            return this;
        }

        public Builder setRecipient(String recipient) {
            obj.recipient = recipient;
            return this;
        }

        public Builder setContent(String content) {
            obj.content = content;
            return this;
        }

        public Builder setMessageTimestamp(LocalDateTime messageTimestamp) {
            obj.messageTimestamp = messageTimestamp;
            return this;
        }

        public Builder setLastAccessTime(LocalDateTime lastAccessTime) {
            obj.lastAccessTime = lastAccessTime;
            return this;
        }

        public Builder setWasRead(boolean wasRead) {
            obj.wasRead = wasRead;
            return this;
        }

        public Builder setReadTime(LocalDateTime readTime) {
            obj.readTime = readTime;
            return this;
        }

        public MessageInfo build() {
            return obj;
        }
    }
}