/*******************************************************************************
 * HellFirePvP / Wildfire 2018
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/Wildfire
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.wildfire.client.util;

import hellfirepvp.wildfire.Wildfire;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;

/**
 * This class is part of the Wildfire Mod
 * The complete source code for this mod can be found on github.
 * Class: TextureHelper
 * Created by HellFirePvP
 * Date: 20.02.2018 / 21:49
 */
public class TextureHelper {

    private static final ResourceLocation blackSpaceholder = new ResourceLocation(Wildfire.MODID, "textures/misc/black.png");
    public static ResourceLocation texFontRenderer = new ResourceLocation("textures/font/ascii.png");

    public static ResourceLocation getBlockAtlasTexture() {
        return TextureMap.LOCATION_BLOCKS_TEXTURE;
    }

    public static void refreshTextureBindState() {
        Minecraft.getMinecraft().renderEngine.bindTexture(blackSpaceholder);
    }

    public static void setActiveTextureToAtlasSprite() {
        Minecraft.getMinecraft().renderEngine.bindTexture(getBlockAtlasTexture());
    }

}
