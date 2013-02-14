package com.ne0nx3r0.proxystopper;

import com.ne0nx3r0.proxystopper.proxyplayer.ProxyPlayerManager;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import net.h31ix.updater.Updater;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

public class ProxyStopper extends JavaPlugin
{
    public ProxyPlayerManager proxyPlayerManager;

    private List<Integer> PROXY_PORTS;
    
    public static boolean BEFORE_CHECKED_MOVE;
    public static boolean BEFORE_CHECKED_CHAT;
    public static boolean BEFORE_CHECKED_COMMANDS;
    public static boolean BEFORE_CHECKED_INTERACT;
    
    public static boolean AFTER_FOUND_DIRTY_MOVE;
    public static boolean AFTER_FOUND_DIRTY_INTERACT;
    public static boolean AFTER_FOUND_DIRTY_COMMANDS;
    public static boolean AFTER_FOUND_DIRTY_CHAT;
    public static boolean AFTER_FOUND_DIRTY_AUTOIPBAN;
    public static boolean AFTER_FOUND_DIRTY_AUTOBAN;
    public static boolean AFTER_FOUND_DIRTY_AUTOKICK;
    public static boolean AFTER_FOUND_DIRTY_NOTIFY;
    
    boolean UPDATE_AVAILABLE = false;
    String UPDATE_STRING;
    
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

        PROXY_PORTS = new ArrayList<Integer>();
        
        for(String sPort : getConfig().getString("ports").split(","))
        {
            try
            {
                PROXY_PORTS.add(Integer.parseInt(sPort));
            }
            catch(Exception e)
            {
                getLogger().log(Level.WARNING,"'"+sPort + "' Is not a valid port number!");
            }
        }
        
        proxyPlayerManager = new ProxyPlayerManager(this);
        
        getServer().getPluginManager().registerEvents(new ProxyStopperPlayerListener(this), this);
        
        if(getConfig().getBoolean("notify-about-updates"))
        {
            Updater updater = new Updater(this, "proxystopper", this.getFile(), Updater.UpdateType.NO_DOWNLOAD, false);
            
            UPDATE_AVAILABLE = updater.getResult() == Updater.UpdateResult.UPDATE_AVAILABLE;
            UPDATE_STRING = updater.getLatestVersionString();
        }
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

    public List<Integer> getProxyPorts()
    {
        return this.PROXY_PORTS;
    }

    public String getMessagePrefix()
    {
        return ChatColor.LIGHT_PURPLE+"[ProxyStopper] "+ChatColor.WHITE;
    }
}
