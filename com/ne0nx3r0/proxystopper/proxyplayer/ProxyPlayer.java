package com.ne0nx3r0.proxystopper.proxyplayer;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ProxyPlayer
{
    Player player;
    int numOfPortsChecked = 0;
    ProxyPlayerStatus status;
    Location lastLocation;
    
    ProxyPlayer(Player p)
    {
        this.player = p;
    }
    
    public String getAddress()
    {
        return player.getAddress().getAddress().getHostAddress();
    }
    
    public void addCheckedPort()
    {
        numOfPortsChecked++;
    }
    
    public int getNumberOfCheckedPorts()
    {
        return numOfPortsChecked;
    }
    
    public Player getPlayer()
    {
        return player;
    }

    public ProxyPlayerStatus getStatus()
    {
        return this.status;
    }
    
    public void setStatus(ProxyPlayerStatus status)
    {
        this.status = status;
    }
}
