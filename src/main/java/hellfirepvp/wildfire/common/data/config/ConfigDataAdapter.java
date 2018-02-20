/*******************************************************************************
 * HellFirePvP / Wildfire 2018
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/Wildfire
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.wildfire.common.data.config;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * This class is part of the Wildfire Mod
 * The complete source code for this mod can be found on github.
 * Class: ConfigDataAdapter
 * Created by HellFirePvP
 * Date: 20.02.2018 / 21:26
 */
public interface ConfigDataAdapter<T extends ConfigDataAdapter.DataSet> {

    public Iterable<T> getDefaultDataSets();

    public String getDataFileName();

    public String getDescription();

    /**
     * Try add a entry to the data-set. The return value defines what happened:
     * - null: Adding the element failed due to an error in the format
     * - Empty optional: Adding the element failed, however reason being contextual information or pack-configurations.
     *          Does not omit the generic format-error message, suggesting the method already gave appropiate information
     * - Optional with value: Everything went fine.
     */
    @Nullable
    public Optional<T> appendDataSet(String str);

    default public LoadPhase getLoadPhase() {
        return LoadPhase.PRE_INIT;
    }

    @Nonnull
    default public String[] serializeDataSet() {
        List<String> defaultValueStrings = new LinkedList<>();
        for (T data : getDefaultDataSets()) {
            defaultValueStrings.add(data.serialize());
        }
        String[] out = new String[defaultValueStrings.size()];
        return defaultValueStrings.toArray(out);
    }

    public static interface DataSet {

        @Nonnull
        public String serialize();

    }

    public static enum LoadPhase {

        PRE_INIT,
        INIT,
        POST_INIT

    }

}
