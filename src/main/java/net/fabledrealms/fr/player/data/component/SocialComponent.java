package net.fabledrealms.fr.player.data.component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SocialComponent {

    private List<UUID> friends = new ArrayList<>();
    private List<UUID> ignored = new ArrayList<>();

    public List<UUID> getFriends() {
        return friends;
    }

    public void setFriends(List<UUID> friends) {
        this.friends = friends;
    }

    public List<UUID> getIgnored() {
        return ignored;
    }

    public void setIgnored(List<UUID> ignored) {
        this.ignored = ignored;
    }
}
