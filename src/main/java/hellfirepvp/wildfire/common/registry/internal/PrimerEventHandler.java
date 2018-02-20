/*******************************************************************************
 * HellFirePvP / Wildfire 2018
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/Wildfire
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.wildfire.common.registry.internal;

import hellfirepvp.wildfire.common.registry.RegistryBlocks;
import hellfirepvp.wildfire.common.registry.RegistryItems;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.potion.Potion;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.List;

/**
 * This class is part of the Wildfire Mod
 * The complete source code for this mod can be found on github.
 * Class: InternalRegistryPrimer
 * Created by HellFirePvP
 * Date: 20.02.2018 / 20:40
 */
public class PrimerEventHandler {

    private InternalRegistryPrimer registry;

    public PrimerEventHandler(InternalRegistryPrimer registry) {
        this.registry = registry;
    }

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event) {
        registry.wipe(event.getClass());
        RegistryItems.init();
        fillRegistry(event.getRegistry().getRegistrySuperType(), event.getRegistry());
    }

    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        registry.wipe(event.getClass());
        RegistryBlocks.init();
        RegistryBlocks.initRenderRegistry();
        fillRegistry(event.getRegistry().getRegistrySuperType(), event.getRegistry());
    }

    @SubscribeEvent
    public void registerBiomes(RegistryEvent.Register<Biome> event) {
        registry.wipe(event.getClass());
        fillRegistry(event.getRegistry().getRegistrySuperType(), event.getRegistry());
    }

    @SubscribeEvent
    public void registerPotions(RegistryEvent.Register<Potion> event) {
        registry.wipe(event.getClass());
        fillRegistry(event.getRegistry().getRegistrySuperType(), event.getRegistry());
    }

    @SubscribeEvent
    public void registerEnchantments(RegistryEvent.Register<Enchantment> event) {
        registry.wipe(event.getClass());
        fillRegistry(event.getRegistry().getRegistrySuperType(), event.getRegistry());
    }

    @SubscribeEvent
    public void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        registry.wipe(event.getClass());
        fillRegistry(event.getRegistry().getRegistrySuperType(), event.getRegistry());
    }

    @SubscribeEvent
    public void registerSounds(RegistryEvent.Register<SoundEvent> event) {
        registry.wipe(event.getClass());
        fillRegistry(event.getRegistry().getRegistrySuperType(), event.getRegistry());
    }

    private <T extends IForgeRegistryEntry<T>> void fillRegistry(Class<T> registrySuperType, IForgeRegistry<T> forgeRegistry) {
        List<?> entries = registry.getEntries(registrySuperType);
        if(entries != null) {
            entries.forEach((e) -> forgeRegistry.register((T) e));
        }
    }

}
