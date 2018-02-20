/*******************************************************************************
 * HellFirePvP / Wildfire 2018
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/Wildfire
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.wildfire.client.resource;

import hellfirepvp.wildfire.Wildfire;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * This class is part of the Wildfire Mod
 * The complete source code for this mod can be found on github.
 * Class: AssetLoader
 * Created by HellFirePvP
 * Date: 20.02.2018 / 21:41
 */
public class AssetLoader {

    private AssetLoader() {}

    @SideOnly(Side.CLIENT)
    protected static BindableResource load(AssetLocation location, SubLocation subLocation, String name, String suffix) {
        return new BindableResource(buildResourceString(location, subLocation, name, suffix));
    }

    @SideOnly(Side.CLIENT)
    private static String buildResourceString(AssetLocation location, SubLocation subLocation, String name, String suffix) {
        if(name.endsWith(suffix)) { //In case of derp.
            name = name.substring(0, name.length() - suffix.length());
        }

        StringBuilder builder = new StringBuilder();
        builder.append(Wildfire.MODID).append(':').append(location.location).append("/");
        if (subLocation != null) {
            builder.append(subLocation.getLocation()).append("/");
        }
        builder.append(name).append(suffix);
        return builder.toString();
    }

    @SideOnly(Side.CLIENT)
    protected static BindableResource loadTexture(TextureLocation location, String name) {
        return load(AssetLocation.TEXTURES, location, name, ".png");
    }

    public static interface SubLocation {

        public String getLocation();

    }

    public static enum ModelLocation implements SubLocation {

        OBJ("obj");

        private final String location;

        private ModelLocation(String location) {
            this.location = location;
        }

        @Override
        public String getLocation() {
            return location;
        }

    }

    public static enum TextureLocation implements SubLocation {

        ITEMS("items"),
        BLOCKS("blocks"),
        GUI("gui"),
        MISC("misc"),
        MODELS("models"),
        EFFECT("effect"),
        ENVIRONMENT("environment");

        private final String location;

        private TextureLocation(String location) {
            this.location = location;
        }

        @Override
        public String getLocation() {
            return location;
        }

    }

    public static enum AssetLocation {

        MODELS("models"),
        TEXTURES("textures");

        private final String location;

        private AssetLocation(String location) {
            this.location = location;
        }

    }

}
