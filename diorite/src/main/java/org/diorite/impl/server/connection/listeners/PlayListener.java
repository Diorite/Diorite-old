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

package org.diorite.impl.server.connection.listeners;

import java.util.Locale;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;

import org.diorite.impl.CoreMain;
import org.diorite.impl.DioriteCore;
import org.diorite.impl.connection.CoreNetworkManager;
import org.diorite.impl.connection.packets.Packet;
import org.diorite.impl.connection.packets.play.PacketPlayClientboundListener;
import org.diorite.impl.connection.packets.play.PacketPlayServerboundListener;
import org.diorite.impl.connection.packets.play.clientbound.PacketPlayClientboundDisconnect;
import org.diorite.impl.connection.packets.play.clientbound.PacketPlayClientboundEntityHeadRotation;
import org.diorite.impl.connection.packets.play.clientbound.PacketPlayClientboundEntityLook;
import org.diorite.impl.connection.packets.play.serverbound.PacketPlayServerboundAbilities;
import org.diorite.impl.connection.packets.play.serverbound.PacketPlayServerboundArmAnimation;
import org.diorite.impl.connection.packets.play.serverbound.PacketPlayServerboundBlockDig;
import org.diorite.impl.connection.packets.play.serverbound.PacketPlayServerboundBlockDig.BlockDigAction;
import org.diorite.impl.connection.packets.play.serverbound.PacketPlayServerboundBlockPlace;
import org.diorite.impl.connection.packets.play.serverbound.PacketPlayServerboundChat;
import org.diorite.impl.connection.packets.play.serverbound.PacketPlayServerboundCloseWindow;
import org.diorite.impl.connection.packets.play.serverbound.PacketPlayServerboundCommand;
import org.diorite.impl.connection.packets.play.serverbound.PacketPlayServerboundCustomPayload;
import org.diorite.impl.connection.packets.play.serverbound.PacketPlayServerboundEnchantItem;
import org.diorite.impl.connection.packets.play.serverbound.PacketPlayServerboundEntityAction;
import org.diorite.impl.connection.packets.play.serverbound.PacketPlayServerboundFlying;
import org.diorite.impl.connection.packets.play.serverbound.PacketPlayServerboundHeldItemSlot;
import org.diorite.impl.connection.packets.play.serverbound.PacketPlayServerboundKeepAlive;
import org.diorite.impl.connection.packets.play.serverbound.PacketPlayServerboundLook;
import org.diorite.impl.connection.packets.play.serverbound.PacketPlayServerboundPosition;
import org.diorite.impl.connection.packets.play.serverbound.PacketPlayServerboundPositionLook;
import org.diorite.impl.connection.packets.play.serverbound.PacketPlayServerboundResourcePackStatus;
import org.diorite.impl.connection.packets.play.serverbound.PacketPlayServerboundSetCreativeSlot;
import org.diorite.impl.connection.packets.play.serverbound.PacketPlayServerboundSettings;
import org.diorite.impl.connection.packets.play.serverbound.PacketPlayServerboundSpectate;
import org.diorite.impl.connection.packets.play.serverbound.PacketPlayServerboundSteerBoat;
import org.diorite.impl.connection.packets.play.serverbound.PacketPlayServerboundSteerVehicle;
import org.diorite.impl.connection.packets.play.serverbound.PacketPlayServerboundTabComplete;
import org.diorite.impl.connection.packets.play.serverbound.PacketPlayServerboundTeleportAccept;
import org.diorite.impl.connection.packets.play.serverbound.PacketPlayServerboundTransaction;
import org.diorite.impl.connection.packets.play.serverbound.PacketPlayServerboundUpdateSign;
import org.diorite.impl.connection.packets.play.serverbound.PacketPlayServerboundUseEntity;
import org.diorite.impl.connection.packets.play.serverbound.PacketPlayServerboundUseItem;
import org.diorite.impl.connection.packets.play.serverbound.PacketPlayServerboundVehicleMove;
import org.diorite.impl.connection.packets.play.serverbound.PacketPlayServerboundWindowClick;
import org.diorite.impl.entity.IEntity;
import org.diorite.impl.entity.IPlayer;
import org.diorite.impl.input.InputAction;
import org.diorite.impl.input.InputActionType;
import org.diorite.impl.inventory.PlayerInventoryImpl;
import org.diorite.GameMode;
import org.diorite.chat.component.BaseComponent;
import org.diorite.entity.Entity;
import org.diorite.entity.data.HandType;
import org.diorite.event.EventType;
import org.diorite.event.entity.EntityDamageByEntityEvent;
import org.diorite.event.entity.EntityDamageEvent.DamageCause;
import org.diorite.event.player.PlayerArmAnimationEvent;
import org.diorite.event.player.PlayerBlockDestroyEvent;
import org.diorite.event.player.PlayerBlockPlaceEvent;
import org.diorite.event.player.PlayerInteractEvent;
import org.diorite.event.player.PlayerInteractEvent.Action;
import org.diorite.event.player.PlayerInventoryClickEvent;
import org.diorite.inventory.ClickType;
import org.diorite.inventory.PlayerInventory;
import org.diorite.inventory.item.ItemStack;
import org.diorite.material.items.ArmorMat;
import org.diorite.world.chunk.Chunk;

public class PlayListener implements PacketPlayServerboundListener
{
    private final DioriteCore        core;
    private final CoreNetworkManager networkManager;
    private final IPlayer            player;

    public PlayListener(final DioriteCore core, final CoreNetworkManager networkManager, final IPlayer player)
    {
        this.core = core;
        this.networkManager = networkManager;
        this.player = player;
    }

    @Override
    public void handle(final PacketPlayServerboundKeepAlive packet)
    {
        this.networkManager.updateKeepAlive();
    }

    @Override
    public void handle(final PacketPlayServerboundSettings packet)
    {
//        final byte oldViewDistance = this.player.getViewDistance();
        this.core.sync(() ->
        {
            this.player.setViewDistance(packet.getViewDistance());
            this.player.setMainHand(packet.getHandSide());
            this.player.setPreferredLocale(Locale.forLanguageTag(packet.getLocale().replace('_', '-')));
        }, this.player);
//        if (oldViewDistance != this.player.getViewDistance())
//        {
//            this.player.getPlayerChunks().wantUpdate();
//        }
        // TODO: implement
    }

    @Override
    public void handle(final PacketPlayServerboundCustomPayload packet)
    {
        // TODO: implement
    }

    @Override
    public void handle(final PacketPlayServerboundHeldItemSlot packet)
    {
        this.core.sync(() -> this.player.setHeldItemSlot(packet.getSlot()), this.player);
    }


    @Override
    public void handle(final PacketPlayServerboundFlying packet)
    {
        // TODO: implement
    }

    @Override
    public void handle(final PacketPlayServerboundPositionLook packet)
    {
        // TODO: don't sync packets when client sends too much of them
        final IPlayer player = this.player;
        if ((player.getX() == packet.getX()) && (player.getY() == packet.getY()) && (player.getZ() == packet.getZ()))
        {
            if ((player.getYaw() == packet.getYaw()) && (player.getPitch() == packet.getPitch()))
            {
                return;
            }
            this.core.sync(() -> player.setRotation(packet.getYaw(), packet.getPitch()), player);
            return;
        }
        if ((player.getYaw() == packet.getYaw()) && (player.getPitch() == packet.getPitch()))
        {
            this.core.sync(() -> player.setPosition(packet.getX(), packet.getY(), packet.getZ()), player);
            return;
        }
        this.core.sync(() -> player.setPositionAndRotation(packet.getX(), packet.getY(), packet.getZ(), packet.getYaw(), packet.getPitch()), player);
        this.core.getPlayersManager().forEachExcept(this.player, p -> p.getWorld().equals(this.player.getWorld()), this.packetAndHeadRotation(this.player, new PacketPlayClientboundEntityLook(this.player.getId(), player.getYaw(), player.getPitch(), player.isOnGround())));
    }

    @Override
    public void handle(final PacketPlayServerboundPosition packet)
    {
        final IPlayer player = this.player;
        if ((player.getX() == packet.getX()) && (player.getY() == packet.getY()) && (player.getZ() == packet.getZ()))
        {
            return;
        }
        // TODO: don't sync packets when client sends too much of them
        this.core.sync(() -> player.setPosition(packet.getX(), packet.getY(), packet.getZ()), player);
    }

    @Override
    public void handle(final PacketPlayServerboundLook packet)
    {
        final IPlayer player = this.player;
        if ((player.getYaw() == packet.getYaw()) && (player.getPitch() == packet.getPitch()))
        {
            return;
        }
        // TODO: don't sync packets when client sends too much of them
        this.core.sync(() -> player.setRotation(packet.getYaw(), packet.getPitch()), player);
        this.core.getPlayersManager().forEachExcept(this.player, p -> p.getWorld().equals(this.player.getWorld()), this.packetAndHeadRotation(this.player, new PacketPlayClientboundEntityLook(this.player.getId(), player.getYaw(), player.getPitch(), player.isOnGround())));
    }

    private Packet<PacketPlayClientboundListener>[] packetAndHeadRotation(final IEntity e, final Packet<PacketPlayClientboundListener> base)
    {
        final Packet<PacketPlayClientboundListener>[] packets = new Packet[2];

        packets[0] = base;
        packets[1] = new PacketPlayClientboundEntityHeadRotation(e.getId(), e.getYaw());

        return packets;
    }

    @Override
    public void handle(final PacketPlayServerboundChat packet)
    {
        this.player.chat(packet.getContent());
    }

    @Override
    public void handle(final PacketPlayServerboundTabComplete packet)
    {
        this.core.getInputThread().add(new InputAction(packet.getContent(), packet.isAssumeCommand(), this.player, InputActionType.TAB_COMPLETE));
    }

    @Override
    public void handle(final PacketPlayServerboundAbilities packet)
    {
        this.core.sync(() -> this.player.setAbilitiesFromClient(packet), this.player);
    }

    @Override
    public void handle(final PacketPlayServerboundResourcePackStatus packet)
    {
        // TODO This is not needed? Maybe create event or something other...
    }

    @Override
    public void handle(final PacketPlayServerboundSetCreativeSlot packet)
    {
        if (this.player.getGameMode().equals(GameMode.CREATIVE))
        {
            if (packet.getSlot() == -1)
            {
                this.player.dropItem(packet.getItem());
            }
            else
            {
                this.player.getInventory().setItem(packet.getSlot(), packet.getItem());
            }
        }
//        else
//        {
//            // TODO: maybe do something about this?
//        }
    }

    @Override
    public void handle(final PacketPlayServerboundSpectate packet)
    {
        // TODO
    }

    @Override
    public void handle(final PacketPlayServerboundEnchantItem packet)
    {
        // TODO
    }

    @Override
    public void handle(final PacketPlayServerboundSteerVehicle packet)
    {
        // TODO
    }

    @Override
    public void handle(final PacketPlayServerboundTransaction packet)
    {
        // TODO
    }

    @Override
    public void handle(final PacketPlayServerboundUpdateSign packet)
    {
        // TODO
    }

    @Override
    public void handle(final PacketPlayServerboundUseEntity packet)
    {
        final Entity victim = this.player.getWorld().getEntityTrackers().getTracker(packet.getTargetEntity()).getTracker();
        EventType.callEvent(new EntityDamageByEntityEvent(victim, this.player, DamageCause.ENTITY_ATTACK));
    }

    @Override
    public void handle(final PacketPlayServerboundTeleportAccept packet)
    {
        CoreMain.debug(packet);
    }

    @Override
    public void handle(final PacketPlayServerboundUseItem packet)
    {
        final ItemStackWithSlot itemWithSlot = this.getItemFromHand(this.player.getInventory(), packet.getHandType());
        if (itemWithSlot.item.getMaterial() instanceof ArmorMat)
        {
            this.core.sync(() -> EventType.callEvent(new PlayerInventoryClickEvent(this.player, (short) - 2, - 1, itemWithSlot.slot, ClickType.SHIFT_MOUSE_RIGHT)), this.player);
        }
        CoreMain.debug(packet);
        // TODO
    }

    @Override
    public void handle(final PacketPlayServerboundVehicleMove packet)
    {
        // TODO
    }

    @Override
    public void handle(final PacketPlayServerboundSteerBoat packet)
    {
        // TODO
    }

    @Override
    public void handle(final PacketPlayServerboundEntityAction packet)
    {
        this.core.sync(() -> packet.getEntityAction().doAction(this.player, packet.getJumpBoost()), this.player);
    }

    @Override
    public void handle(final PacketPlayServerboundArmAnimation packet)
    {
        this.core.sync(() -> EventType.callEvent(new PlayerArmAnimationEvent(this.player, packet.getHandType())), this.player);
    }

    @Override
    public void handle(final PacketPlayServerboundBlockDig packet)
    {
        this.core.sync(() ->
        {
            if ((packet.getAction() == BlockDigAction.FINISH_DIG) || ((packet.getAction() == BlockDigAction.START_DIG) && this.player.getGameMode().equals(GameMode.CREATIVE)))
            {
                EventType.callEvent(new PlayerBlockDestroyEvent(this.player, packet.getBlockLocation().setWorld(this.player.getWorld()).getBlock()));
            }

            if (packet.getAction() == BlockDigAction.START_DIG)
            {
                this.core.sync(() -> EventType.callEvent(new PlayerInteractEvent(this.player, Action.LEFT_CLICK_ON_BLOCK, packet.getBlockLocation().setWorld(this.player.getWorld()).getBlock())));
            }

            if (packet.getAction() == BlockDigAction.DROP_ITEM)
            {
                this.core.sync(() -> EventType.callEvent(new PlayerInventoryClickEvent(this.player, (short) - 1, - 1, this.player.getInventory().getHotbarInventory().getSlotOffset() + this.player.getInventory().getHeldItemSlot(), ClickType.DROP_KEY)), this.player);
            }

            if (packet.getAction() == BlockDigAction.DROP_ITEM_STACK)
            {
                this.core.sync(() -> EventType.callEvent(new PlayerInventoryClickEvent(this.player, (short) - 1, - 1, this.player.getInventory().getHotbarInventory().getSlotOffset() + this.player.getInventory().getHeldItemSlot(), ClickType.CTRL_DROP_KEY)), this.player);
            }
            if (packet.getAction() == BlockDigAction.SWAP_OFF_HAND)
            {
                this.core.sync(() -> EventType.callEvent(new PlayerInventoryClickEvent(this.player, (short) - 1, - 1, this.player.getInventory().getHotbarInventory().getSlotOffset() + this.player.getInventory().getHeldItemSlot(), ClickType.SWAP_OFF_HAND)), this.player);
            }

            // TODO: implement
        }, this.player);
    }

    @Override
    public void handle(final PacketPlayServerboundBlockPlace packet)
    {
        CoreMain.debug(packet);

        if (packet.getCursorPos().getBlockFace() == null)
        {
            return;
        }
        this.core.sync(() -> EventType.callEvent(new PlayerInteractEvent(this.player, Action.RIGHT_CLICK_ON_BLOCK, packet.getLocation().setWorld(this.player.getWorld()).getBlock())));

        final PlayerBlockPlaceEvent event = new PlayerBlockPlaceEvent(this.player, packet.getLocation().setWorld(this.player.getWorld()).getBlock().getRelative(packet.getCursorPos().getBlockFace()), packet.getCursorPos().getHandType());
        final int y = packet.getLocation().getY() + packet.getCursorPos().getBlockFace().getModY();
        if ((y >= Chunk.CHUNK_FULL_HEIGHT) || (y < 0))
        {
            event.setCancelled(true);
        }
        else
        {
            final ItemStack item = this.getItemFromHand(this.player.getInventory(), event.getHand()).item;

            if ((item == null) || (item.getAmount() < 1))
            {
                event.setCancelled(true);
            }
        }

        this.core.sync(() -> EventType.callEvent(event), this.player);
    }

    @Override
    public void handle(final PacketPlayServerboundCommand packet)
    {
        CoreMain.debug(packet); // TODO
    }

    @Override
    public void handle(final PacketPlayServerboundCloseWindow packet)
    {
        this.core.sync(() -> this.player.closeInventory(packet.getId()));
    }

    @Override
    public void handle(final PacketPlayServerboundWindowClick p)
    {
        if (p.getClickType() == null)
        {
            CoreMain.debug("Unknown click type in PacketPlayInWindowClick.");
            return;
        }
        this.core.sync(() -> EventType.callEvent(new PlayerInventoryClickEvent(this.player, p.getActionNumber(), p.getId(), p.getClickedSlot(), p.getClickType())), this.player);
    }

    public CoreNetworkManager getNetworkManager()
    {
        return this.networkManager;
    }

    @Override
    public DioriteCore getCore()
    {
        return this.core;
    }

    @Override
    public void disconnect(final BaseComponent message)
    {
        this.core.getPlayersManager().playerQuit(this.player);

        this.networkManager.sendPacket(new PacketPlayClientboundDisconnect(message));
        this.networkManager.close(message, true);
        // TODO: implement
    }

    @Override
    public Logger getLogger()
    {
        return this.core.getLogger();
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).appendSuper(super.toString()).append("core", this.core).append("networkManager", this.networkManager).toString();
    }

    @Override
    public IPlayer getPlayer()
    {
        return this.player;
    }

    private ItemStackWithSlot getItemFromHand(final PlayerInventory inv, final HandType hand)
    {
        if (hand == HandType.MAIN)
        {
            return new ItemStackWithSlot(inv.getItemInHand(), inv.getHeldItemSlot() + inv.getHotbarInventory().getSlotOffset());
        }
        else if (hand == HandType.OFF)
        {
            return new ItemStackWithSlot(inv.getItem(PlayerInventoryImpl.SECOND_HAND_SLOT), PlayerInventoryImpl.SECOND_HAND_SLOT);
        }
        else
        {
            throw new UnsupportedOperationException("Unknown HandType");
        }
    }

    private class ItemStackWithSlot
    {
        public final ItemStack item;
        public final int       slot;

        ItemStackWithSlot(final ItemStack item, final int slot)
        {
            this.item = item;
            this.slot = slot;
        }
    }
}
