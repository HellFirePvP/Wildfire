/*******************************************************************************
 * HellFirePvP / Wildfire 2018
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/Wildfire
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.wildfire.client.resource;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * This class is part of the Wildfire Mod
 * The complete source code for this mod can be found on github.
 * Class: TextureQuery
 * Created by HellFirePvP
 * Date: 20.02.2018 / 21:43
 */
public class TextureQuery {

    private final AssetLoader.TextureLocation location;
    private final String name;

    private Object resolvedResource;

    public TextureQuery(AssetLoader.TextureLocation location, String name) {
        this.location = location;
        this.name = name;
    }

    @SideOnly(Side.CLIENT)
    public BindableResource resolve() {
        if(resolvedResource == null) {
            resolvedResource = AssetLibrary.loadTexture(location, name);
        }
        return (BindableResource) resolvedResource;
    }

}
