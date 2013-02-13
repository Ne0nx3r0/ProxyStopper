package com.ne0nx3r0.proxystopper;

import com.ne0nx3r0.proxystopper.proxyplayer.ProxyPlayerStatus;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

public class ProxyStopperPlayerListener implements Listener{
    private ProxyStopper plugin;
    
    public ProxyStopperPlayerListener(ProxyStopper plugin)
    {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e)
    {   
        e.getPlayer().sendMessage(plugin.getMessagePrefix()
                +"Please wait while your connection is checked.");

        plugin.proxyPlayerManager.addUncleanPlayer(e.getPlayer());
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e)
    {          
        plugin.proxyPlayerManager.removeUncleanPlayer(e.getPlayer());      
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteract(PlayerInteractEvent e)
    {        
        if(plugin.BEFORE_CHECKED_INTERACT || plugin.AFTER_FOUND_DIRTY_INTERACT)
        {
            ProxyPlayerStatus status = plugin.proxyPlayerManager.getStatus(e.getPlayer());
            
            if(status == ProxyPlayerStatus.UNCHECKED)
            {
                if(plugin.BEFORE_CHECKED_INTERACT)
                {
                    e.setCancelled(true);

                    e.getPlayer().sendMessage(plugin.getMessagePrefix()
                            +"Your connection has not been verified yet, please wait.");
                }
                else if(plugin.AFTER_FOUND_DIRTY_INTERACT)
                {
                    e.setCancelled(true);

                    e.getPlayer().sendMessage(plugin.getMessagePrefix()
                            +"You cannot interact while using a proxy.");
                }
            }
        }
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerMove(PlayerMoveEvent e)
    {        
        if(plugin.BEFORE_CHECKED_MOVE || plugin.AFTER_FOUND_DIRTY_MOVE)
        {
            ProxyPlayerStatus status = plugin.proxyPlayerManager.getStatus(e.getPlayer());
            
            if(status == ProxyPlayerStatus.UNCHECKED)
            {
                if(plugin.BEFORE_CHECKED_MOVE)
                {
                    e.setCancelled(true);

                    e.getPlayer().sendMessage(plugin.getMessagePrefix()
                            +"Your connection has not been verified yet, please wait.");
                }
                else if(plugin.AFTER_FOUND_DIRTY_MOVE)
                {
                    e.setCancelled(true);

                    e.getPlayer().sendMessage(plugin.getMessagePrefix()
                            +"You cannot move while using a proxy.");
                }
            }
        }
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerChat(AsyncPlayerChatEvent e)
    {        
        if(plugin.BEFORE_CHECKED_CHAT || plugin.AFTER_FOUND_DIRTY_CHAT)
        {
            ProxyPlayerStatus status = plugin.proxyPlayerManager.getStatus(e.getPlayer());
            
            if(status == ProxyPlayerStatus.UNCHECKED)
            {
                if(plugin.BEFORE_CHECKED_CHAT)
                {
                    e.setCancelled(true);

                    e.getPlayer().sendMessage(plugin.getMessagePrefix()
                            +"Your connection has not been verified yet, please wait.");
                }
                else if(plugin.AFTER_FOUND_DIRTY_CHAT)
                {
                    e.setCancelled(true);

                    e.getPlayer().sendMessage(plugin.getMessagePrefix()
                            +"You cannot chat while using a proxy.");
                }
            }
        }
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent e)
    {        
        if(plugin.BEFORE_CHECKED_COMMANDS || plugin.AFTER_FOUND_DIRTY_COMMANDS)
        {
            ProxyPlayerStatus status = plugin.proxyPlayerManager.getStatus(e.getPlayer());
            
            if(status == ProxyPlayerStatus.UNCHECKED)
            {
                if(plugin.BEFORE_CHECKED_COMMANDS)
                {
                    e.setCancelled(true);

                    e.getPlayer().sendMessage(plugin.getMessagePrefix()
                            +"Your connection has not been verified yet, please wait.");
                }
                else if(plugin.AFTER_FOUND_DIRTY_COMMANDS)
                {
                    e.setCancelled(true);

                    e.getPlayer().sendMessage(plugin.getMessagePrefix()
                            +"You cannot use commands while using a proxy.");
                }
            }
        }
    }
}