/*******************************************************************************
 * HellFirePvP / Wildfire 2018
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/Wildfire
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.wildfire.common.event;

import hellfirepvp.wildfire.Wildfire;
import hellfirepvp.wildfire.common.data.SyncDataHolder;
import hellfirepvp.wildfire.common.network.PacketChannel;
import hellfirepvp.wildfire.common.network.packet.server.PktFinalizeLogin;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

/**
 * This class is part of the Wildfire Mod
 * The complete source code for this mod can be found on github.
 * Class: EventHandlerNetwork
 * Created by HellFirePvP
 * Date: 20.02.2018 / 21:14
 */
public class EventHandlerNetwork {

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onLogin(PlayerEvent.PlayerLoggedInEvent e) {
        EntityPlayerMP p = (EntityPlayerMP) e.player;
        Wildfire.log.info("[Wildfire] Synchronizing configuration to " + p.getName());
        SyncDataHolder.syncAllDataTo(p);

        PacketChannel.CHANNEL.sendTo(new PktFinalizeLogin(), p);
    }
}
