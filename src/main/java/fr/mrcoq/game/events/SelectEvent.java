package fr.mrcoq.game.events;

import fr.mrcoq.LoupGarousse;
import fr.mrcoq.game.Game;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class SelectEvent implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {

        Game game = LoupGarousse.getGame();

        if(!game.isStarted()) return;
        if(!game.wwPlayerExist(e.getPlayer())) return;
        if(game.isNight()) return;

        if(!e.getAction().name().contains("LEFT_CLICK")) return;

        Player player = e.getPlayer();

        Player target = getPlayerIsLooking(player, game);

        game.broadcastMessage(player.getName() + " vote pour " + target.getDisplayName());

        game.select(player, target);

    }

    public Player getPlayerIsLooking(Player player, Game game) {
        for(Entity entity : player.getNearbyEntities(10, 10, 10)) {
            if(entity instanceof Player) {
                if(getLookingAt(player, (LivingEntity) entity)) {
                    if(game.wwPlayerExist((Player) entity)) {
                        return (Player) entity;
                    }
                }
            }
        }

        return null;
    }

    public boolean getLookingAt(Player player, LivingEntity livingEntity) {
        Location eye = player.getEyeLocation();
        Vector toEntity = livingEntity.getEyeLocation().toVector().subtract(eye.toVector());
        double dot = toEntity.normalize().dot(eye.getDirection());

        return dot > 0.99d;
    }

}
