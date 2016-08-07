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
import org.diorite.entity.EntityType;
import org.diorite.inventory.item.ItemStack;
import org.diorite.tileentity.TileEntityMobSpawner;
import org.diorite.utils.math.DioriteRandom;

public class TileEntityMobSpawnerImpl extends TileEntityImpl implements TileEntityMobSpawner
{
    private final Block      block;
    private       EntityType entityType;
    private       int        spawnDelay;

    public TileEntityMobSpawnerImpl(final Block block)
    {
        super(block.getLocation());

        this.block = block;
        this.entityType = EntityType.PIG;
        this.spawnDelay = 200; //TODO: check and set proper default delay
    }
    @Override
    public void doTick(final int tps)
    {
       //TODO: check light level, spawn entity
    }

    @Override
    public Block getBlock()
    {
        return this.block;
    }

    @Override
    public void simulateDrop(final DioriteRandom rand, final Set<ItemStack> drops)
    {
        //TODO
    }

    @Override
    public EntityType getSpawnerType()
    {
        return this.entityType;
    }

    @Override
    public void setSpawnerType(final EntityType type)
    {
        this.entityType = type;
    }

    @Override
    public int getSpawnDelay()
    {
        return this.spawnDelay;
    }

    @Override
    public void setSpawnDelay(final int delay)
    {
        this.spawnDelay = delay;
    }
}
