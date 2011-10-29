package com.minecarts.witchdoctor.listener;

import com.minecarts.witchdoctor.EffectTrackerEntry;
import com.minecarts.witchdoctor.Witchdoctor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.Timestamp;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class PlayerListener extends org.bukkit.event.player.PlayerListener {
    private Witchdoctor plugin;

    public PlayerListener(Witchdoctor plugin){
        this.plugin = plugin;
    }
    @Override
    public void onPlayerJoin(PlayerJoinEvent e) {
        //Reapply any pending effects for this player
        Player p = e.getPlayer();
        List<EffectTrackerEntry> pendingEffects = plugin.getTrackedEffects(p.getName());
        if(pendingEffects != null){
            for(EffectTrackerEntry entry : pendingEffects){
                //Find the number of seconds between when this effect was applied, and now
                Date now = new Date();
                int remainingTime = entry.mobEffect.getDuration() - ((int)((now.getTime() - entry.dateAdded.getTime()) / 1000) * 20);
                //System.out.println("Tracked effect founnd, remaining time: " + remainingTime);
                if(remainingTime > 0){
                    Bukkit.getScheduler().cancelTask(entry.taskId); //Cancel the old pending task
                    plugin.applyPotion(p,entry.mobEffect.getEffectId(),remainingTime,entry.mobEffect.getAmplifier());
                }
            }
            plugin.clearTrackedEffects(p.getName());
        }
    }
}