package com.ne0nx3r0.proxystopper.proxyplayer;

import com.ne0nx3r0.proxystopper.ProxyStopper;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Player;

public class ProxyPlayerManager{
    private ProxyStopper plugin;
    private Map<Player,ProxyPlayer> uncleanPlayers;
    
    public ProxyPlayerManager(ProxyStopper p)
    {
        plugin = p;
        
        uncleanPlayers = new HashMap<Player,ProxyPlayer>();
        
        File whitelistFile = new File(plugin.getDataFolder(), "whitelist.yml");   
        
        if(!whitelistFile.exists())
        {
            whitelistFile.getParentFile().mkdirs();
            
            plugin.copy(plugin.getResource(whitelistFile.getName()), whitelistFile);
        }     
        
        int iMoveTicks = plugin.getConfig().getInt("move-ticks");
        
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable()
        {
            @Override
            public void run()
            {
                for(ProxyPlayer pp : uncleanPlayers.values())
                {
                    Player player = pp.getPlayer();
                    
//Theoretically an extra protection from memory leaks.
                    if(!player.isOnline())
                    {
                        uncleanPlayers.remove(player);
                        
                        continue;
                    }
                    
                    if(pp.lastLocation == null)
                    {
                        pp.lastLocation = player.getLocation();
                    }
                    else if(pp.lastLocation != player.getLocation())
                    {
                        player.teleport(pp.lastLocation);
                        
                        if(ProxyStopper.BEFORE_CHECKED_MOVE && pp.status == ProxyPlayerStatus.UNCHECKED)
                        {
                            player.sendMessage(plugin.getMessagePrefix()
                                    +"Please wait while your connection is verified.");
                        }
                        else if(ProxyStopper.AFTER_FOUND_DIRTY_MOVE && pp.status == ProxyPlayerStatus.DIRTY)
                        {
                            player.sendMessage(plugin.getMessagePrefix()
                                    +"You cannot move while using a proxy to connect!");
                        }
                    }
                }
            }
        }, iMoveTicks, iMoveTicks);
    }
    
    //Unclean!!! Unclean!!!
    public void addUncleanPlayer(Player p)
    {
        ProxyPlayer pp = new ProxyPlayer(p);
        
        uncleanPlayers.put(p,pp);
        
        for(int port : plugin.getProxyPorts())
        {
            plugin.getServer().getScheduler().runTaskAsynchronously(
                plugin,
                new PortCheckThread(plugin,pp,port));
        }
    }

    public void removeUncleanPlayer(Player player)
    {
        uncleanPlayers.remove(player);
    }

    public ProxyPlayerStatus getStatus(Player player)
    {
        ProxyPlayer pp = this.uncleanPlayers.get(player);
        
        if(pp != null)
        {
            return pp.getStatus();
        }
        
        //If they didn't exist they've been checked
        return ProxyPlayerStatus.CLEAN;
    }
}
