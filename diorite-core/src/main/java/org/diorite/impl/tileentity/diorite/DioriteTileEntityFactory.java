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

import org.diorite.impl.DioriteCore;
import org.diorite.impl.block.BeaconImpl;
import org.diorite.impl.block.BrewingStandImpl;
import org.diorite.impl.block.ChestImpl;
import org.diorite.impl.block.CommandBlockImpl;
import org.diorite.impl.block.DispenserImpl;
import org.diorite.impl.block.DropperImpl;
import org.diorite.impl.block.FurnaceImpl;
import org.diorite.impl.block.HopperImpl;
import org.diorite.impl.block.JukeboxImpl;
import org.diorite.impl.block.MobSpawnerImpl;
import org.diorite.impl.block.NoteBlockImpl;
import org.diorite.impl.block.SignImpl;
import org.diorite.impl.block.SkullImpl;
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
import org.diorite.block.Block;
import org.diorite.block.BlockLocation;
import org.diorite.block.BlockState;
import org.diorite.material.BlockMaterialData;
import org.diorite.nbt.NbtTagCompound;

public class DioriteTileEntityFactory implements ITileEntityFactory
{
    private final DioriteCore core;

    public DioriteTileEntityFactory(final DioriteCore core)
    {
        this.core = core;
    }

    @Override
    public TileEntityImpl createTileEntity(final BlockState block)
    {
        //TODO

        BlockMaterialData mat = block.getType();

        TileEntityImpl tileEntity = null;

        if(mat == BlockMaterialData.BEACON)
        {
            tileEntity = new TileEntityBeaconImpl((BeaconImpl) block);
        }
        else if(mat == BlockMaterialData.BREWING_STAND_BLOCK) //TODO: what about BlockMaterialData.BREWING_STAND?
        {
            tileEntity = new TileEntityBrewingStandImpl((BrewingStandImpl) block);
        }
        else if(mat == BlockMaterialData.CHEST || mat == BlockMaterialData.TRAPPED_CHEST)
        {
            tileEntity = new TileEntityChestImpl((ChestImpl) block);
        }
        else if(mat == BlockMaterialData.COMMAND_BLOCK)
        {
            tileEntity = new TileEntityCommandBlockImpl((CommandBlockImpl) block);
        }
        else if(mat == BlockMaterialData.DISPENSER)
        {
            tileEntity = new TileEntityDispenserImpl((DispenserImpl) block);
        }
        else if(mat == BlockMaterialData.DROPPER)
        {
            tileEntity = new TileEntityDropperImpl((DropperImpl) block);
        }
        else if(mat == BlockMaterialData.FURNACE)
        {
            tileEntity = new TileEntityFurnaceImpl((FurnaceImpl) block);
        }
        else if(mat == BlockMaterialData.HOPPER)
        {
            tileEntity = new TileEntityHopperImpl((HopperImpl) block);
        }
        else if(mat == BlockMaterialData.JUKEBOX)
        {
            tileEntity = new TileEntityJukeboxImpl((JukeboxImpl) block);
        }
        else if(mat == BlockMaterialData.MOB_SPAWNER)
        {
            tileEntity = new TileEntityMobSpawnerImpl((MobSpawnerImpl) block);
        }
        else if(mat == BlockMaterialData.NOTEBLOCK)
        {
            tileEntity = new TileEntityNoteBlockImpl((NoteBlockImpl) block);
        }
        else if(mat == BlockMaterialData.WALL_SIGN) //TODO: what about BlockMaterialData.SIGN?
        {
            tileEntity = new TileEntitySignImpl((SignImpl) block);
        }
        else if(mat == BlockMaterialData.SKULL_BLOCK) //TODO: what about BlockMaterialData.SKULL?
        {
            tileEntity = new TileEntitySkullImpl((SkullImpl) block);
        }

        return tileEntity;
    }

    @Override
    public TileEntityImpl createTileEntity(final NbtTagCompound nbt, final WorldImpl world)
    {
        final BlockLocation blockLocation = new BlockLocation(nbt.getInt("x"), nbt.getInt("y"), nbt.getInt("z"));
        final Block block = world.getBlock(blockLocation);

        TileEntityImpl tileEntity = createTileEntity(block.getState());
        tileEntity.loadFromNbt(nbt);

        return tileEntity;
    }
}
