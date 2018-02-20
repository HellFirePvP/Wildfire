/*******************************************************************************
 * HellFirePvP / Wildfire 2018
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/Wildfire
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.wildfire.client.resource;

import hellfirepvp.wildfire.Wildfire;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

/**
 * This class is part of the Wildfire Mod
 * The complete source code for this mod can be found on github.
 * Class: BindableResource
 * Created by HellFirePvP
 * Date: 20.02.2018 / 21:42
 */
@SideOnly(Side.CLIENT)
public class BindableResource {

    private ITextureObject resource = null;
    private String path;

    public BindableResource(String path) {
        this.path = path;
        allocateGlId();
    }

    public String getPath() {
        return path;
    }

    public boolean isInitialized() {
        return resource != null;
    }

    public ITextureObject getResource() {
        return resource;
    }

    @Deprecated
    public void invalidateAndReload() {
        if(resource != null)
            GL11.glDeleteTextures(resource.getGlTextureId());
        resource = null;
    }

    public void allocateGlId() {
        if (resource != null || AssetLibrary.reloading) return;
        resource = new SimpleTexture(new ResourceLocation(path));
        try {
            resource.loadTexture(Minecraft.getMinecraft().getResourceManager());
        } catch (Exception exc) {
            Wildfire.log.warn("[Wildfire] [AssetLibrary] Failed to load texture " + path);
            Wildfire.log.warn("[Wildfire] [AssetLibrary] Please report this issue; include the message above, the following stacktrace as well as instructions on how to reproduce this!");
            exc.printStackTrace();
            resource = TextureUtil.MISSING_TEXTURE;
            return;
        }
        if(Wildfire.isRunningInDevEnvironment()) {
            Wildfire.log.info("[Wildfire] [AssetLibrary] Allocated " + path + " to " + resource.getGlTextureId());
        }
    }

    public void bind() {
        if(AssetLibrary.reloading) return; //we do nothing but wait.
        if (resource == null) {
            allocateGlId();
        }
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, resource.getGlTextureId());
        //GlStateManager.bindTexture(resource.getGlTextureId());
    }

}

