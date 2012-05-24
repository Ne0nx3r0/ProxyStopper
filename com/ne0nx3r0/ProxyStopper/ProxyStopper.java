package com.ne0nx3r0.ProxyStopper;

import org.bukkit.plugin.java.JavaPlugin;
import com.ne0nx3r0.ProxyStopper.proxyplayer.ProxyPlayers;

public class ProxyStopper extends JavaPlugin{
    public ProxyPlayers ppManager;
    
    @Override
    public void onEnable(){
        ppManager = new ProxyPlayers(this);
        
        getServer().getPluginManager().registerEvents(new ProxyStopperPlayerListener(this), this);
    }
}
