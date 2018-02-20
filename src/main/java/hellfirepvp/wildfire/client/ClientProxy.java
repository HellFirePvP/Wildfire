/*******************************************************************************
 * HellFirePvP / Wildfire 2018
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/Wildfire
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.wildfire.client;

import hellfirepvp.wildfire.Wildfire;
import hellfirepvp.wildfire.client.effect.EffectHandler;
import hellfirepvp.wildfire.client.event.ClientNetworkEventHandler;
import hellfirepvp.wildfire.client.resource.AssetLibrary;
import hellfirepvp.wildfire.common.CommonProxy;
import hellfirepvp.wildfire.common.auxiliary.TickManager;
import hellfirepvp.wildfire.common.block.BlockDynamicColor;
import hellfirepvp.wildfire.common.block.BlockDynamicStateMapper;
import hellfirepvp.wildfire.common.item.base.IMetaItem;
import hellfirepvp.wildfire.common.item.base.render.INBTModel;
import hellfirepvp.wildfire.common.item.base.render.ItemDynamicColor;
import hellfirepvp.wildfire.common.registry.RegistryBlocks;
import hellfirepvp.wildfire.common.registry.RegistryItems;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is part of the Wildfire Mod
 * The complete source code for this mod can be found on github.
 * Class: ClientProxy
 * Created by HellFirePvP
 * Date: 20.02.2018 / 20:38
 */
public class ClientProxy extends CommonProxy {

    private final ClientScheduler scheduler = new ClientScheduler();
    public static boolean connected = false;

    @Override
    public void preInit() {
        MinecraftForge.EVENT_BUS.register(this);
        try {
            ((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(AssetLibrary.resReloadInstance);
        } catch (Exception exc) {
            Wildfire.log.warn("[Wildfire] Could not add AssetLibrary to resource manager! Texture reloading will have no effect on Wildfire textures.");
            AssetLibrary.resReloadInstance.onResourceManagerReload(null);
        }
        OBJLoader.INSTANCE.addDomain(Wildfire.MODID);

        super.preInit();
    }

    @Override
    public void init() {
        super.init();

        registerPendingIBlockColorBlocks();
        registerPendingIItemColorItems();

        MinecraftForge.EVENT_BUS.register(new ClientNetworkEventHandler());
        MinecraftForge.EVENT_BUS.register(EffectHandler.getInstance());
    }

    @SubscribeEvent
    public void registerModels(ModelRegistryEvent event) {
        registerFluidRenderers();
        registerEntityRenderers();
        registerDisplayInformationInit();
        registerTileRenderers();
    }

    private void registerFluidRenderers() {

    }

    private void registerTileRenderers() {

    }

    private void registerEntityRenderers() {

    }

    private void registerPendingIBlockColorBlocks() {
        BlockColors colors = Minecraft.getMinecraft().getBlockColors();
        for (BlockDynamicColor b : RegistryBlocks.pendingIBlockColorBlocks) {
            colors.registerBlockColorHandler(b::getColorMultiplier, (Block) b);
        }
    }

    private void registerPendingIItemColorItems() {
        ItemColors colors = Minecraft.getMinecraft().getItemColors();
        for (ItemDynamicColor i : RegistryItems.pendingDynamicColorItems) {
            colors.registerItemColorHandler(i::getColorForItemStack, (Item) i);
        }
    }

    @Override
    public void scheduleClientside(Runnable r, int tickDelay) {
        scheduler.addRunnable(r, tickDelay);
    }

    private void registerFluidRender(Fluid f) {
        RegistryBlocks.FluidCustomModelMapper mapper = new RegistryBlocks.FluidCustomModelMapper(f);
        Block block = f.getBlock();
        if(block != null) {
            Item item = Item.getItemFromBlock(block);
            if (item != Items.AIR) {
                ModelLoader.registerItemVariants(item);
                ModelLoader.setCustomMeshDefinition(item, mapper);
            } else {
                ModelLoader.setCustomStateMapper(block, mapper);
            }
        }
    }

    @Override
    protected void registerTickHandlers(TickManager manager) {
        super.registerTickHandlers(manager);

        manager.register(scheduler);
    }

    private <T extends TileEntity> void registerTESR(Class<T> tile, TileEntitySpecialRenderer<T> renderer) {
        ClientRegistry.bindTileEntitySpecialRenderer(tile, renderer);
    }

    public void registerDisplayInformationInit() {
        for (RenderInfoItem modelEntry : itemRegister) {
            if (modelEntry.variant) {
                registerVariantName(modelEntry.item, modelEntry.name);
            }
            Item item = modelEntry.item;
            ModelResourceLocation def = new ModelResourceLocation(Wildfire.MODID + ":" + modelEntry.name, "inventory");
            if(item instanceof INBTModel) {
                List<ResourceLocation> out = ((INBTModel) item).getAllPossibleLocations(def);
                ResourceLocation[] arr = new ResourceLocation[out.size()];
                arr = out.toArray(arr);
                ModelBakery.registerItemVariants(item, arr);
                ModelLoader.setCustomMeshDefinition(item, stack -> ((INBTModel) item).getModelLocation(stack, def));
            } else {
                ModelLoader.setCustomModelResourceLocation(item, modelEntry.metadata, def);
            }
        }

        for (RenderInfoBlock modelEntry : blockRegister) {
            if(modelEntry.block instanceof BlockDynamicStateMapper) {
                if(((BlockDynamicStateMapper) modelEntry.block).handleRegisterStateMapper()) {
                    ((BlockDynamicStateMapper) modelEntry.block).registerStateMapper();
                }
            }

            Item item = Item.getItemFromBlock(modelEntry.block);
            ModelResourceLocation def = new ModelResourceLocation(Wildfire.MODID + ":" + modelEntry.name, "inventory");
            if(item instanceof INBTModel) {
                List<ResourceLocation> out = ((INBTModel) item).getAllPossibleLocations(def);
                ResourceLocation[] arr = new ResourceLocation[out.size()];
                arr = out.toArray(arr);
                ModelBakery.registerItemVariants(item, arr);
                ModelLoader.setCustomMeshDefinition(item, (stack -> ((INBTModel) item).getModelLocation(stack, def)));
            } else {
                ModelLoader.setCustomModelResourceLocation(item, modelEntry.metadata, def);
            }
        }
    }

    @Override
    public void registerFromSubItems(Item item, String name) {
        if (item instanceof IMetaItem) {
            int[] additionalMetas = ((IMetaItem) item).getSubItems();
            if (additionalMetas != null) {
                for (int meta : additionalMetas) {
                    registerItemRender(item, meta, name);
                }
            }
            return;
        }
        NonNullList<ItemStack> list = NonNullList.create();
        item.getSubItems(item.getCreativeTab(), list);
        if (list.size() > 0) {
            for (ItemStack i : list) {
                registerItemRender(item, i.getItemDamage(), name);
            }
        } else {
            registerItemRender(item, 0, name);
        }
    }

    @Override
    public void registerVariantName(Item item, String name) {
        ModelBakery.registerItemVariants(item, new ResourceLocation(Wildfire.MODID, name));
    }

    @Override
    public void registerBlockRender(Block block, int metadata, String name) {
        blockRegister.add(new RenderInfoBlock(block, metadata, name));
    }

    @Override
    public void registerItemRender(Item item, int metadata, String name) {
        registerItemRender(item, metadata, name, false);
    }

    @Override
    public void registerItemRender(Item item, int metadata, String name, boolean variant) {
        itemRegister.add(new RenderInfoItem(item, metadata, name, variant));
    }

    private static List<RenderInfoBlock> blockRegister = new ArrayList<>();
    private static List<RenderInfoItem> itemRegister = new ArrayList<>();

    private static class RenderInfoBlock {

        public Block block;
        public int metadata;
        public String name;

        public RenderInfoBlock(Block block, int metadata, String name) {
            this.block = block;
            this.metadata = metadata;
            this.name = name;
        }
    }

    private static class RenderInfoItem {

        public Item item;
        public int metadata;
        public String name;
        public boolean variant;

        public RenderInfoItem(Item item, int metadata, String name, boolean variant) {
            this.item = item;
            this.metadata = metadata;
            this.name = name;
            this.variant = variant;
        }
    }

}
