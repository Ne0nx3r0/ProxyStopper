package com.ne0nx3r0.ProxyStopper.proxyplayer;

import org.bukkit.entity.Player;

public class ProxyPlayer
{
    private Player player;
    private int numOfPortsChecked = 0;
    private ProxyPlayerStatus status;
    
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
