package com.ne0nx3r0.proxystopper.proxyplayer;

import com.ne0nx3r0.proxystopper.ProxyStopper;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class ProxyPlayerManager{
    private ProxyStopper plugin;
    private Map<Player,ProxyPlayer> uncleanPlayers;
    private final List<String> whitelistedPlayers;
    private final List<String> whitelistedIps;

    public ProxyPlayerManager(ProxyStopper p)
    {
        plugin = p;
        
        uncleanPlayers = new HashMap<Player,ProxyPlayer>();
        
        File whitelistFile = new File(p.getDataFolder(), "whitelist.yml");   
        
        if(!whitelistFile.exists())
        {
            whitelistFile.getParentFile().mkdirs();
            
            p.copy(p.getResource(whitelistFile.getName()), whitelistFile);
        }     
        
        FileConfiguration whitelistYml = YamlConfiguration.loadConfiguration(whitelistFile);
        
        this.whitelistedPlayers = whitelistYml.getStringList("players");
        
        for(String sPlayer : whitelistedPlayers)
        {
            whitelistedPlayers.remove(sPlayer);
            whitelistedPlayers.add(sPlayer.toLowerCase());
        }
        
        this.whitelistedIps = whitelistYml.getStringList("ips");
        
        for(String sIp : whitelistedIps)
        {
            if(StringUtils.countMatches(sIp,".") < 3)
            {
                whitelistedIps.remove(sIp);
                
                plugin.getLogger().log(Level.WARNING,"'"+sIp+"' is an invalid IP!");
                
                continue;
            }
            
            for(String sOctet : sIp.split("."))
            {
                if(!sOctet.equals("*"))
                {
                    try
                    {
                        Integer.parseInt(sOctet);
                    }
                    catch(Exception e)
                    {
                        whitelistedIps.remove(sIp);

                        plugin.getLogger().log(Level.WARNING,"'"+sIp+"' is an invalid IP!");

                        continue;
                    }
                }
            }
        }
        
        int iMoveTicks = plugin.getConfig().getInt("move-ticks");
        
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(p, new Runnable()
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
        String[] sPlayerIp = p.getAddress().getAddress().getHostAddress().split(".");
        
        if(!this.whitelistedPlayers.contains(p.getName().toLowerCase()))
        {
            for(String sIp : this.whitelistedIps)
            {
                String sOctets = sIp.concat(".");
                
                for(int i=0;i<5;i++)
                {
                    if(!sOctets.equals("*")
                    && !sOctets.equals(sPlayerIp[i]))
                    {
                        break;
                    }
                    
                    //Matched four times
                    if(i == 4)
                    {
                        p.sendMessage(plugin.getMessagePrefix()
                                +"Your IP is whitelisted on this server.");
                        
                        return;
                    }
                }
            }

            p.sendMessage(plugin.getMessagePrefix()
                +"Please wait while your connection is checked.");

            ProxyPlayer pp = new ProxyPlayer(p);

            uncleanPlayers.put(p,pp);

            for(int port : plugin.getProxyPorts())
            {
                plugin.getServer().getScheduler().runTaskAsynchronously(
                    plugin,
                    new PortCheckThread(plugin,pp,port));
            }
        }
        else
        {
            p.sendMessage(plugin.getMessagePrefix()
                    +"You are whitelisted on this server.");
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
