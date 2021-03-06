/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016. Diorite (by Bartłomiej Mazur (aka GotoFinal))
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.diorite.impl.block;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import org.diorite.block.Block;
import org.diorite.block.BrewingStand;
import org.diorite.inventory.Inventory;
import org.diorite.inventory.item.ItemStack;
import org.diorite.tileentity.TileEntityBrewingStand;

public class BrewingStandImpl extends BlockStateImpl implements BrewingStand
{
    private final TileEntityBrewingStand tileEntity;

    public BrewingStandImpl(final Block block)
    {
        super(block);
        this.tileEntity = (TileEntityBrewingStand) block.getChunk().getTileEntity(block);
    }

    @Override
    public ItemStack getLeftInput()
    {
        return this.tileEntity.getInventory().getItem(0);
    }

    @Override
    public void setLeftInput(final ItemStack input)
    {
        this.tileEntity.getInventory().setItem(0, input);
    }

    @Override
    public ItemStack getMiddleInput()
    {
        return this.tileEntity.getInventory().getItem(1);
    }

    @Override
    public void setMiddleInput(final ItemStack input)
    {
        this.tileEntity.getInventory().setItem(1, input);
    }

    @Override
    public ItemStack getRightInput()
    {
        return this.tileEntity.getInventory().getItem(2);
    }

    @Override
    public void setRightInput(final ItemStack input)
    {
        this.tileEntity.getInventory().setItem(2, input);
    }

    @Override
    public ItemStack getIngredient()
    {
        return this.tileEntity.getInventory().getItem(3);
    }

    @Override
    public void setIngredient(final ItemStack ingredient)
    {
        this.tileEntity.getInventory().setItem(3, ingredient);
    }

    @Override
    public ItemStack getFuel()
    {
        return this.tileEntity.getInventory().getItem(4);
    }

    @Override
    public void setFuel(final ItemStack fuel)
    {
        this.tileEntity.getInventory().setItem(4, fuel);
    }

    public Inventory getInventory()
    {
        return this.tileEntity.getInventory();
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).appendSuper(super.toString()).append("tileEntity", this.tileEntity).append("inventory", this.tileEntity.getInventory()).toString();
    }
}
