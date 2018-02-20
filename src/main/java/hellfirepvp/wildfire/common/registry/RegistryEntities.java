/*******************************************************************************
 * HellFirePvP / Wildfire 2018
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/Wildfire
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.wildfire.common.registry;

import hellfirepvp.wildfire.Wildfire;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

/**
 * This class is part of the Wildfire Mod
 * The complete source code for this mod can be found on github.
 * Class: RegistryEntities
 * Created by HellFirePvP
 * Date: 20.02.2018 / 21:10
 */
public class RegistryEntities {

    public static void init() {
        registerEntities();
    }

    private static void registerEntities() {
        int modEid = 0;

        //registerEntity(SpellProjectile.class, "EntitySpellProjectile", modEid++, 128, 1, true);
    }

    private static void registerEntity(Class<? extends Entity> entityClass, String name, int id, int trackingRange, int updateFreq, boolean sendVelUpdates) {
        EntityRegistry.registerModEntity(new ResourceLocation(Wildfire.MODID, name.toLowerCase()), entityClass, name, id, Wildfire.instance, trackingRange, updateFreq, sendVelUpdates);
    }

}
