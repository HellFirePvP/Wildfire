/*******************************************************************************
 * HellFirePvP / Wildfire 2018
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/Wildfire
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.wildfire.common.auxiliary;

import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.EnumSet;

/**
 * This class is part of the Wildfire Mod
 * The complete source code for this mod can be found on github.
 * Class: ITickHandler
 * Created by HellFirePvP
 * Date: 20.02.2018 / 21:01
 */
public interface ITickHandler {

    public void tick(TickEvent.Type type, Object... context);

    /**
     * WORLD, context: world
     * SERVER, context:
     * CLIENT, context:
     * RENDER, context: pTicks
     * PLAYER, context: player, side
     */
    public EnumSet<TickEvent.Type> getHandledTypes();

    public boolean canFire(TickEvent.Phase phase);

    public String getName();

}
