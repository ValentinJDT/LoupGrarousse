package fr.mrcoq.game;

import fr.mrcoq.LoupGarousse;
import fr.mrcoq.game.player.WWPlayer;
import fr.mrcoq.game.player.role.Role;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Game {

    private boolean started = false;
    private boolean night = false;

    private List<WWPlayer> wwPlayers = new ArrayList<>();
    private List<WWPlayer> deadPlayers = new ArrayList<>();

    private Role actualRole = Role.VILLAGER;

    private int day = 0;
    private int minPlayers = 5;
    private int maxPlayers = 12;

    public Game() {

    }

    public void start() {
        if(started) {
            return;
        }

        this.day = 0;
        this.started = true;

        while(started) {
            if(this.getRoles().size() < 2) {
                this.started = false;
                continue;
            }

            for(Role role : this.getRoles()) {
                this.actualRole = role;
                if(role == Role.VILLAGER) {
                    this.setCycle(false);
                    this.broadcastMessage("§7Il est l'heure de se réveiller");
                } else if(!this.night) {
                    setCycle(true);
                    this.broadcastMessage("§7Il est l'heure de se coucher");
                }

                Bukkit.getScheduler().scheduleSyncRepeatingTask(LoupGarousse.getInstance(), () -> {
                    List<WWPlayer> actualWWPlayers = getPlayersByRole(role);


                }, 0, 20L * role.getTimePlay());
            }
        }


    }

    public boolean kill(Player player) {

        if(!this.started) {
            return false;
        }

        Optional<WWPlayer> canKill = wwPlayers.stream().filter(wwPlayer -> wwPlayer.getPlayer().getName().equals(player.getName())).findFirst();

        canKill.ifPresent(wwPlayer -> {
            deadPlayers.add(wwPlayer);
            wwPlayers.remove(wwPlayer);
        });

        return canKill.isPresent();
    }

    public boolean join(Player player) {
        boolean canJoin = wwPlayers.stream().noneMatch(wwPlayer -> wwPlayer.getPlayer().getName().equals(player.getName()));

        if(canJoin) {
            wwPlayers.add(new WWPlayer(player));
        }

        return canJoin;
    }

    public boolean leave(Player player) {
        Optional<WWPlayer> canLeave = wwPlayers.stream().filter(wwPlayer -> wwPlayer.getPlayer().getName().equals(player.getName())).findFirst();

        canLeave.ifPresent(wwPlayer -> wwPlayers.remove(wwPlayer));

        return canLeave.isPresent();
    }

    public void addDay() {
        this.day++;
    }

    public int getDay() {
        return this.day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public WWPlayer getWWPlayer(Player player) {
        return wwPlayers.stream().filter(wwPlayer -> player.getName().equals(wwPlayer.getPlayer().getName())).findFirst().orElse(null);
    }

    public WWPlayer getWWPlayer(String playerName) {
        return wwPlayers.stream().filter(wwPlayer -> playerName.equals(wwPlayer.getPlayer().getName())).findFirst().orElse(null);
    }

    public List<WWPlayer> getPlayersByRole(Role role) {
        return wwPlayers.stream().filter(wwPlayer -> wwPlayer.getRole() == role).collect(Collectors.toList());
    }

    public List<Role> getRoles() {
        return wwPlayers.stream().map(WWPlayer::getRole).sorted(Comparator.comparingInt(Role::getOrder)).distinct().toList();
    }

    public List<WWPlayer> getWwPlayers() {
        return wwPlayers;
    }

    public void broadcastMessage(String message) {
        getWwPlayers().forEach(wwPlayer -> wwPlayer.getPlayer().sendMessage(message));
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public void setMinPlayers(int minPlayers) {
        this.minPlayers = minPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public boolean isStarted() {
        return this.started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public boolean setCycle(boolean cycle) {
        this.night = cycle;

        return this.night;
    }

    public boolean switchCycle() {
        this.night = !this.night;
        return this.night;
    }

    public boolean isNight() {
        return this.night;
    }

    public boolean isDay() {
        return !this.night;
    }

    public Role getActualRole() {
        return actualRole;
    }

    public void setActualRole(Role actualRole) {
        this.actualRole = actualRole;
    }
}
