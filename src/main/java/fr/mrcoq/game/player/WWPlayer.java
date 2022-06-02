package fr.mrcoq.game.player;

import fr.mrcoq.game.player.role.Role;
import org.bukkit.entity.Player;

public class WWPlayer {


    private Role role;
    private Player player;

    public WWPlayer(Player player) {
        this.player = player;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Player getPlayer() {
        return player;
    }
}
