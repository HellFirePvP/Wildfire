/*******************************************************************************
 * HellFirePvP / Wildfire 2018
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/Wildfire
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.wildfire.common.network.packet.server;

import hellfirepvp.wildfire.Wildfire;
import hellfirepvp.wildfire.common.data.AbstractData;
import hellfirepvp.wildfire.common.data.SyncDataHolder;
import hellfirepvp.wildfire.common.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is part of the Wildfire Mod
 * The complete source code for this mod can be found on github.
 * Class: PktSyncData
 * Created by HellFirePvP
 * Date: 20.02.2018 / 21:16
 */
public class PktSyncData implements IMessage, IMessageHandler<PktSyncData, IMessage> {

    private Map<String, AbstractData> data = new HashMap<>();
    private boolean shouldSyncAll = false;

    public PktSyncData() {
    }

    public PktSyncData(Map<String, AbstractData> dataToSend, boolean shouldSyncAll) {
        this.data = dataToSend;
        this.shouldSyncAll = shouldSyncAll;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        PacketBuffer pb = new PacketBuffer(buf);
        int size = pb.readInt();

        for (int i = 0; i < size; i++) {
            String key = ByteBufUtils.readString(pb);

            byte providerId = pb.readByte();
            AbstractData.AbstractDataProvider<? extends AbstractData> provider = AbstractData.Registry.getProvider(providerId);
            if (provider == null) {
                Wildfire.log.warn("[Wildfire] Provider for ID " + providerId + " doesn't exist! Skipping...");
                continue;
            }

            NBTTagCompound cmp;
            try {
                cmp = pb.readCompoundTag();
            } catch (IOException e) {
                Wildfire.log.warn("[Wildfire] Provider Compound of " + providerId + " threw an IOException! Skipping...");
                Wildfire.log.warn("[Wildfire] Exception message: " + e.getMessage());
                continue;
            }

            AbstractData dat = provider.provideNewInstance();
            dat.readRawFromPacket(cmp);

            data.put(key, dat);
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer pb = new PacketBuffer(buf);
        pb.writeInt(data.size());

        for (String key : data.keySet()) {
            AbstractData dat = data.get(key);
            NBTTagCompound cmp = new NBTTagCompound();
            if (shouldSyncAll) {
                dat.writeAllDataToPacket(cmp);
            } else {
                dat.writeToPacket(cmp);
            }

            ByteBufUtils.writeString(pb, key);

            byte providerId = dat.getProviderID();
            pb.writeByte(providerId);
            pb.writeCompoundTag(cmp);
        }
    }

    @Override
    public IMessage onMessage(PktSyncData message, MessageContext ctx) {
        Wildfire.proxy.scheduleClientside(() -> SyncDataHolder.receiveServerPacket(message.data));
        return null;
    }

}
