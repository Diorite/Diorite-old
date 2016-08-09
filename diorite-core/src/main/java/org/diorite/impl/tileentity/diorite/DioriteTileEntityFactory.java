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

package org.diorite.impl.tileentity.diorite;

import static org.diorite.material.BlockMaterialData.*;


import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Function;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import org.diorite.impl.DioriteCore;
import org.diorite.impl.tileentity.ITileEntityFactory;
import org.diorite.impl.tileentity.TileEntityBeaconImpl;
import org.diorite.impl.tileentity.TileEntityBrewingStandImpl;
import org.diorite.impl.tileentity.TileEntityChestImpl;
import org.diorite.impl.tileentity.TileEntityCommandBlockImpl;
import org.diorite.impl.tileentity.TileEntityDispenserImpl;
import org.diorite.impl.tileentity.TileEntityDropperImpl;
import org.diorite.impl.tileentity.TileEntityFurnaceImpl;
import org.diorite.impl.tileentity.TileEntityHopperImpl;
import org.diorite.impl.tileentity.TileEntityImpl;
import org.diorite.impl.tileentity.TileEntityJukeboxImpl;
import org.diorite.impl.tileentity.TileEntityMobSpawnerImpl;
import org.diorite.impl.tileentity.TileEntityNoteBlockImpl;
import org.diorite.impl.tileentity.TileEntitySignImpl;
import org.diorite.impl.tileentity.TileEntitySkullImpl;
import org.diorite.impl.world.WorldImpl;
import org.diorite.block.Beacon;
import org.diorite.block.Block;
import org.diorite.block.BlockLocation;
import org.diorite.block.BlockState;
import org.diorite.block.BrewingStand;
import org.diorite.block.Chest;
import org.diorite.block.CommandBlock;
import org.diorite.block.Dispenser;
import org.diorite.block.Dropper;
import org.diorite.block.Furnace;
import org.diorite.block.Hopper;
import org.diorite.block.Jukebox;
import org.diorite.block.MobSpawner;
import org.diorite.block.NoteBlock;
import org.diorite.block.Sign;
import org.diorite.block.Skull;
import org.diorite.material.BlockMaterialData;
import org.diorite.nbt.NbtTagCompound;

public class DioriteTileEntityFactory implements ITileEntityFactory
{
    private final DioriteCore core;
    private final Map<BlockMaterialData, Function<BlockState, TileEntityImpl>> mapper = new IdentityHashMap<>(32);

    public DioriteTileEntityFactory(final DioriteCore core)
    {
        this.core = core;
    }

    {
        this.mapper.put(BEACON, block -> new TileEntityBeaconImpl((Beacon) block));
        this.mapper.put(BREWING_STAND_BLOCK, block -> new TileEntityBrewingStandImpl((BrewingStand) block));
        this.mapper.put(CHEST, block -> new TileEntityChestImpl((Chest) block));
        this.mapper.put(TRAPPED_CHEST, block -> new TileEntityChestImpl((Chest) block));
        this.mapper.put(COMMAND_BLOCK, block ->  new TileEntityCommandBlockImpl((CommandBlock) block));
        this.mapper.put(DISPENSER, block -> new TileEntityDispenserImpl((Dispenser) block));
        this.mapper.put(DROPPER, block -> new TileEntityDropperImpl((Dropper) block));
        this.mapper.put(FURNACE, block -> new TileEntityFurnaceImpl((Furnace) block));
        this.mapper.put(HOPPER, block -> new TileEntityHopperImpl((Hopper) block));
        this.mapper.put(JUKEBOX, block -> new TileEntityJukeboxImpl((Jukebox) block));
        this.mapper.put(MOB_SPAWNER, block -> new TileEntityMobSpawnerImpl((MobSpawner) block));
        this.mapper.put(NOTEBLOCK, block -> new TileEntityNoteBlockImpl((NoteBlock) block));
        this.mapper.put(WALL_SIGN, block -> new TileEntitySignImpl((Sign) block));
        this.mapper.put(SKULL_BLOCK, block -> new TileEntitySkullImpl((Skull) block));
    }

    @Override
    public TileEntityImpl createTileEntity(final BlockState block)
    {
        final BlockMaterialData mat = block.getType();
        final Function<BlockState, TileEntityImpl> creator = this.mapper.get(mat);

        if (creator == null)
        {
            return null;
        }

        return creator.apply(block);
    }

    @Override
    public TileEntityImpl createTileEntity(final NbtTagCompound nbt, final WorldImpl world)
    {
        final BlockLocation blockLocation = new BlockLocation(nbt.getInt("x"), nbt.getInt("y"), nbt.getInt("z"));
        final Block block = world.getBlock(blockLocation);

        final TileEntityImpl tileEntity = this.createTileEntity(block.getState());
        tileEntity.loadFromNbt(nbt);

        return tileEntity;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).appendSuper(super.toString()).append("core", this.core).append("mapper", this.mapper).toString();
    }
}
