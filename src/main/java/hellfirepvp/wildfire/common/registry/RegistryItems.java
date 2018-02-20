/*******************************************************************************
 * HellFirePvP / Wildfire 2018
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/Wildfire
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.wildfire.common.registry;

import hellfirepvp.wildfire.Wildfire;
import hellfirepvp.wildfire.common.CommonProxy;
import hellfirepvp.wildfire.common.item.base.IItemVariants;
import hellfirepvp.wildfire.common.item.base.render.ItemDynamicColor;
import hellfirepvp.wildfire.common.item.block.ItemBlockCustomName;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.LinkedList;
import java.util.List;

/**
 * This class is part of the Wildfire Mod
 * The complete source code for this mod can be found on github.
 * Class: RegistryItems
 * Created by HellFirePvP
 * Date: 20.02.2018 / 20:39
 */
public class RegistryItems {

    public static List<ItemDynamicColor> pendingDynamicColorItems = new LinkedList<>();

    public static CreativeTabs creativeTabWildfire;

    public static void setupDefaults() {
        creativeTabWildfire = new CreativeTabs(Wildfire.MODID) {
            @Override
            public ItemStack getTabIconItem() {
                return new ItemStack(Items.FLINT_AND_STEEL);
            }
        };
    }

    public static void init() {
        registerItems();

        registerBlockItems();

        registerDispenseBehavior();
    }

    //"Normal" items
    private static void registerItems() {

    }

    //Items associated to blocks/itemblocks
    private static void registerBlockItems() {
        RegistryBlocks.defaultItemBlocksToRegister.forEach(RegistryItems::registerDefaultItemBlock);
        RegistryBlocks.customNameItemBlocksToRegister.forEach(RegistryItems::registerCustomNameItemBlock);
    }

    private static void registerDispenseBehavior() {

    }

    private static <T extends Block> void registerCustomNameItemBlock(T block) {
        registerItem(new ItemBlockCustomName(block), block.getClass().getSimpleName().toLowerCase());
    }

    private static <T extends Block> void registerDefaultItemBlock(T block) {
        registerDefaultItemBlock(block, block.getClass().getSimpleName().toLowerCase());
    }

    private static <T extends Block> void registerDefaultItemBlock(T block, String name) {
        registerItem(new ItemBlock(block), name);
    }

    private static <T extends Item> T registerItem(T item, String name) {
        item.setUnlocalizedName(name);
        item.setRegistryName(name);
        register(item, name);
        return item;
    }

    private static <T extends Item> T registerItem(T item) {
        String simpleName = item.getClass().getSimpleName().toLowerCase();
        if (item instanceof ItemBlock) {
            simpleName = ((ItemBlock) item).getBlock().getClass().getSimpleName().toLowerCase();
        }
        return registerItem(item, simpleName);
    }

    private static <T extends IForgeRegistryEntry<T>> void register(T item, String name) {
        CommonProxy.registryPrimer.register(item);

        if (item instanceof Item) {
            registerItemInformations((Item) item, name);
            if(item instanceof ItemDynamicColor) {
                pendingDynamicColorItems.add((ItemDynamicColor) item);
            }
        }
    }

    private static <T extends Item> void registerItemInformations(T item, String name) {
        if (item instanceof IItemVariants) {
            for (int i = 0; i < ((IItemVariants) item).getVariants().length; i++) {
                int m = i;
                if (((IItemVariants) item).getVariantMetadatas() != null) {
                    m = ((IItemVariants) item).getVariantMetadatas()[i];
                }
                String vName = name + "_" + ((IItemVariants) item).getVariants()[i];
                if (((IItemVariants) item).getVariants()[i].equals("*")) {
                    vName = name;
                }
                Wildfire.proxy.registerItemRender(item, m, vName, true);
            }
        } else if(!(item instanceof ItemBlockCustomName)) {
            Wildfire.proxy.registerFromSubItems(item, name);
        }
    }

}
