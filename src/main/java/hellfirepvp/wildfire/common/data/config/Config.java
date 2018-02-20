/*******************************************************************************
 * HellFirePvP / Wildfire 2018
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/Wildfire
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.wildfire.common.data.config;

import hellfirepvp.wildfire.Wildfire;
import hellfirepvp.wildfire.common.data.config.entry.ConfigEntry;
import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * This class is part of the Wildfire Mod
 * The complete source code for this mod can be found on github.
 * Class: Config
 * Created by HellFirePvP
 * Date: 20.02.2018 / 21:22
 */
public class Config {
    private static Configuration latestConfig;

    private static File dirConfigurationRegistries;

    private static List<ConfigEntry> dynamicConfigEntries = new LinkedList<>();
    private static List<ConfigDataAdapter<?>> dataAdapters = new LinkedList<>();


    public static int maxEffectRenderDistance = 64, maxEffectRenderDistanceSq;

    public static int particleAmount = 2;
    public static boolean clientPreloadTextures = true;

    private Config() {}

    public static void load(File file) {
        latestConfig = new Configuration(file);
        latestConfig.load();
        loadData();
        latestConfig.save();
    }

    public static void addDynamicEntry(ConfigEntry entry) {
        if(latestConfig != null) {
            throw new IllegalStateException("Too late to add dynamic configuration entries");
        }
        dynamicConfigEntries.add(entry);
    }

    public static void addDataRegistry(ConfigDataAdapter<?> dataAdapter) {
        for (ConfigDataAdapter<?> cfg : dataAdapters) {
            if(cfg.getDataFileName().equalsIgnoreCase(dataAdapter.getDataFileName())) {
                throw new IllegalArgumentException("Duplicate DataRegistry names! " + cfg.getDataFileName() + " - " + dataAdapter.getDataFileName());
            }
        }
        dataAdapters.add(dataAdapter);
    }

    public static void loadDataRegistries(File cfgDirectory) {
        File dirAS = new File(cfgDirectory, Wildfire.MODID);
        if(!dirAS.exists()) {
            dirAS.mkdirs();
        }
        dirConfigurationRegistries = dirAS;
    }

    public static void loadConfigRegistries(ConfigDataAdapter.LoadPhase phase) {
        for (ConfigDataAdapter<?> cfg : dataAdapters) {
            if(cfg.getLoadPhase() != phase) {
                continue;
            }
            attemptLoad(cfg, new File(dirConfigurationRegistries, cfg.getDataFileName() + ".cfg"));
        }
    }

    private static void attemptLoad(ConfigDataAdapter<?> cfg, File file) {
        String[] out = cfg.serializeDataSet();

        Configuration config = new Configuration(file);
        config.load();
        config.addCustomCategoryComment("data", cfg.getDescription());
        out = config.getStringList("data", "data", out, "");
        for (String str : out) {
            if(cfg.appendDataSet(str) == null) {
                Wildfire.log.warn("[Wildfire] Skipped Entry '" + str + "' for registry " + cfg.getDataFileName() + "! Invalid format!");
            }
        }
        config.save();
    }

    private static void loadData() {
        maxEffectRenderDistance = latestConfig.getInt("maxEffectRenderDistance", "rendering", 64, 1, 512, "Defines how close to the position of a particle/floating texture you have to be in order for it to render.");
        maxEffectRenderDistanceSq = maxEffectRenderDistance * maxEffectRenderDistance;
        clientPreloadTextures = latestConfig.getBoolean("preloadTextures", "rendering", true, "If set to 'true' the mod will preload most of the bigger textures during postInit. This provides a more fluent gameplay experience (as it doesn't need to load the textures when they're first needed), but increases loadtime.");
        particleAmount = latestConfig.getInt("particleAmount", "rendering", 2, 0, 2, "Sets the amount of particles/effects: 0 = minimal (only necessary particles will appear), 1 = lowered (most unnecessary particles will be filtered), 2 = all particles are visible");


        for (ConfigEntry ce : dynamicConfigEntries) {
            ce.loadFromConfig(latestConfig);
        }
    }

}