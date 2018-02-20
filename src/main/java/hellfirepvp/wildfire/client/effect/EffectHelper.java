/*******************************************************************************
 * HellFirePvP / Wildfire 2018
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/Wildfire
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.wildfire.client.effect;

import hellfirepvp.wildfire.client.effect.fx.EntityFXFacingDepthParticle;
import hellfirepvp.wildfire.client.effect.fx.EntityFXFacingParticle;
import hellfirepvp.wildfire.common.util.Vector3;

/**
 * This class is part of the Wildfire Mod
 * The complete source code for this mod can be found on github.
 * Class: EffectHelper
 * Created by HellFirePvP
 * Date: 20.02.2018 / 21:50
 */
public class EffectHelper {

    public static EntityFXFacingParticle genericFlareParticle(Vector3 v) {
        return genericFlareParticle(v.getX(), v.getY(), v.getZ());
    }

    public static EntityFXFacingParticle genericFlareParticle(double x, double y, double z) {
        EntityFXFacingParticle p = new EntityFXFacingParticle(x, y, z);
        p.enableAlphaFade(EntityComplexFX.AlphaFunction.PYRAMID).setAlphaMultiplier(0.75F);
        EffectHandler.getInstance().registerFX(p);
        return p;
    }

    public static EntityFXFacingParticle genericGatewayFlareParticle(double x, double y, double z) {
        EntityFXFacingParticle p = new EntityFXFacingParticle.Gateway(x, y, z);
        p.enableAlphaFade(EntityComplexFX.AlphaFunction.PYRAMID).setAlphaMultiplier(0.75F);
        EffectHandler.getInstance().registerFX(p);
        return p;
    }

    public static EntityFXFacingDepthParticle genericDepthIgnoringFlareParticle(double x, double y, double z) {
        EntityFXFacingDepthParticle p = new EntityFXFacingDepthParticle(x, y, z);
        p.enableAlphaFade(EntityComplexFX.AlphaFunction.PYRAMID).setAlphaMultiplier(0.75F);
        EffectHandler.getInstance().registerFX(p);
        return p;
    }

}
