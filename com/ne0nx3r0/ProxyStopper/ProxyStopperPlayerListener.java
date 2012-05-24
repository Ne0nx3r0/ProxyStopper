package com.ne0nx3r0.ProxyStopper;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ProxyStopperPlayerListener implements Listener{
    private ProxyStopper plugin;
    
    public ProxyStopperPlayerListener(ProxyStopper p){
        this.plugin = p;
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){        
        plugin.ppManager.addUncleanPlayer(e.getPlayer());
        
        e.getPlayer().sendMessage("Please wait while your connection is verified.");
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e){          
        plugin.ppManager.removePlayer(e.getPlayer());      
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteract(PlayerInteractEvent e){        
        if(!plugin.ppManager.isClean(e.getPlayer())){
            e.setCancelled(true);
            
            e.getPlayer().sendMessage("Your connection has not been verified.");
        }
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent e){
        if(!plugin.ppManager.isClean(e.getPlayer())){
            e.setCancelled(true);
            
            e.getPlayer().sendMessage("Your connection has not been verified.");
        }
    }
}