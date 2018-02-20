/*******************************************************************************
 * HellFirePvP / Wildfire 2018
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/Wildfire
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.wildfire.common.item.block;

import hellfirepvp.wildfire.common.block.BlockCustomName;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

/**
 * This class is part of the Wildfire Mod
 * The complete source code for this mod can be found on github.
 * Class: ItemBlockCustomName
 * Created by HellFirePvP
 * Date: 20.02.2018 / 20:55
 */
public class ItemBlockCustomName extends ItemBlock {

    public ItemBlockCustomName(Block block) {
        super(block);
        setHasSubtypes(true); //Normally the case if you're using multi-type blocks.
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        Block b = getBlock();
        if (b instanceof BlockCustomName) {
            String identifier = ((BlockCustomName) b).getIdentifierForMeta(stack.getItemDamage());
            return super.getUnlocalizedName(stack) + "." + identifier;
        }
        return super.getUnlocalizedName(stack);
    }
}
