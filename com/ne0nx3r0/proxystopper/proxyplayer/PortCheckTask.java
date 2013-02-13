package com.ne0nx3r0.proxystopper.proxyplayer;

import com.ne0nx3r0.proxystopper.ProxyStopper;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

class PortCheckThread implements Runnable
{
    private final ProxyStopper plugin;
    private final ProxyPlayer pp;
    private final int port;

    public PortCheckThread(ProxyStopper plugin,ProxyPlayer pp,int port)
    {
        this.plugin = plugin;
        this.pp = pp;
        this.port = port;
    }

    @Override
    public void run()
    {
            System.out.println("trying: "+pp.getAddress()+":"+port);

        try
        {
            Socket ServerSok = new Socket(pp.getAddress(),port);

            plugin.getLogger().log(Level.INFO,pp.getAddress()+":"+port+" was open");
            checkedPort(pp,port,true);

            ServerSok.close();
            return;
        }
        catch (Exception ex)
        {
            Logger.getLogger(ProxyPlayerManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println(pp.getAddress()+":"+port+" was closed");
        checkedPort(pp,port,false);
    }

    private void checkedPort(ProxyPlayer pp,int port,boolean wasOpen)
    {
        if(wasOpen)
        {
            pp.setStatus(ProxyPlayerStatus.DIRTY);

            plugin.getLogger().log(Level.INFO, 
                    "{0} is using a proxy! (port {1} was open)", 
                    new Object[]{pp.getPlayer().getName(), port});
        }
        else
        {
            if(pp.getNumberOfCheckedPorts() == plugin.getProxyPorts().length 
            && pp.getStatus() == ProxyPlayerStatus.UNCHECKED)
            {
                pp.setStatus(ProxyPlayerStatus.CLEAN);
            }
            
            pp.addCheckedPort();

            pp.getPlayer().sendMessage(
                plugin.getMessagePrefix()
                +(pp.getNumberOfCheckedPorts() / plugin.getProxyPorts().length * 10)
                + "% verified");
        }
    }
}