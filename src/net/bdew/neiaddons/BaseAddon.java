package net.bdew.neiaddons;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.versioning.ArtifactVersion;
import cpw.mods.fml.common.versioning.VersionParser;
import net.bdew.neiaddons.api.NEIAddon;

public abstract class BaseAddon implements NEIAddon {

    protected Boolean active = false;
    protected static Logger log;

    @Override
    public final Boolean isActive() {
        return active;
    }
    
    public final static void logInfo(String message, Object... params) {
        log.log(Level.INFO,message,params);
    }

    public final static void logWarning(String message, Object... params) {
        log.log(Level.WARNING,message,params);
    }
    
    public void preInit(FMLPreInitializationEvent ev) {
        log=ev.getModLog();
    }
    
    protected Boolean verifyModVersion(String spec) {
        ArtifactVersion req = VersionParser.parseVersionReference(spec);
        String modid = req.getLabel();
        
        Map<String, ModContainer> modlist = Loader.instance().getIndexedModList();
        
        if (!modlist.containsKey(modid)) {
            logWarning("Required mod %s is not installed, dependent features will be unavailable", req.getLabel());
            return false;
        }
        
        ArtifactVersion found = modlist.get(modid).getProcessedVersion();
        
        if (found==null) {
            logWarning("Unable to determine version of required mod %s, dependent features will be unavailable", req.getLabel());
            return false;
        }
        
        if (!req.containsVersion(found)) {
            logWarning("Version mismatch: %s is required while %s was detected, dependent features will be unavailable", req.toString(), found.getVersionString());
            return false;
        }
        
        logInfo("Version check success: %s required / %s detected", req.toString(), found.getVersionString());
    
        return true;
    }

    
}