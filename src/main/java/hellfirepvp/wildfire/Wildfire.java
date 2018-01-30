/*******************************************************************************
 * HellFirePvP / Wildfire 2018
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/Wildfire
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.wildfire;

import net.minecraftforge.fml.common.Mod;
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

    @Mod.Instance(MODID)
    public static Wildfire instance;

    public static Logger log = LogManager.getLogger(NAME);

}