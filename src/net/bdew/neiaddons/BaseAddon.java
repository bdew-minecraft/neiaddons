package net.bdew.neiaddons;

import java.util.logging.Level;
import java.util.logging.Logger;

import net.bdew.neiaddons.api.NEIAddon;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public abstract class BaseAddon implements NEIAddon {

    protected Boolean active = false;
    protected static Logger log;

    @Override
    public final Boolean isActive() {
        return active;
    }
    
    public final void logInfo(String message, Object... params) {
        log.log(Level.INFO,message,params);
    }

    public final void logWarning(String message, Object... params) {
        log.log(Level.WARNING,message,params);
    }
    
    public void preInit(FMLPreInitializationEvent ev) {
        log=ev.getModLog();
    }
 
}