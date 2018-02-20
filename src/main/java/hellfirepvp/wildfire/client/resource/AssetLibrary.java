/*******************************************************************************
 * HellFirePvP / Wildfire 2018
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/Wildfire
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.wildfire.client.resource;

import hellfirepvp.wildfire.Wildfire;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is part of the Wildfire Mod
 * The complete source code for this mod can be found on github.
 * Class: AssetLibrary
 * Created by HellFirePvP
 * Date: 20.02.2018 / 21:41
 */
public class AssetLibrary implements IResourceManagerReloadListener {

    public static AssetLibrary resReloadInstance = new AssetLibrary();
    public static boolean reloading = false;

    private static Map<AssetLoader.SubLocation, Map<String, BindableResource>> loadedTextures = new HashMap<>();

    private AssetLibrary() {}

    public static BindableResource loadTexture(AssetLoader.TextureLocation location, String name) {
        if(name.endsWith(".png")) {
            throw new IllegalArgumentException("Tried to loadTexture with appended .png from the AssetLibrary!");
        }
        if(!loadedTextures.containsKey(location)) {
            loadedTextures.put(location, new HashMap<>());
        }
        Map<String, BindableResource> resources = loadedTextures.get(location);
        if(resources.containsKey(name)) {
            return resources.get(name);
        }
        BindableResource res = AssetLoader.load(AssetLoader.AssetLocation.TEXTURES, location, name, ".png");
        resources.put(name, res);
        return res;
    }

    @Override
    public void onResourceManagerReload(@Nullable IResourceManager resourceManager) {
        if(reloading) return;
        reloading = true;
        Wildfire.log.info("[Wildfire] [AssetLibrary] Refreshing and Invalidating Resources");
        for (Map<String, BindableResource> map : loadedTextures.values()) {
            for (BindableResource res : map.values()) {
                res.invalidateAndReload(); //Massively unloading all textures.
            }
        }
        reloading = false;

        Wildfire.log.info("[Wildfire] [AssetLibrary] Successfully reloaded and rebound library.");
    }

}
