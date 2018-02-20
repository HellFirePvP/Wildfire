/*******************************************************************************
 * HellFirePvP / Wildfire 2018
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/Wildfire
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.wildfire.common.data.config.entry;

import net.minecraftforge.common.config.Configuration;

/**
 * This class is part of the Wildfire Mod
 * The complete source code for this mod can be found on github.
 * Class: ConfigEntry
 * Created by HellFirePvP
 * Date: 20.02.2018 / 21:25
 */
public abstract class ConfigEntry {

    private final Section section;
    private final String key;

    protected ConfigEntry(Section section, String key) {
        this.section = section;
        this.key = key;
    }

    public String getConfigurationSection() {
        return section.name().toLowerCase();
    }

    public String getKey() {
        return key;
    }

    public abstract void loadFromConfig(Configuration cfg);

    public static enum Section {

        ;

    }

}
