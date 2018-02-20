/*******************************************************************************
 * HellFirePvP / Wildfire 2018
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/Wildfire
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.wildfire.common.registry.internal;

import com.google.common.collect.Lists;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is part of the Wildfire Mod
 * The complete source code for this mod can be found on github.
 * Class: InternalRegistryPrimer
 * Created by HellFirePvP
 * Date: 20.02.2018 / 20:40
 */
public class InternalRegistryPrimer {

    private Map<Class<?>, List<IForgeRegistryEntry<?>>> primed = new HashMap<>();

    public <V extends IForgeRegistryEntry<V>> V register(V entry) {
        Class<V> type = entry.getRegistryType();
        List<IForgeRegistryEntry<?>> entries = primed.get(type);
        if(entries == null) {
            entries = Lists.newLinkedList();
            primed.put(type, entries);
        }
        entries.add(entry);
        return entry;
    }

    <T extends IForgeRegistryEntry<T>> List<?> getEntries(Class<T> type) {
        return primed.get(type);
    }

    void wipe(Class<?> type) {
        primed.remove(type);
    }

}
