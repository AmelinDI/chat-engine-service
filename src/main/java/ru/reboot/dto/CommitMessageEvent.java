package ru.reboot.dto;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class CommitMessageEvent {

    public static final String TOPIC = "COMMIT_MESSAGE_EVENT";

    private Set<String> messageIds;

    public CommitMessageEvent() {
    }

    public CommitMessageEvent(Collection<String> messageIds) {
        setMessageIds(messageIds);
    }

    public Collection<String> getMessageIds() {
        if (Objects.isNull(messageIds)) {
            messageIds = new HashSet<>();
        }
        return messageIds;
    }

    public void setMessageIds(Collection<String> messageIds) {
        this.messageIds = new HashSet<>(messageIds);
    }

    @Override
    public String toString() {
        return "CommitMessageEvent{" +
                "messageIds=" + messageIds +
                '}';
    }
}
