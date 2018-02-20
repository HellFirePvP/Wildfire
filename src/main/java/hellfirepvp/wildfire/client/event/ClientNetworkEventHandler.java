/*******************************************************************************
 * HellFirePvP / Wildfire 2018
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/Wildfire
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.wildfire.client.event;

import hellfirepvp.wildfire.client.ClientProxy;
import hellfirepvp.wildfire.client.effect.EffectHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

/**
 * This class is part of the Wildfire Mod
 * The complete source code for this mod can be found on github.
 * Class: ClientNetworkEventHandler
 * Created by HellFirePvP
 * Date: 20.02.2018 / 21:09
 */
public class ClientNetworkEventHandler {

    @SubscribeEvent
    public void onDc(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        EffectHandler.cleanUp();
        ClientProxy.connected = false;
    }

}
