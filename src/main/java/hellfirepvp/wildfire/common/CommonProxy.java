/*******************************************************************************
 * HellFirePvP / Wildfire 2018
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/Wildfire
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.wildfire.common;

import hellfirepvp.wildfire.common.auxiliary.TickManager;
import hellfirepvp.wildfire.common.data.SyncDataHolder;
import hellfirepvp.wildfire.common.event.EventHandlerNetwork;
import hellfirepvp.wildfire.common.network.PacketChannel;
import hellfirepvp.wildfire.common.registry.RegistryEntities;
import hellfirepvp.wildfire.common.registry.RegistryItems;
import hellfirepvp.wildfire.common.registry.internal.InternalRegistryPrimer;
import hellfirepvp.wildfire.common.registry.internal.PrimerEventHandler;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: CommonProxy
 * Created by HellFirePvP
 * Date: 20.02.2018 / 20:36
 */
public class CommonProxy {

    private CommonScheduler commonScheduler = new CommonScheduler();
    public static InternalRegistryPrimer registryPrimer;

    public void preLoadConfigEntries() {

    }

    public void registerConfigDataRegistries() {

    }

    public void preInit() {
        registryPrimer = new InternalRegistryPrimer();
        MinecraftForge.EVENT_BUS.register(new PrimerEventHandler(registryPrimer));

        RegistryItems.setupDefaults();

        PacketChannel.init();

        RegistryEntities.init();
    }

    public void init() {
        MinecraftForge.EVENT_BUS.register(new EventHandlerNetwork());
        MinecraftForge.EVENT_BUS.register(TickManager.getInstance());

        TickManager manager = TickManager.getInstance();
        registerTickHandlers(manager);
    }

    protected void registerTickHandlers(TickManager manager) {
        manager.register(SyncDataHolder.getTickInstance());
        manager.register(commonScheduler);
    }

    public void postInit() {

    }

    public void registerVariantName(Item item, String name) {}

    public void registerBlockRender(Block block, int metadata, String name) {}

    public void registerItemRender(Item item, int metadata, String name) {}

    public <T extends Item> void registerItemRender(T item, int metadata, String name, boolean variant) {}

    public void registerFromSubItems(Item item, String name) {}

    public void scheduleClientside(Runnable r, int tickDelay) {}

    public void scheduleClientside(Runnable r) {
        scheduleClientside(r, 0);
    }

    public void scheduleDelayed(Runnable r, int tickDelay) {
        commonScheduler.addRunnable(r, tickDelay);
    }

    public void scheduleDelayed(Runnable r) {
        scheduleDelayed(r, 0);
    }

}
