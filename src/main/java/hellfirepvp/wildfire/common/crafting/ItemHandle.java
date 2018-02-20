/*******************************************************************************
 * HellFirePvP / Wildfire 2018
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/Wildfire
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.wildfire.common.crafting;

import hellfirepvp.wildfire.common.util.ByteBufUtils;
import hellfirepvp.wildfire.common.util.ItemUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.common.crafting.CompoundIngredient;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.UniversalBucket;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.util.*;

/**
 * This class is part of the Wildfire Mod
 * The complete source code for this mod can be found on github.
 * Class: ItemHandle
 * Created by HellFirePvP
 * Date: 20.02.2018 / 21:30
 */
public final class ItemHandle {

    private static final Constructor<CompoundIngredient> COMPOUND_CTOR;

    public static ItemHandle EMPTY = new ItemHandle();

    public final Type handleType;

    private List<ItemStack> applicableItems = new LinkedList<>();
    private String oreDictName = null;
    private FluidStack fluidTypeAndAmount = null;

    /*
     * PLEASE do not use this without populating the respective fields...
     */
    private ItemHandle(Type type) {
        this.handleType = type;
    }

    private ItemHandle() {
        this(Type.STACK);
        applicableItems.add(ItemStack.EMPTY);
    }

    public ItemHandle(String oreDictName) {
        this.oreDictName = oreDictName;
        this.handleType = Type.OREDICT;
    }

    public ItemHandle(Fluid fluid) {
        this.fluidTypeAndAmount = new FluidStack(fluid, 1000);
        this.handleType = Type.FLUID;
    }

    public ItemHandle(Fluid fluid, int mbAmount) {
        this.fluidTypeAndAmount = new FluidStack(fluid, mbAmount);
        this.handleType = Type.FLUID;
    }

    public ItemHandle(FluidStack compareStack) {
        this.fluidTypeAndAmount = compareStack.copy();
        this.handleType = Type.FLUID;
    }

    public ItemHandle(@Nonnull ItemStack matchStack) {
        this.applicableItems.add(ItemUtils.copyStackWithSize(matchStack, matchStack.getCount()));
        this.handleType = Type.STACK;
    }

    public ItemHandle(@Nonnull ItemStack... matchStacks) {
        for (ItemStack stack : matchStacks) {
            this.applicableItems.add(ItemUtils.copyStackWithSize(stack, stack.getCount()));
        }
        this.handleType = Type.STACK;
    }

    public ItemHandle(NonNullList<ItemStack> matchStacks) {
        for (ItemStack stack : matchStacks) {
            this.applicableItems.add(ItemUtils.copyStackWithSize(stack, stack.getCount()));
        }
        this.handleType = Type.STACK;
    }

    public static ItemHandle of(Ingredient ingredient) {
        return new ItemHandle(ingredient.getMatchingStacks());
    }

    public NonNullList<ItemStack> getApplicableItems() {
        if(oreDictName != null) {
            NonNullList<ItemStack> stacks = OreDictionary.getOres(oreDictName);

            NonNullList<ItemStack> out = NonNullList.create();
            for (ItemStack oreDictIn : stacks) {
                if (oreDictIn.getItemDamage() == OreDictionary.WILDCARD_VALUE && !oreDictIn.isItemStackDamageable()) {
                    oreDictIn.getItem().getSubItems(oreDictIn.getItem().getCreativeTab(), out);
                } else {
                    out.add(oreDictIn);
                }
            }
            return out;
        } else if(fluidTypeAndAmount != null) {
            return NonNullList.withSize(1, UniversalBucket.getFilledBucket(ForgeModContainer.getInstance().universalBucket, fluidTypeAndAmount.getFluid()));
        } else {
            NonNullList<ItemStack> l = NonNullList.create();
            l.addAll(applicableItems);
            return l;
        }
    }

    public Object getObjectForRecipe() {
        if(oreDictName != null) {
            return oreDictName;
        }
        if(fluidTypeAndAmount != null) {
            return UniversalBucket.getFilledBucket(ForgeModContainer.getInstance().universalBucket, fluidTypeAndAmount.getFluid());
        }
        return applicableItems;
    }

    @SideOnly(Side.CLIENT)
    public NonNullList<ItemStack> getApplicableItemsForRender() {
        return getApplicableItems();
    }

    //CACHE THIS !!!111ELEVEN11!
    public Ingredient getRecipeIngredient() {
        switch (handleType) {
            case OREDICT:
                return new OreIngredient(this.oreDictName);
            case FLUID:
                return Ingredient.fromStacks(FluidUtil.getFilledBucket(new FluidStack(fluidTypeAndAmount.getFluid(), 1000)));
            case STACK:
            default:
                List<Ingredient> ingredients = new ArrayList<>();
                for (ItemStack stack : this.applicableItems) {
                    Ingredient i = Ingredient.fromStacks(stack);
                    if(i != Ingredient.EMPTY) {
                        ingredients.add(i);
                    }
                }
                try {
                    return COMPOUND_CTOR.newInstance(ingredients);
                } catch (Exception e) {}
        }
        return Ingredient.EMPTY;
    }

    @Nullable
    public String getOreDictName() {
        return oreDictName;
    }

    @Nullable
    public FluidStack getFluidTypeAndAmount() {
        return fluidTypeAndAmount;
    }

    public boolean matchCrafting(ItemStack stack) {
        if(stack.isEmpty()) return false;

        switch (handleType) {
            case OREDICT:
                for (int id : OreDictionary.getOreIDs(stack)) {
                    String name = OreDictionary.getOreName(id);
                    if (name != null && name.equals(oreDictName)) {
                        return true;
                    }
                }
                return false;
            case STACK:
                for (ItemStack applicable : applicableItems) {
                    if(ItemUtils.stackEqualsNonNBT(applicable, stack)) {
                        return true;
                    }
                }
                return false;
            case FLUID:
                FluidStack contained = FluidUtil.getFluidContained(stack);
                if(contained == null || contained.getFluid() == null || !contained.getFluid().equals(fluidTypeAndAmount.getFluid())) {
                    return false;
                }
                return ItemUtils.drainFluidFromItem(stack, fluidTypeAndAmount, false).isSuccess();
        }
        return false;
    }

    public void serialize(ByteBuf byteBuf) {
        byteBuf.writeInt(handleType.ordinal());
        switch (handleType) {
            case OREDICT:
                ByteBufUtils.writeString(byteBuf, this.oreDictName);
                break;
            case STACK:
                byteBuf.writeInt(this.applicableItems.size());
                for (ItemStack applicableItem : this.applicableItems) {
                    ByteBufUtils.writeItemStack(byteBuf, applicableItem);
                }
                break;
            case FLUID:
                ByteBufUtils.writeFluidStack(byteBuf, this.fluidTypeAndAmount);
                break;
        }
    }

    public static ItemHandle deserialize(ByteBuf byteBuf) {
        Type type = Type.values()[byteBuf.readInt()];
        ItemHandle handle = new ItemHandle(type);
        switch (type) {
            case OREDICT:
                handle.oreDictName = ByteBufUtils.readString(byteBuf);
                break;
            case STACK:
                int amt = byteBuf.readInt();
                for (int i = 0; i < amt; i++) {
                    handle.applicableItems.add(ByteBufUtils.readItemStack(byteBuf));
                }
                break;
            case FLUID:
                handle.fluidTypeAndAmount = ByteBufUtils.readFluidStack(byteBuf);
                break;
        }
        return handle;
    }

    static {
        Constructor<CompoundIngredient> ctor;
        try {
            ctor = CompoundIngredient.class.getDeclaredConstructor(Collection.class);
        } catch (Exception exc) {
            throw new IllegalStateException("Could not find CompoundIngredient Constructor! Recipes can't be created; Exiting execution! Try with AS and forge alone first! Please report this along with exact forge version and other mods.");
        }
        ctor.setAccessible(true);
        COMPOUND_CTOR = ctor;
    }

    public static enum Type {

        OREDICT,
        STACK,
        FLUID

    }

}
