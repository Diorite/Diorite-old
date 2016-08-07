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

import org.diorite.block.Block;
import org.diorite.block.BlockFace;
import org.diorite.block.Skull;
import org.diorite.material.SkullType;
import org.diorite.tileentity.TileEntitySkull;

public class SkullImpl extends BlockStateImpl implements Skull
{
    private final TileEntitySkull tileEntity;

    public SkullImpl(final Block block)
    {
        super(block);

        this.tileEntity = (TileEntitySkull) block.getWorld().getTileEntity(block.getLocation());
    }

    @Override
    public boolean hasOwner()
    {
        return this.tileEntity.hasOwner();
    }

    @Override
    public String getOwner()
    {
        return this.tileEntity.getOwner();
    }

    @Override
    public void setOwner(final String owner)
    {
        this.tileEntity.setOwner(owner);
    }

    @Override
    public BlockFace getRotation()
    {
        return this.tileEntity.getRotation();
    }

    @Override
    public void setRotation(final BlockFace rotation)
    {
        this.tileEntity.setRotation(rotation);
    }

    @Override
    public SkullType getSkullType()
    {
        return this.tileEntity.getSkullType();
    }

    @Override
    public void setSkullType(final SkullType type)
    {
        this.tileEntity.setSkullType(type);
    }
}
