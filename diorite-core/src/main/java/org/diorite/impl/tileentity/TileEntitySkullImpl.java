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

package org.diorite.impl.tileentity;

import java.util.Set;

import org.diorite.block.Block;
import org.diorite.block.BlockFace;
import org.diorite.block.Skull;
import org.diorite.inventory.item.ItemStack;
import org.diorite.material.SkullType;
import org.diorite.tileentity.TileEntitySkull;
import org.diorite.utils.math.DioriteRandom;

public class TileEntitySkullImpl extends TileEntityImpl implements TileEntitySkull
{
    private final Skull     block;
    private       BlockFace rotation;
    private       SkullType skullType;

    public TileEntitySkullImpl(final Skull block)
    {
        super(block.getLocation());

        this.block = block;
        rotation = BlockFace.SELF;
        this.skullType = SkullType.CREEPER; //TODO: is "CREEPER" default skull type?
    }

    @Override
    public void doTick(final int tps)
    {
        //TODO
    }

    @Override
    public Block getBlock()
    {
        return this.block.getBlock();
    }

    @Override
    public void simulateDrop(final DioriteRandom rand, final Set<ItemStack> drops)
    {
        //TODO
    }

    @Override
    public boolean hasOwner()
    {
        return false; //TODO
    }

    @Override
    public String getOwner()
    {
        return null; //TODO
    }

    @Override
    public void setOwner(final String owner)
    {
        //TODO
    }

    @Override
    public BlockFace getRotation()
    {
        return this.rotation;
    }

    @Override
    public void setRotation(final BlockFace rotation)
    {
        this.rotation = rotation;
    }

    @Override
    public SkullType getSkullType()
    {
        return this.skullType;
    }

    @Override
    public void setSkullType(final SkullType type)
    {
        this.skullType = type;
    }
}
