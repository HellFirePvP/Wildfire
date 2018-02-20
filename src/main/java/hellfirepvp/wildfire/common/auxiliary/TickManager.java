/*******************************************************************************
 * HellFirePvP / Wildfire 2018
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/Wildfire
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.wildfire.common.auxiliary;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This class is part of the Wildfire Mod
 * The complete source code for this mod can be found on github.
 * Class: TickManager
 * Created by HellFirePvP
 * Date: 20.02.2018 / 21:01
 */
public class TickManager {

    private static final TickManager instance = new TickManager();

    private Map<TickEvent.Type, List<ITickHandler>> registeredTickHandlers = new HashMap<>();

    private TickManager() {
        for (TickEvent.Type type : TickEvent.Type.values()) {
            registeredTickHandlers.put(type, new LinkedList<>());
        }
    }

    public static TickManager getInstance() {
        return instance;
    }

    public void register(ITickHandler handler) {
        for (TickEvent.Type type : handler.getHandledTypes()) {
            registeredTickHandlers.get(type).add(handler);
        }
    }

    @SubscribeEvent
    public void worldTick(TickEvent.WorldTickEvent event) {
        TickEvent.Phase ph = event.phase;
        for (ITickHandler handler : registeredTickHandlers.get(TickEvent.Type.WORLD)) {
            if(handler.canFire(ph)) handler.tick(TickEvent.Type.WORLD, event.world);
        }
    }

    @SubscribeEvent
    public void serverTick(TickEvent.ServerTickEvent event) {
        TickEvent.Phase ph = event.phase;
        for (ITickHandler handler : registeredTickHandlers.get(TickEvent.Type.SERVER)) {
            if(handler.canFire(ph)) handler.tick(TickEvent.Type.SERVER);
        }
    }

    @SubscribeEvent
    public void clientTick(TickEvent.ClientTickEvent event) {
        TickEvent.Phase ph = event.phase;
        for (ITickHandler handler : registeredTickHandlers.get(TickEvent.Type.CLIENT)) {
            if(handler.canFire(ph)) handler.tick(TickEvent.Type.CLIENT);
        }
    }

    @SubscribeEvent
    public void renderTick(TickEvent.RenderTickEvent event) {
        TickEvent.Phase ph = event.phase;
        for (ITickHandler handler : registeredTickHandlers.get(TickEvent.Type.RENDER)) {
            if(handler.canFire(ph)) handler.tick(TickEvent.Type.RENDER, event.renderTickTime);
        }
    }

    @SubscribeEvent
    public void playerTick(TickEvent.PlayerTickEvent event) {
        TickEvent.Phase ph = event.phase;
        for (ITickHandler handler : registeredTickHandlers.get(TickEvent.Type.PLAYER)) {
            if(handler.canFire(ph)) handler.tick(TickEvent.Type.PLAYER, event.player, event.side);
        }
    }

}
