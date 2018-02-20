/*******************************************************************************
 * HellFirePvP / Wildfire 2018
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/Wildfire
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.wildfire;

import hellfirepvp.wildfire.common.CommonProxy;
import hellfirepvp.wildfire.common.data.config.Config;
import hellfirepvp.wildfire.common.data.config.ConfigDataAdapter;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class is part of the Wildfire Mod
 * The complete source code for this mod can be found on github.
 * Class: Wildfire
 * Created by HellFirePvP
 * Date: 30.01.2018 / 23:38
 */
@Mod(modid = Wildfire.MODID, name = Wildfire.NAME, version = Wildfire.VERSION,
    certificateFingerprint = "certificate-placeholder :^)",
    acceptedMinecraftVersions = "[1.12.2]")
public class Wildfire {

    public static final String MODID = "wildfire";
    public static final String NAME = "Wildfire";
    public static final String VERSION = "0.1";
    public static final String CLIENT_PROXY = "hellfirepvp.wildfire.client.ClientProxy";
    public static final String COMMON_PROXY = "hellfirepvp.wildfire.common.CommonProxy";

    private static boolean devEnvChache = false;

    @Mod.Instance(MODID)
    public static Wildfire instance;

    public static Logger log = LogManager.getLogger(NAME);

    @SidedProxy(clientSide = CLIENT_PROXY, serverSide = COMMON_PROXY)
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        event.getModMetadata().version = VERSION;
        devEnvChache = (Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");

        proxy.preLoadConfigEntries();
        Config.load(event.getSuggestedConfigurationFile());

        proxy.registerConfigDataRegistries();
        Config.loadDataRegistries(event.getModConfigurationDirectory());
        Config.loadConfigRegistries(ConfigDataAdapter.LoadPhase.PRE_INIT);

        proxy.preInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        Config.loadConfigRegistries(ConfigDataAdapter.LoadPhase.INIT);
        MinecraftForge.EVENT_BUS.register(this);

        proxy.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        Config.loadConfigRegistries(ConfigDataAdapter.LoadPhase.POST_INIT);
        proxy.postInit();
    }

    public static boolean isRunningInDevEnvironment() {
        return devEnvChache;
    }

    static {
        FluidRegistry.enableUniversalBucket();
    }

}