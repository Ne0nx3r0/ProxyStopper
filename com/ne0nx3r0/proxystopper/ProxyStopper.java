package com.ne0nx3r0.proxystopper;

import com.ne0nx3r0.proxystopper.proxyplayer.ProxyPlayerManager;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

public class ProxyStopper extends JavaPlugin{
    public ProxyPlayerManager proxyPlayerManager;

    private int[] PROXY_PORTS = new int[]{8080,80,81,1080,6588,8000,3128,553,554,4480};
    
    boolean BEFORE_CHECKED_MOVE;
    boolean BEFORE_CHECKED_CHAT;
    boolean BEFORE_CHECKED_COMMANDS;
    boolean BEFORE_CHECKED_INTERACT;
    
    boolean AFTER_FOUND_DIRTY_MOVE;
    boolean AFTER_FOUND_DIRTY_INTERACT;
    boolean AFTER_FOUND_DIRTY_COMMANDS;
    boolean AFTER_FOUND_DIRTY_CHAT;
    public static boolean AFTER_FOUND_DIRTY_AUTOIPBAN;
    public static boolean AFTER_FOUND_DIRTY_AUTOBAN;
    public static boolean AFTER_FOUND_DIRTY_AUTOKICK;
    public static boolean AFTER_FOUND_DIRTY_NOTIFY;
    
    @Override
    public void onEnable()
    {
        File configFile = new File(this.getDataFolder(), "config.yml");   
        
        if(!configFile.exists())
        {
            configFile.getParentFile().mkdirs();
            
            copy(this.getResource(configFile.getName()), configFile);
        }
        
        ConfigurationSection beforeChecked = getConfig().getConfigurationSection("before-checked");
        
        BEFORE_CHECKED_MOVE = beforeChecked.getBoolean("move",false);
        BEFORE_CHECKED_CHAT = beforeChecked.getBoolean("chat",false);
        BEFORE_CHECKED_COMMANDS = beforeChecked.getBoolean("commands",false);
        BEFORE_CHECKED_INTERACT = beforeChecked.getBoolean("interact",false);

        ConfigurationSection afterDirty = getConfig().getConfigurationSection("after-found-dirty");

        AFTER_FOUND_DIRTY_MOVE = afterDirty.getBoolean("move",false);
        AFTER_FOUND_DIRTY_CHAT = afterDirty.getBoolean("chat",false);
        AFTER_FOUND_DIRTY_COMMANDS = afterDirty.getBoolean("commands",false);
        AFTER_FOUND_DIRTY_INTERACT = afterDirty.getBoolean("interact",false);
        AFTER_FOUND_DIRTY_NOTIFY = afterDirty.getBoolean("notify",false);
        AFTER_FOUND_DIRTY_AUTOKICK = afterDirty.getBoolean("auto-kick",false);
        AFTER_FOUND_DIRTY_AUTOBAN = afterDirty.getBoolean("auto-ban",false);
        AFTER_FOUND_DIRTY_AUTOIPBAN = afterDirty.getBoolean("auto-ip-ban",false);
          
        proxyPlayerManager = new ProxyPlayerManager(this);
        
        getServer().getPluginManager().registerEvents(new ProxyStopperPlayerListener(this), this);
    }
    
//Copies files from inside the jar
    public void copy(InputStream in, File file)
    {
        try
        {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            
            while((len=in.read(buf))>0)
            {
                out.write(buf,0,len);
            }
            
            out.close();
            in.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public int[] getProxyPorts()
    {
        return this.PROXY_PORTS;
    }

    public String getMessagePrefix()
    {
        return ChatColor.LIGHT_PURPLE+"[ProxyStopper] "+ChatColor.WHITE;
    }
}
