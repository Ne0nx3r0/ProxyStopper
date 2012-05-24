package com.ne0nx3r0.ProxyStopper.proxyplayer;

import com.ne0nx3r0.ProxyStopper.ProxyStopper;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Player;

public class ProxyPlayers{
    private ProxyStopper plugin;
    private static Map<String,ProxyPlayer> players;
    
    public ProxyPlayers(ProxyStopper p){
        this.plugin = p;
        
        this.players = new HashMap<String,ProxyPlayer>();
    }
    
    public static int[] PROXY_PORTS = new int[]{8080,80,81,1080,6588,8000,3128,553,554,4480};
    private void runProxyCheck(ProxyPlayer pp){
        for(int port : PROXY_PORTS){
            new Thread(new PortCheckThread(pp,port)).start();
        }
    }
    
    private static class PortCheckThread implements Runnable{
        private final ProxyPlayer pp;
        private final int port;

        public PortCheckThread(ProxyPlayer pp,int port){
            this.pp = pp;
            this.port = port;
        }

        @Override
        public void run(){
            System.out.println("trying: "+pp.getAddress()+":"+port);
            
            try{
                Socket ServerSok = new Socket(pp.getAddress(),port);
                
                System.out.println(pp.getAddress()+":"+port+" was open");
                checkedPort(pp,port,true);
                
                ServerSok.close();
                return;
            } catch (UnknownHostException ex) {
                //Logger.getLogger(ProxyPlayers.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                //Logger.getLogger(ProxyPlayers.class.getName()).log(Level.SEVERE, null, ex);
            }  
            
            System.out.println(pp.getAddress()+":"+port+" was closed");
            checkedPort(pp,port,false);
        }
    }
    
    private static void checkedPort(ProxyPlayer pp,int port,boolean wasOpen){
        if(pp.isUsingProxy())
            return;

        if(wasOpen){
            pp.setUsingProxy(true);
            
            System.out.println(pp.getPlayer().getName() + " is using a proxy! (port "+port+" was open)");
        }else{
            pp.addCheckedPort();
            
            if(pp.getNumberOfCheckedPorts() == PROXY_PORTS.length && !pp.isUsingProxy()){
                pp.setClean(true);
                
                System.out.println(pp.getPlayer().getName() + " is clean.");;
            }
        }
    }
    
    //Unclean!!! Unclean!!!
    public void addUncleanPlayer(Player p){
        ProxyPlayer pp = new ProxyPlayer(p);
        
        players.put(p.getName(),pp);
        
        runProxyCheck(pp);
    }

    public void removePlayer(Player player) {
        players.remove(player.getName());
    }

    public boolean isClean(Player player){
        return players.get(player.getName()).isClean();
    }
}
