package cc.hyperium.mixinsimp.renderer.client.particle;

import net.minecraft.client.particle.IParticleFactory;
import java.util.Map;

public interface IMixinEffectRenderer {
    Map<Integer, IParticleFactory> getParticleMap();
}
