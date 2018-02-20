/*******************************************************************************
 * HellFirePvP / Wildfire 2018
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/Wildfire
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.wildfire.common.data;

import hellfirepvp.wildfire.common.auxiliary.ITickHandler;
import hellfirepvp.wildfire.common.network.PacketChannel;
import hellfirepvp.wildfire.common.network.packet.server.PktSyncData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.*;

/**
 * This class is part of the Wildfire Mod
 * The complete source code for this mod can be found on github.
 * Class: SyncDataHolder
 * Created by HellFirePvP
 * Date: 20.02.2018 / 21:16
 */
public class SyncDataHolder implements ITickHandler {

    public static final SyncDataHolder tickInstance = new SyncDataHolder();

    private static Map<String, AbstractData> serverData = new HashMap<>();
    private static Map<String, AbstractData> clientData = new HashMap<>();

    private static List<String> dirtyData = new ArrayList<>();
    private static final Object dirtyLock = new Object();
    private static byte providerCounter = 0;

    private SyncDataHolder() {}

    public static SyncDataHolder getTickInstance() {
        return tickInstance;
    }

    public static void register(AbstractData.AbstractDataProvider<? extends AbstractData> provider) {
        AbstractData.Registry.register(provider);
        AbstractData ad = provider.provideNewInstance();
        ad.setProviderId(provider.getProviderId());
        serverData.put(provider.getKey(), ad);
        ad = provider.provideNewInstance();
        ad.setProviderId(provider.getProviderId());
        clientData.put(provider.getKey(), ad);
    }

    public static byte allocateNewId() {
        byte pId = providerCounter;
        providerCounter++;
        return pId;
    }

    public static <T extends AbstractData> T getDataServer(String key) {
        return (T) serverData.get(key);
    }

    public static <T extends AbstractData> T getDataClient(String key) {
        return (T) clientData.get(key);
    }

    public static <T extends AbstractData> T getData(Side side, String key) {
        switch (side) {
            case CLIENT:
                return getDataClient(key);
            case SERVER:
                return getDataServer(key);
        }
        throw new IllegalArgumentException("Side not defined: " + side);
    }

    public static void markForUpdate(String key) {
        synchronized (dirtyLock) {
            if (!dirtyData.contains(key)) {
                dirtyData.add(key);
            }
        }
    }

    public static void syncAllDataTo(EntityPlayer player) {
        PktSyncData dataSync = new PktSyncData(serverData, true);
        PacketChannel.CHANNEL.sendTo(dataSync, (net.minecraft.entity.player.EntityPlayerMP) player);
    }

    public static void receiveServerPacket(Map<String, AbstractData> data) {
        for (String key : data.keySet()) {
            AbstractData dat = clientData.get(key);
            if (dat != null) {
                dat.handleIncomingData(data.get(key));
            }
        }
    }

    public static void initialize() {

    }

    @Override
    public void tick(TickEvent.Type type, Object... context) {
        if (dirtyData.isEmpty()) return;
        Map<String, AbstractData> pktData = new HashMap<>();
        synchronized (dirtyLock) {
            for (String s : dirtyData) {
                AbstractData d = getDataServer(s);
                pktData.put(s, d);
            }
            dirtyData.clear();
        }
        PktSyncData dataSync = new PktSyncData(pktData, false);
        PacketChannel.CHANNEL.sendToAll(dataSync);
    }

    @Override
    public EnumSet<TickEvent.Type> getHandledTypes() {
        return EnumSet.of(TickEvent.Type.SERVER);
    }

    @Override
    public boolean canFire(TickEvent.Phase phase) {
        return phase == TickEvent.Phase.END;
    }

    @Override
    public String getName() {
        return "Sync Data Holder";
    }
}
