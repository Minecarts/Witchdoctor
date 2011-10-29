package com.minecarts.witchdoctor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import com.minecarts.witchdoctor.listener.PlayerListener;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.MobEffect;
import net.minecraft.server.Packet41MobEffect;
import net.minecraft.server.Packet42RemoveMobEffect;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.PluginDescriptionFile;

import com.minecarts.witchdoctor.command.WitchdoctorCommand;

public class Witchdoctor extends org.bukkit.plugin.java.JavaPlugin {
    public final Logger log = Logger.getLogger("com.minecarts.witchdoctor");
    private HashMap<String,List<EffectTrackerEntry>> playerEffects = new HashMap<String,List<EffectTrackerEntry>>();


    private PlayerListener playerListener = new PlayerListener(this);

    public void clearTrackedEffects(String name){
        if(playerEffects.containsKey(name)){
            playerEffects.get(name).clear();
        }
    }
    
    public List<EffectTrackerEntry> getTrackedEffects(String name){
        if(playerEffects.containsKey(name)){
            return playerEffects.get(name);
        } else {
            return null;
        }
    }

    public void trackEffect(String name, MobEffect me, int taskId){
        EffectTrackerEntry entry = new EffectTrackerEntry(me, taskId);
        if(playerEffects.containsKey(name)){
            playerEffects.get(name).add(entry);
        } else {
            List<EffectTrackerEntry> list = new ArrayList<EffectTrackerEntry>();
            list.add(entry);
            playerEffects.put(name, list);
        }
    }
    public void untrackEffect(String name, MobEffect me){
        if(playerEffects.containsKey(name)){
            playerEffects.get(name).remove(me);
        }
    }

    public void onEnable() {
        PluginManager pm = getServer().getPluginManager();
        PluginDescriptionFile pdf = getDescription();

        Bukkit.getPluginManager().registerEvent(Event.Type.PLAYER_JOIN, playerListener, Event.Priority.Monitor,this);

        //Register commands
        getCommand("witchdoctor").setExecutor(new WitchdoctorCommand(this));

        log.info("[" + pdf.getName() + "] version " + pdf.getVersion() + " enabled.");
    }

     public void applyPotion(Player player, int effectId, int duration, int amplifier){
        final MobEffect me = new MobEffect(effectId,duration,amplifier);
        final EntityPlayer ep = ((CraftPlayer) player).getHandle();

        ep.netServerHandler.sendPacket(new Packet41MobEffect(ep.id, me));
        final Witchdoctor plugin = this;

        //Remove the effect later
        int taskId = Bukkit.getScheduler().scheduleSyncDelayedTask(this,(new Runnable() {
            public void run() {
                plugin.untrackEffect(ep.name,me);
                ep.netServerHandler.sendPacket(new Packet42RemoveMobEffect(ep.id, me));
            }
        }),duration);

       this.trackEffect(ep.name,me,taskId); //Track for reapplying at login
    }

    public void onDisable() {

    }
}