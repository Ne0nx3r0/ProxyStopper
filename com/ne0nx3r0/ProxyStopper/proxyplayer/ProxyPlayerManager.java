package com.ne0nx3r0.ProxyStopper.proxyplayer;

import com.ne0nx3r0.ProxyStopper.ProxyStopper;
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
        
        //TODO: Whitelist
    }
    
    //Unclean!!! Unclean!!!
    public void addUncleanPlayer(Player p)
    {
        ProxyPlayer pp = new ProxyPlayer(p);
        
        uncleanPlayers.put(p,pp);
        
        for(int port : plugin.getProxyPorts())
        {
            new Thread(new PortCheckThread(plugin,pp,port)).start();
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
