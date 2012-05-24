package com.ne0nx3r0.ProxyStopper.proxyplayer;

import org.bukkit.entity.Player;

public class ProxyPlayer{
    private Player player;
    private boolean clean = false;
    
    private boolean usingProxy = false;
    private int numOfPortsChecked = 0;
    
    ProxyPlayer(Player p){
        this.player = p;
    }
    
    public boolean isClean(){
        return this.clean;
    }
    
    public String getAddress(){
        return player.getAddress().getAddress().getHostAddress();
    }
    
    public void addCheckedPort(){
        numOfPortsChecked++;
    }
    
    public int getNumberOfCheckedPorts(){
        return numOfPortsChecked;
    }
    
    public void setClean(boolean clean){
        this.clean = clean;
    }
    
    public Player getPlayer(){
        return player;
    }

    public boolean isUsingProxy(){
        return usingProxy;
    }
    public void setUsingProxy(boolean b) {
        this.usingProxy = true;
    }
}
