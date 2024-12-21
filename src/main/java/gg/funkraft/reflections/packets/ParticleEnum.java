package gg.funkraft.reflections.packets;

import java.util.Objects;

public enum ParticleEnum {
    // Bukkit basically doesn't have a proper
    // particle enum that works on 1.8 - 1.12
    // ((it technically does but it's behind NMS & need reflections so no))
    // so making my own enum with only the ones I need.
    FIREWORKS_SPARK,
    BLOCK_CRACK,
    FLAME,
    ENCHANTMENT_TABLE;

    public static ParticleEnum getFromString(String s) {
        for (ParticleEnum particle : ParticleEnum.values()) {
            if(Objects.equals(particle.toString(), s)) return particle;
        }
        return null;
    }
}
