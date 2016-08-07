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
import org.diorite.block.MobSpawner;
import org.diorite.entity.EntityType;
import org.diorite.tileentity.TileEntityMobSpawner;

public class MobSpawnerImpl extends BlockStateImpl implements MobSpawner
{
    private final TileEntityMobSpawner tileEntity;

    public MobSpawnerImpl(final Block block)
    {
        super(block);

        this.tileEntity = (TileEntityMobSpawner) block.getWorld().getTileEntity(block.getLocation());
    }

    @Override
    public EntityType getSpawnerType()
    {
        return this.tileEntity.getSpawnerType();
    }

    @Override
    public void setSpawnerType(final EntityType type)
    {
        this.tileEntity.setSpawnerType(type);
    }

    @Override
    public int getSpawnDelay()
    {
        return this.tileEntity.getSpawnDelay();
    }

    @Override
    public void setSpawnDelay(final int delay)
    {
        this.tileEntity.setSpawnDelay(delay);
    }
}
