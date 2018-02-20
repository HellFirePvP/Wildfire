/*******************************************************************************
 * HellFirePvP / Wildfire 2018
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/Wildfire
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.wildfire.common.data;

import net.minecraft.nbt.NBTTagCompound;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is part of the Wildfire Mod
 * The complete source code for this mod can be found on github.
 * Class: AbstractData
 * Created by HellFirePvP
 * Date: 20.02.2018 / 21:20
 */
public abstract class AbstractData {

    private byte providerId;

    public final void markDirty() {
        AbstractDataProvider<? extends AbstractData> provider = Registry.getProvider(getProviderID());
        if (provider != null) {
            SyncDataHolder.markForUpdate(provider.getKey());
        }
    }

    public final void setProviderId(byte id) {
        this.providerId = id;
    }

    public final byte getProviderID() {
        return providerId;
    }

    public abstract void writeAllDataToPacket(NBTTagCompound compound);

    public abstract void writeToPacket(NBTTagCompound compound);

    public abstract void readRawFromPacket(NBTTagCompound compound);

    public abstract void handleIncomingData(AbstractData serverData);

    public static class Registry {

        private static Map<Byte, AbstractDataProvider<? extends AbstractData>> registry = new HashMap<Byte, AbstractDataProvider<? extends AbstractData>>();

        public static void register(AbstractDataProvider<? extends AbstractData> provider) {
            registry.put(provider.getProviderId(), provider);
        }

        public static AbstractDataProvider<? extends AbstractData> getProvider(Byte id) {
            return registry.get(id);
        }
    }

    public abstract static class AbstractDataProvider<T extends AbstractData> {

        private String key;
        private byte providerId;

        protected AbstractDataProvider(String key, byte providerId) {
            this.key = key;
            this.providerId = providerId;
        }

        public abstract T provideNewInstance();

        public final String getKey() {
            return key;
        }

        public final byte getProviderId() {
            return providerId;
        }

    }

    public abstract static class ProviderAutoAllocate<T extends AbstractData> extends AbstractDataProvider<T> {

        public ProviderAutoAllocate(String key) {
            super(key, SyncDataHolder.allocateNewId());
        }

    }

}
