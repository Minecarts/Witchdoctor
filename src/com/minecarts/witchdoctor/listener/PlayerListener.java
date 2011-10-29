package com.minecarts.witchdoctor.listener;

import com.minecarts.witchdoctor.EffectTrackerEntry;
import com.minecarts.witchdoctor.Witchdoctor;
import net.minecraft.server.MobEffect;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class PlayerListener extends org.bukkit.event.player.PlayerListener {
    private Witchdoctor plugin;
    private ArrayList<MobEffectTrackingEntry> reapplyEffectList = new ArrayList<MobEffectTrackingEntry>();

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
                    reapplyEffectList.add(new MobEffectTrackingEntry(p,entry.mobEffect,remainingTime));
                }
            }

            //Clear the (old) tracked effects
            plugin.clearTrackedEffects(p.getName());

            //Reapply the potion effects (with the updated remaining time
            for(MobEffectTrackingEntry entry : reapplyEffectList){
                plugin.applyPotion(entry.player,entry.mobEffect.getEffectId(),entry.remainingTime,entry.mobEffect.getAmplifier());
            }
        }
    }

    private class MobEffectTrackingEntry{
        public Player player;
        public MobEffect mobEffect;
        public int remainingTime;

        public MobEffectTrackingEntry(Player p, MobEffect me, int time){
            this.player = p;
            this.mobEffect = me;
            this.remainingTime = time;
        }
    }
}