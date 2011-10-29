package com.minecarts.witchdoctor;

import net.minecraft.server.MobEffect;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: stephen
 * Date: 10/16/11
 * Time: 11:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class EffectTrackerEntry {
    public Date dateAdded;
    public MobEffect mobEffect;
    public int taskId;

    public EffectTrackerEntry(MobEffect mobEffect, int taskId){
        this.dateAdded = new Date();
        this.mobEffect = mobEffect;
        this.taskId = taskId;
    }
}
