/*******************************************************************************
 * HellFirePvP / Wildfire 2018
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/Wildfire
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.wildfire.client;

import hellfirepvp.wildfire.common.auxiliary.ITickHandler;
import hellfirepvp.wildfire.common.util.Counter;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This class is part of the Wildfire Mod
 * The complete source code for this mod can be found on github.
 * Class: ClientScheduler
 * Created by HellFirePvP
 * Date: 20.02.2018 / 21:21
 */
public class ClientScheduler implements ITickHandler {

    private static long clientTick = 0;
    private static final Object lock = new Object();

    private boolean inTick = false;
    private Map<Runnable, Counter> queuedRunnables = new HashMap<>();
    private Map<Runnable, Integer> waitingRunnables = new HashMap<>();

    @Override
    public void tick(TickEvent.Type type, Object... context) {
        clientTick++;

        inTick = true;
        synchronized (lock) {
            inTick = true;
            Iterator<Runnable> iterator = queuedRunnables.keySet().iterator();
            while (iterator.hasNext()) {
                Runnable r = iterator.next();
                Counter delay = queuedRunnables.get(r);
                delay.decrement();
                if(delay.value <= 0) {
                    r.run();
                    iterator.remove();
                }
            }
            inTick = false;
            for (Map.Entry<Runnable, Integer> waiting : waitingRunnables.entrySet()) {
                queuedRunnables.put(waiting.getKey(), new Counter(waiting.getValue()));
            }
        }
        waitingRunnables.clear();
    }

    public static long getClientTick() {
        return clientTick;
    }

    @Override
    public EnumSet<TickEvent.Type> getHandledTypes() {
        return EnumSet.of(TickEvent.Type.CLIENT);
    }

    @Override
    public boolean canFire(TickEvent.Phase phase) {
        return phase == TickEvent.Phase.END;
    }

    @Override
    public String getName() {
        return "Client Scheduler";
    }

    public void addRunnable(Runnable r, int tickDelay) {
        synchronized (lock) {
            if(inTick) {
                waitingRunnables.put(r, tickDelay);
            } else {
                queuedRunnables.put(r, new Counter(tickDelay));
            }
        }
    }

}
