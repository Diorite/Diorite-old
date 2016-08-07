package org.diorite.impl.tileentity.diorite;

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
import org.diorite.block.Block;
import org.diorite.block.BlockLocation;
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
    public TileEntityImpl createTileEntity(final Block block)
    {
        //TODO

        BlockMaterialData mat = block.getType();

        TileEntityImpl tileEntity = null;

        if(mat == BlockMaterialData.BEACON)
        {
            tileEntity = new TileEntityBeaconImpl(block);
        }
        else if(mat == BlockMaterialData.BREWING_STAND_BLOCK) //TODO: what about BlockMaterialData.BREWING_STAND?
        {
            tileEntity = new TileEntityBrewingStandImpl(block);
        }
        else if(mat == BlockMaterialData.CHEST || mat == BlockMaterialData.TRAPPED_CHEST)
        {
            tileEntity = new TileEntityChestImpl(block);
        }
        else if(mat == BlockMaterialData.COMMAND_BLOCK)
        {
            tileEntity = new TileEntityCommandBlockImpl(block);
        }
        else if(mat == BlockMaterialData.DISPENSER)
        {
            tileEntity = new TileEntityDispenserImpl(block);
        }
        else if(mat == BlockMaterialData.DROPPER)
        {
            tileEntity = new TileEntityDropperImpl(block);
        }
        else if(mat == BlockMaterialData.FURNACE)
        {
            tileEntity = new TileEntityFurnaceImpl(block);
        }
        else if(mat == BlockMaterialData.HOPPER)
        {
            tileEntity = new TileEntityHopperImpl(block);
        }
        else if(mat == BlockMaterialData.JUKEBOX)
        {
            tileEntity = new TileEntityJukeboxImpl(block);
        }
        else if(mat == BlockMaterialData.MOB_SPAWNER)
        {
            tileEntity = new TileEntityMobSpawnerImpl(block);
        }
        else if(mat == BlockMaterialData.NOTEBLOCK)
        {
            tileEntity = new TileEntityNoteBlockImpl(block);
        }
        else if(mat == BlockMaterialData.WALL_SIGN) //TODO: what about BlockMaterialData.SIGN?
        {
            tileEntity = new TileEntitySignImpl(block);
        }
        else if(mat == BlockMaterialData.SKULL_BLOCK) //TODO: what about BlockMaterialData.SKULL?
        {
            tileEntity = new TileEntitySkullImpl(block);
        }

        return tileEntity;
    }

    @Override
    public TileEntityImpl createTileEntity(final NbtTagCompound nbt, final WorldImpl world)
    {
        final BlockLocation blockLocation = new BlockLocation(nbt.getInt("x"), nbt.getInt("y"), nbt.getInt("z"));
        final Block block = world.getBlock(blockLocation);

        TileEntityImpl tileEntity = createTileEntity(block);
        tileEntity.loadFromNbt(nbt);

        return tileEntity;
    }
}
