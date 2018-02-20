/*******************************************************************************
 * HellFirePvP / Wildfire 2018
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/Wildfire
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.wildfire.common.network;

import hellfirepvp.wildfire.Wildfire;
import hellfirepvp.wildfire.client.ClientProxy;
import hellfirepvp.wildfire.common.network.packet.ClientReplyPacket;
import hellfirepvp.wildfire.common.network.packet.server.PktFinalizeLogin;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * This class is part of the Wildfire Mod
 * The complete source code for this mod can be found on github.
 * Class: PacketChannel
 * Created by HellFirePvP
 * Date: 20.02.2018 / 21:08
 */
public class PacketChannel {

    public static final SimpleNetworkWrapper CHANNEL = new SimpleNetworkWrapper(Wildfire.NAME) {

        @Override
        public void sendToServer(IMessage message) {
            if(message instanceof ClientReplyPacket && !PacketChannel.canBeSent(message)) {
                return;
            }
            super.sendToServer(message);
        }
    };

    @SideOnly(Side.CLIENT)
    private static boolean canBeSent(IMessage message) {
        return ClientProxy.connected;
    }

    public static void init() {
        int id = 0;

        //(server -> client)
        CHANNEL.registerMessage(PktFinalizeLogin.class, PktFinalizeLogin.class, id++, Side.CLIENT);

        //(client -> server)
        //CHANNEL.registerMessage(PktDiscoverConstellation.class, PktDiscoverConstellation.class, id++, Side.SERVER);
    }

    public static NetworkRegistry.TargetPoint pointFromPos(World world, Vec3i pos, double range) {
        return new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), range);
    }
}
