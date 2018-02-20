/*******************************************************************************
 * HellFirePvP / Wildfire 2018
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/Wildfire
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.wildfire.client.effect;

/**
 * This class is part of the Wildfire Mod
 * The complete source code for this mod can be found on github.
 * Class: IComplexEffect
 * Created by HellFirePvP
 * Date: 20.02.2018 / 21:32
 */
public interface IComplexEffect {

    public boolean canRemove();

    public boolean isRemoved();

    public void flagAsRemoved();

    public void clearRemoveFlag();

    public RenderTarget getRenderTarget();

    public void render(float pTicks);

    public void tick();

    //Valid layers: 0, 1, 2
    //Lower layers are rendered first.
    default public int getLayer() {
        return 0;
    }

    public static enum RenderTarget {

        RENDERLOOP

    }

    public static interface PreventRemoval {}

}
