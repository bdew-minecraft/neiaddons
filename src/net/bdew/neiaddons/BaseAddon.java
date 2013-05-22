package net.bdew.neiaddons;

import net.bdew.neiaddons.api.NEIAddon;

public abstract class BaseAddon implements NEIAddon {

    protected Boolean active = false;

    @Override
    public final Boolean isActive() {
        return active;
    }
}