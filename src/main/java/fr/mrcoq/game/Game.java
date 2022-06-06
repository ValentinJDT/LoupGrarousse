package fr.mrcoq.game;

import fr.mrcoq.LoupGarousse;
import fr.mrcoq.game.player.WWPlayer;
import fr.mrcoq.game.player.role.Role;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.stream.Collectors;

public class Game {

    private boolean started = false;
    private boolean night = false;

    private List<WWPlayer> wwPlayers = new ArrayList<>();
    private Map<Player, WWPlayer> selectedWWPlayers = new HashMap<>();
    private List<WWPlayer> deadPlayers = new ArrayList<>();

    private Role actualRole = Role.VILLAGER;

    private int day = 0;
    private int minPlayers = 5;
    private int maxPlayers = 12;
    private int seconds = 12;

    public Game() {

    }

    public void start() {
        if (started) {
            return;
        }

        this.day = 0;
        this.started = true;

        while (started) {
            if (this.getRoles().size() < 2) {
                this.started = false;
                continue;
            }

            for (Role role : this.getRoles()) {
                this.actualRole = role;
                if (role == Role.VILLAGER) {
                    this.setCycle(false);
                    this.broadcastMessage("§7Il est l'heure de se réveiller");
                } else if (!this.night) {
                    setCycle(true);
                    this.broadcastMessage("§7Il est l'heure de se coucher");
                }

                List<WWPlayer> actualWWPlayers = getPlayersByRole(role);

                seconds = 0;
                selectedWWPlayers.clear();
                new RunnableStart(role, actualWWPlayers).runTaskTimer(LoupGarousse.getInstance(), 0, 20L);


                /*Bukkit.getScheduler().scheduleSyncRepeatingTask(LoupGarousse.getInstance(), () -> {
                }, 0, 20L);*/
            }
        }


    }

    public class RunnableStart extends BukkitRunnable {

        private Role role;
        List<WWPlayer> actualWWPlayers;

        public RunnableStart(Role role, List<WWPlayer> actualWWPlayers) {
            this.actualWWPlayers = actualWWPlayers;
            this.role = role;
        }

        @Override
        public void run() {
            if (role.getTimePlay() < seconds) {
                this.cancel();
                return;
            }

            broadcastActionBarMessage(role.getDisplayName() + " | " + (role.getTimePlay() - seconds));
            seconds++;
        }
    }

    public boolean kill(Player player) {

        if (!this.started) {
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

        if (canJoin) {
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

    public boolean wwPlayerExist(Player player) {
        return wwPlayers.stream().anyMatch(wwPlayer -> player.getName().equals(wwPlayer.getPlayer().getName()));
    }

    public boolean wwPlayerExist(String playerName) {
        return wwPlayers.stream().anyMatch(wwPlayer -> playerName.equals(wwPlayer.getPlayer().getName()));
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
        return wwPlayers.stream().map(WWPlayer::getRole).sorted(Comparator.comparingInt(Role::getOrder)).distinct().collect(Collectors.toList());
    }

    public List<WWPlayer> getWwPlayers() {
        return wwPlayers;
    }

    public Map<Player, WWPlayer> getSelectedWWPlayers() {
        return selectedWWPlayers;
    }

    public void setSelectedWWPlayers(Map<Player, WWPlayer> selectedWWPlayers) {
        this.selectedWWPlayers = selectedWWPlayers;
    }

    public void select(Player player, Player target) {
        WWPlayer wwPlayer = getWWPlayer(target);
        selectedWWPlayers.put(player, wwPlayer);
    }

    public void broadcastMessage(String message) {
        getWwPlayers().forEach(wwPlayer -> wwPlayer.getPlayer().sendMessage(message));
    }

    public void broadcastActionBarMessage(String message) {
        getWwPlayers().forEach(wwPlayer -> wwPlayer.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message)));
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
