/*******************************************************************************
 * HellFirePvP / Wildfire 2018
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/Wildfire
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.wildfire.common.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import javax.annotation.Nullable;

/**
 * This class is part of the Wildfire Mod
 * The complete source code for this mod can be found on github.
 * Class: BlockDynamicColor
 * Created by HellFirePvP
 * Date: 20.02.2018 / 20:57
 */
public interface BlockDynamicColor {

    //Return -1 for no color multiplication
    public int getColorMultiplier(IBlockState state, @Nullable IBlockAccess access, @Nullable BlockPos pos, int renderPass);

}
