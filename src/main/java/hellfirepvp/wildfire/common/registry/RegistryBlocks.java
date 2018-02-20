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
import hellfirepvp.wildfire.common.block.BlockDynamicColor;
import hellfirepvp.wildfire.common.block.BlockVariants;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.LinkedList;
import java.util.List;

/**
 * This class is part of the Wildfire Mod
 * The complete source code for this mod can be found on github.
 * Class: RegistryBlocks
 * Created by HellFirePvP
 * Date: 20.02.2018 / 20:39
 */
public class RegistryBlocks {

    public static List<Block> defaultItemBlocksToRegister = new LinkedList<>();
    public static List<Block> customNameItemBlocksToRegister = new LinkedList<>();
    public static List<BlockDynamicColor> pendingIBlockColorBlocks = new LinkedList<>();

    public static void init() {
        registerFluids();

        registerBlocks();

        registerTileEntities();
    }

    private static void registerFluids() {
        
    }

    //Blocks
    private static void registerBlocks() {
        
    }

    //Called after items are registered.
    //Necessary for blocks that require different models/renders for different metadata values
    public static void initRenderRegistry() {
        
    }

    //Tiles
    private static void registerTileEntities() {
        
    }

    public static void queueCustomNameItemBlock(Block block) {
        customNameItemBlocksToRegister.add(block);
    }

    public static void queueDefaultItemBlock(Block block) {
        defaultItemBlocksToRegister.add(block);
    }

    private static <T extends Block> T registerBlock(T block, String name) {
        CommonProxy.registryPrimer.register(block.setUnlocalizedName(name).setRegistryName(name));
        if(block instanceof BlockDynamicColor) {
            pendingIBlockColorBlocks.add((BlockDynamicColor) block);
        }
        return block;
    }

    public static <T extends Block> T registerBlock(T block) {
        return registerBlock(block, block.getClass().getSimpleName().toLowerCase());
    }

    private static void registerBlockRender(Block block) {
        if(block instanceof BlockVariants) {
            for (IBlockState state : ((BlockVariants) block).getValidStates()) {
                String unlocName = ((BlockVariants) block).getBlockName(state);
                String name = unlocName + "_" + ((BlockVariants) block).getStateName(state);
                Wildfire.proxy.registerVariantName(Item.getItemFromBlock(block), name);
                Wildfire.proxy.registerBlockRender(block, block.getMetaFromState(state), name);
            }
        } else {
            Wildfire.proxy.registerVariantName(Item.getItemFromBlock(block), block.getUnlocalizedName());
            Wildfire.proxy.registerBlockRender(block, 0, block.getUnlocalizedName());
        }
    }

    private static void registerTile(Class<? extends TileEntity> tile, String name) {
        GameRegistry.registerTileEntity(tile, name);
    }

    public static void registerTile(Class<? extends TileEntity> tile) {
        registerTile(tile, tile.getSimpleName().toLowerCase());
    }

    public static class FluidCustomModelMapper extends StateMapperBase implements ItemMeshDefinition {

        private final ModelResourceLocation res;

        public FluidCustomModelMapper(Fluid f) {
            this.res = new ModelResourceLocation(Wildfire.MODID.toLowerCase() + ":blockfluids", f.getName());
        }

        @Override
        public ModelResourceLocation getModelLocation(ItemStack stack) {
            return res;
        }

        @Override
        public ModelResourceLocation getModelResourceLocation(IBlockState state) {
            return res;
        }

    }

}
