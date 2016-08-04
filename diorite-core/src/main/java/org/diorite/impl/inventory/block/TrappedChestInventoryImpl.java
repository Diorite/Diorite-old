package org.diorite.impl.inventory.block;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import org.diorite.impl.connection.packets.play.clientbound.PacketPlayClientboundWindowItems;
import org.diorite.impl.entity.IPlayer;
import org.diorite.impl.inventory.InventoryImpl;
import org.diorite.impl.inventory.item.ItemStackImplArray;
import org.diorite.block.TrappedChest;
import org.diorite.entity.Player;
import org.diorite.inventory.InventoryType;
import org.diorite.inventory.block.TrappedChestInventory;
import org.diorite.inventory.slot.Slot;

public class TrappedChestInventoryImpl extends InventoryImpl<TrappedChest> implements TrappedChestInventory
{
    private final ItemStackImplArray content = ItemStackImplArray.create(InventoryType.CHEST.getSize());
    private final Slot[]             slots   = new Slot[InventoryType.CHEST.getSize()];

    public TrappedChestInventoryImpl(final TrappedChest holder)
    {
        super(holder);

        for (int i = 0; i < this.slots.length; i++)
        {
            this.slots[i] = Slot.BASE_CONTAINER_SLOT;
        }
    }

    @Override
    public ItemStackImplArray getArray()
    {
        return this.content;
    }

    @Override
    public Slot getSlot(final int slot)
    {
        return this.slots[slot];
    }

    @Override
    public void update(final Player player) throws IllegalArgumentException
    {
        if (! this.viewers.contains(player))
        {
            throw new IllegalArgumentException("Player must be a viewer of inventory.");
        }

        ((IPlayer) player).getNetworkManager().sendPacket(new PacketPlayClientboundWindowItems(this.windowId, this.content));
    }

    @Override
    public void update()
    {
        this.viewers.stream().filter(h -> h instanceof Player).map(h -> (Player) h).forEach(this::update);
    }

    @Override
    public InventoryType getType()
    {
        return InventoryType.CHEST;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).appendSuper(super.toString()).append("content", this.content).append("slots", this.slots).toString();
    }
}
