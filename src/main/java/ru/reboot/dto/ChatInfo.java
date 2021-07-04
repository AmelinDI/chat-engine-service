package ru.reboot.dto;

public class ChatInfo {

    private String companion;
    private String companionId;
    private int unreadMessagesCount;

    public String getCompanion() {
        return companion;
    }

    public void setCompanion(String companion) {
        this.companion = companion;
    }

    public String getCompanionId() {
        return companionId;
    }

    public void setCompanionId(String companionId) {
        this.companionId = companionId;
    }

    public int getUnreadMessagesCount() {
        return unreadMessagesCount;
    }

    public void setUnreadMessagesCount(int unreadMessagesCount) {
        this.unreadMessagesCount = unreadMessagesCount;
    }

    @Override
    public String toString() {
        return "ChatInfo{" +
                "companion='" + companion + '\'' +
                ", unreadMessagesCount=" + unreadMessagesCount +
                '}';
    }
}