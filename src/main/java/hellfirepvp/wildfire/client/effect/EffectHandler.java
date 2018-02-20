/*******************************************************************************
 * HellFirePvP / Wildfire 2018
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/Wildfire
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.wildfire.client.effect;

import hellfirepvp.wildfire.Wildfire;
import hellfirepvp.wildfire.client.effect.fx.EntityFXFacingDepthParticle;
import hellfirepvp.wildfire.client.effect.fx.EntityFXFacingParticle;
import hellfirepvp.wildfire.client.resource.AssetLibrary;
import hellfirepvp.wildfire.common.data.config.Config;
import hellfirepvp.wildfire.common.util.Counter;
import hellfirepvp.wildfire.common.util.Vector3;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.*;

/**
 * This class is part of the Wildfire Mod
 * The complete source code for this mod can be found on github.
 * Class: EffectHandler
 * Created by HellFirePvP
 * Date: 20.02.2018 / 21:32
 */
public final class EffectHandler {

    public static final Random STATIC_EFFECT_RAND = new Random();
    public static final EffectHandler instance = new EffectHandler();

    private static boolean acceptsNewParticles = true, cleanRequested = false;
    private static List<IComplexEffect> toAddBuffer = new LinkedList<>();

    public static final Map<IComplexEffect.RenderTarget, Map<Integer, List<IComplexEffect>>> complexEffects = new HashMap<>();
    public static final List<EntityFXFacingDepthParticle> fastRenderDepthParticles = new LinkedList<>();
    public static final List<EntityFXFacingParticle> fastRenderParticles = new LinkedList<>();

    private EffectHandler() {}

    public static EffectHandler getInstance() {
        return instance;
    }

    public static int getDebugEffectCount() {
        final Counter c = new Counter(0);
        for (Map<Integer, List<IComplexEffect>> effects : complexEffects.values()) {
            for (List<IComplexEffect> eff : effects.values()) {
                c.value += eff.size();
            }
        }
        c.value += fastRenderParticles.size();
        return c.value;
    }

    @SubscribeEvent
    public void onDebugText(RenderGameOverlayEvent.Text event) {
        if(Minecraft.getMinecraft().gameSettings.showDebugInfo) {
            event.getLeft().add("");
            event.getLeft().add(TextFormatting.BLUE + "[Wildfire]" + TextFormatting.RESET + " EffectHandler:");
            event.getLeft().add(TextFormatting.BLUE + "[Wildfire]" + TextFormatting.RESET + " > Complex effects: " + getDebugEffectCount());
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onRender(RenderWorldLastEvent event) {
        float pTicks = event.getPartialTicks();
        acceptsNewParticles = false;
        GlStateManager.disableDepth();
        EntityFXFacingParticle.renderFast(pTicks, fastRenderDepthParticles);
        GlStateManager.enableDepth();
        EntityFXFacingParticle.renderFast(pTicks, fastRenderParticles);

        Map<Integer, List<IComplexEffect>> layeredEffects = complexEffects.get(IComplexEffect.RenderTarget.RENDERLOOP);
        for (int i = 0; i <= 2; i++) {
            for (IComplexEffect effect : layeredEffects.get(i)) {
                effect.render(pTicks);
            }
        }
        acceptsNewParticles = true;
    }

    @SubscribeEvent
    public void onClTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        tick();
    }

    public EntityComplexFX registerFX(EntityComplexFX entityComplexFX) {
        register(entityComplexFX);
        return entityComplexFX;
    }

    private void register(final IComplexEffect effect) {
        if(AssetLibrary.reloading || effect == null || Minecraft.getMinecraft().isGamePaused()) return;

        //instead of getEffeciveSide - neither is pretty, but this at least prevents async editing.
        if (!Thread.currentThread().getName().contains("Client thread")) {
            Wildfire.proxy.scheduleClientside(() -> register(effect));
            return;
        }

        if(acceptsNewParticles) {
            registerUnsafe(effect);
        } else {
            toAddBuffer.add(effect);
        }
    }

    private void registerUnsafe(IComplexEffect effect) {
        if(!mayAcceptParticle(effect)) return;

        if(effect instanceof EntityFXFacingDepthParticle) {
            fastRenderDepthParticles.add((EntityFXFacingDepthParticle) effect);
        } else if(effect instanceof EntityFXFacingParticle) {
            fastRenderParticles.add((EntityFXFacingParticle) effect);
        } else {
            complexEffects.get(effect.getRenderTarget()).get(effect.getLayer()).add(effect);
        }
        effect.clearRemoveFlag();
    }

    public void tick() {
        if(cleanRequested) {
            for (IComplexEffect.RenderTarget t : IComplexEffect.RenderTarget.values()) {
                for (int i = 0; i <= 2; i++) {
                    List<IComplexEffect> effects = complexEffects.get(t).get(i);
                    effects.forEach(IComplexEffect::flagAsRemoved);
                    effects.clear();
                }
            }
            fastRenderParticles.clear();
            toAddBuffer.clear();
            cleanRequested = false;
        }
        if(Minecraft.getMinecraft().player == null) {
            return;
        }

        acceptsNewParticles = false;
        for (IComplexEffect.RenderTarget target : complexEffects.keySet()) {
            Map<Integer, List<IComplexEffect>> layeredEffects = complexEffects.get(target);
            for (int i = 0; i <= 2; i++) {
                Iterator<IComplexEffect> iterator = layeredEffects.get(i).iterator();
                while (iterator.hasNext()) {
                    IComplexEffect effect = iterator.next();
                    effect.tick();
                    if(effect.canRemove()) {
                        effect.flagAsRemoved();
                        iterator.remove();
                    }
                }
            }
        }

        Vector3 playerPos = Vector3.atEntityCorner(Minecraft.getMinecraft().player);
        for (EntityFXFacingParticle effect : new ArrayList<>(fastRenderParticles)) {
            if (effect == null) {
                fastRenderParticles.remove(null);
                continue;
            }
            effect.tick();
            if (effect.canRemove() || effect.getPosition().distanceSquared(playerPos) >= Config.maxEffectRenderDistanceSq) {
                effect.flagAsRemoved();
                fastRenderParticles.remove(effect);
            }
        }
        for (EntityFXFacingParticle effect : new ArrayList<>(fastRenderDepthParticles)) {
            if (effect == null) {
                fastRenderDepthParticles.remove(null);
                continue;
            }
            effect.tick();
            if (effect.canRemove() || effect.getPosition().distanceSquared(playerPos) >= Config.maxEffectRenderDistanceSq) {
                effect.flagAsRemoved();
                fastRenderDepthParticles.remove(effect);
            }
        }
        acceptsNewParticles = true;
        List<IComplexEffect> effects = new LinkedList<>(toAddBuffer);
        toAddBuffer.clear();
        for (IComplexEffect eff : effects) {
            registerUnsafe(eff);
        }
    }

    public static boolean mayAcceptParticle(IComplexEffect effect) {
        int cfg = Config.particleAmount;
        if(cfg > 1 && !Minecraft.isFancyGraphicsEnabled()) {
            cfg = 1;
        }
        if(effect instanceof IComplexEffect.PreventRemoval || cfg == 2) return true;
        return cfg == 1 && STATIC_EFFECT_RAND.nextInt(3) == 0;
    }

    static {
        for (IComplexEffect.RenderTarget target : IComplexEffect.RenderTarget.values()) {
            Map<Integer, List<IComplexEffect>> layeredEffects = new HashMap<>();
            for (int i = 0; i <= 2; i++) {
                layeredEffects.put(i, new LinkedList<>());
            }
            complexEffects.put(target, layeredEffects);
        }
    }

    public static void cleanUp() {
        cleanRequested = true;
    }
}
