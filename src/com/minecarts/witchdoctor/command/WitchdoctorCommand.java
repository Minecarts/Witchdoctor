package com.minecarts.witchdoctor.command;

import net.minecraft.server.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.minecarts.witchdoctor.CommandHandler;
import com.minecarts.witchdoctor.Witchdoctor;
import com.minecarts.witchdoctor.EffectType;

import net.minecraft.server.MobEffect;

import org.bukkit.craftbukkit.entity.CraftPlayer;
import net.minecraft.server.Packet41MobEffect;
import net.minecraft.server.Packet42RemoveMobEffect;
import org.bukkit.entity.Player;

public class WitchdoctorCommand extends CommandHandler {

    public WitchdoctorCommand(Witchdoctor plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args[1].equals("set") && args.length == 5 && sender.hasPermission("witchdoctor.*")){
            Player player = Bukkit.getServer().getPlayerExact(args[0]);
             if(player == null){
                sender.sendMessage("Couldn't match player!");
                return false;
            }
            plugin.applyPotion(player,Integer.parseInt(args[2]),Integer.parseInt(args[4]),Integer.parseInt(args[3]));
            sender.sendMessage("Witchdoctor: Applied effect!");
            return true;
        }
            //new MobEffect(effectid, duration, amplifier);
            //Packet41MobEffect(entityId, mobeffect)
        return false;
    }
}
