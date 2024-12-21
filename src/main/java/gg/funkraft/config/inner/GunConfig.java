package gg.funkraft.config.inner;

import org.bukkit.configuration.ConfigurationSection;

import lombok.Getter;
import gg.funkraft.config.ConfigPart;

@Getter
public class GunConfig extends ConfigPart {
    public GunConfig(ConfigurationSection conf) {
        super(conf);
        gunDelayDuration = getInt("gunDelayDuration", 50);
    }
    
    private final int gunDelayDuration;
}
