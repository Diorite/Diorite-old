package org.diorite.impl.entity.diorite;

import java.util.UUID;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import org.diorite.impl.DioriteCore;
import org.diorite.impl.connection.packets.play.client.PacketPlayClientAbilities;
import org.diorite.impl.connection.packets.play.server.PacketPlayServer;
import org.diorite.impl.connection.packets.play.server.PacketPlayServerAbilities;
import org.diorite.impl.connection.packets.play.server.PacketPlayServerNamedEntitySpawn;
import org.diorite.impl.connection.packets.play.server.PacketPlayServerPlayerInfo;
import org.diorite.impl.entity.IEntity;
import org.diorite.impl.entity.IHuman;
import org.diorite.impl.entity.IItem;
import org.diorite.impl.entity.attrib.SimpleAttributeModifier;
import org.diorite.impl.entity.meta.entry.EntityMetadataByteEntry;
import org.diorite.impl.entity.meta.entry.EntityMetadataFloatEntry;
import org.diorite.impl.entity.meta.entry.EntityMetadataIntEntry;
import org.diorite.impl.inventory.PlayerCraftingInventoryImpl;
import org.diorite.impl.inventory.PlayerInventoryImpl;
import org.diorite.Diorite;
import org.diorite.GameMode;
import org.diorite.ImmutableLocation;
import org.diorite.auth.GameProfile;
import org.diorite.cfg.magic.MagicNumbers;
import org.diorite.chat.ChatPosition;
import org.diorite.chat.component.BaseComponent;
import org.diorite.entity.EntityType;
import org.diorite.entity.Human;
import org.diorite.entity.Player;
import org.diorite.entity.attrib.AttributeModifier;
import org.diorite.entity.attrib.AttributeProperty;
import org.diorite.entity.attrib.AttributeType;
import org.diorite.entity.attrib.ModifierOperation;
import org.diorite.entity.data.HandSide;
import org.diorite.entity.data.HandType;
import org.diorite.inventory.EntityEquipment;
import org.diorite.inventory.item.BaseItemStack;
import org.diorite.inventory.item.ItemStack;
import org.diorite.permissions.GroupablePermissionsContainer;
import org.diorite.permissions.PlayerPermissionsContainer;
import org.diorite.utils.math.DioriteMathUtils;
import org.diorite.utils.math.geometry.ImmutableEntityBoundingBox;
import org.diorite.utils.others.NamedUUID;

class HumanImpl extends LivingEntityImpl implements IHuman
{
    private static final float ITEM_DROP_MOD_VEL_Y = 0.02F;

    @SuppressWarnings("MagicNumber")
    public static final ImmutableEntityBoundingBox DIE_SIZE = new ImmutableEntityBoundingBox(0.2F, 0.2F);

    // TODO: move this
    private static final AttributeModifier tempSprintMod = new SimpleAttributeModifier(UUID.fromString("662A6B8D-DA3E-4C1C-8813-96EA6097278D"), null, MagicNumbers.ATTRIBUTES__MODIFIERS__SPRINT, ModifierOperation.ADD_PERCENTAGE, null, null);

    protected final GameProfile         gameProfile;
    protected final PlayerInventoryImpl inventory;

    PacketPlayServerAbilities abilities    = new PacketPlayServerAbilities(false, false, false, false, Player.WALK_SPEED, Player.FLY_SPEED);
    HandSide                  mainHand     = HandSide.RIGHT;
    int                       heldItemSlot = 0;
    GameMode                  gameMode     = GameMode.SURVIVAL;
    NamedUUID namedUUID;

    PlayerPermissionsContainer permissionContainer;

    HumanImpl(final DioriteCore core, final GameProfile gameProfile, final int id, final ImmutableLocation location)
    {
        super(gameProfile.getId(), core, id, location);
        this.aabb = BASE_SIZE.create(this);
        this.gameProfile = gameProfile;
        this.permissionContainer = Diorite.getServerManager().getPermissionsManager().createPlayerContainer(this);
        this.gameMode = this.world.getDefaultGameMode();
        this.inventory = new PlayerInventoryImpl(this, 0); // 0 because this is owner of this inventory, and we need this to update
        this.namedUUID = new NamedUUID(gameProfile.getName(), gameProfile.getId());
    }

    @Override
    public NamedUUID getNamedUUID()
    {
        return this.namedUUID;
    }

    @Override
    public void setNamedUUID(final NamedUUID namedUUID)
    {
        this.namedUUID = namedUUID;
    }

    @Override
    public float getHeadHeight()
    {
        return this.isCrouching() ? CROUCHING_HEAD_HEIGHT : BASE_HEAD_HEIGHT;
    }

    @Override
    public ItemImpl dropItem(final ItemStack itemStack)
    {
        if ((itemStack == null) || (itemStack.getAmount() == 0))
        {
            return null;
        }
        final double newY = (this.y - ITEM_DROP_MOD_Y) + this.getHeadHeight();
        final ItemImpl item = new ItemImpl(UUID.randomUUID(), this.core, IEntity.getNextEntityID(), new ImmutableLocation(this.x, newY, this.z, this.world));
        item.setItemStack(itemStack);
        item.setPickupDelay(IItem.DEFAULT_DROP_PICKUP_DELAY);
        item.setThrower(this.namedUUID);

        float yMod = ITEM_DROP_MOD_Y;
        item.velX = (- DioriteMathUtils.sin((this.yaw / DioriteMathUtils.HALF_CIRCLE_DEGREES) * DioriteMathUtils.F_PI) * DioriteMathUtils.cos((this.pitch / DioriteMathUtils.HALF_CIRCLE_DEGREES) * DioriteMathUtils.F_PI) * yMod);
        item.velZ = (DioriteMathUtils.cos((this.yaw / DioriteMathUtils.HALF_CIRCLE_DEGREES) * DioriteMathUtils.F_PI) * DioriteMathUtils.cos((this.pitch / DioriteMathUtils.HALF_CIRCLE_DEGREES) * DioriteMathUtils.F_PI) * yMod);
        item.velY = ((- DioriteMathUtils.sin((this.pitch / DioriteMathUtils.HALF_CIRCLE_DEGREES) * DioriteMathUtils.F_PI) * yMod) + DioriteMathUtils.F_ONE_OF_10);
        final float randomAngle = this.random.nextFloat() * DioriteMathUtils.F_PI * 2;
        yMod = ITEM_DROP_MOD_VEL_Y * this.random.nextFloat();
        item.velX += Math.cos(randomAngle) * yMod;
        item.velY += (this.random.nextFloat() - this.random.nextFloat()) * DioriteMathUtils.F_ONE_OF_10;
        item.velZ += Math.sin(randomAngle) * yMod;

        // TODO: event/pipeline etc.

        this.world.addEntity(item);
        return item;
    }

    @Override
    public void doTick(final int tps)
    {
        super.doTick(tps);
        this.pickupItems();
    }

    @Override
    public void pickupItems()
    {
        // TODO: maybe don't pickup every tick?
        for (final ItemImpl entity : this.getNearbyEntities(1, 2, 1, ItemImpl.class))
        {
            if (entity.canPickup())
            {
                entity.pickUpItem(this);
            }
        }
    }

    @Override
    public PacketPlayServer getSpawnPacket()
    {
        return new PacketPlayServerNamedEntitySpawn(this);
    }

    @Override
    public void initMetadata()
    {
        super.initMetadata();
        this.metadata.add(new EntityMetadataByteEntry(META_KEY_SKIN_FLAGS, 0));
        this.metadata.add(new EntityMetadataByteEntry(META_KEY_CAPE, 0));
        this.metadata.add(new EntityMetadataFloatEntry(META_KEY_ABSORPTION_HEARTS, 0));
        this.metadata.add(new EntityMetadataIntEntry(META_KEY_SCORE, 0));
    }

    @Override
    public GameProfile getGameProfile()
    {
        return this.gameProfile;
    }

    @Override
    public HandSide getHandSide(final HandType type)
    {
        switch (type)
        {
            case MAIN:
                return this.mainHand;
            case OFF:
                return this.mainHand.getOpposite();
            default:
                throw new AssertionError();
        }
    }

    @Override
    public HandType getHandSide(final HandSide side)
    {
        return (this.mainHand == side) ? HandType.MAIN : HandType.OFF;
    }

    @Override
    public void setMainHand(final HandSide side)
    {
        Validate.notNull(side, "Side of hand can't be null!");
        this.mainHand = side;
    }

    @Override
    public GameMode getGameMode()
    {
        return this.gameMode;
    }

    @Override
    public void setGameMode(final GameMode gameMode)
    {
        if (this.gameMode.equals(gameMode))
        {
            return;
        }
        this.gameMode = gameMode;
        this.core.getPlayersManager().forEach(new PacketPlayServerPlayerInfo(PacketPlayServerPlayerInfo.PlayerInfoAction.UPDATE_GAMEMODE, new PacketPlayServerPlayerInfo.PlayerInfoData(this.getUniqueID(), gameMode)));
    }

    @Override
    public boolean isCrouching()
    {
        return this.metadata.getBoolean(META_KEY_BASIC_FLAGS, BasicFlags.CROUCHED);
    }

    @Override
    public void setCrouching(final boolean isCrouching)
    {
        this.metadata.setBoolean(META_KEY_BASIC_FLAGS, BasicFlags.CROUCHED, isCrouching);
    }

    @Override
    public boolean isSprinting()
    {
        return this.metadata.getBoolean(META_KEY_BASIC_FLAGS, BasicFlags.SPRINTING);
    }

    @Override
    public void setSprinting(final boolean isSprinting)
    {
        final AttributeProperty attrib = this.getProperty(AttributeType.GENERIC_MOVEMENT_SPEED, MagicNumbers.PLAYER__MOVE_SPEED);
        attrib.removeModifier(tempSprintMod.getUuid());
        if (isSprinting)
        {
            attrib.addModifier(tempSprintMod);
        }
        this.metadata.setBoolean(META_KEY_BASIC_FLAGS, BasicFlags.SPRINTING, isSprinting);
    }

    @Override
    public int getHeldItemSlot()
    {
        return this.heldItemSlot;
    }

    @Override
    public void setHeldItemSlot(final int slot)
    {
        this.heldItemSlot = slot;
    }

    @Override
    public boolean canFly()
    {
        return this.abilities.isCanFly();
    }

    @Override
    public void setCanFly(final boolean value)
    {
        this.abilities.setCanFly(value);
        if (! value)
        {
            this.abilities.setFlying(false);
        }
    }

    @Override
    public void setCanFly(final boolean value, final double flySpeed)
    {
        this.abilities.setCanFly(value);
        this.abilities.setFlyingSpeed((float) flySpeed);
        if (! value)
        {
            this.abilities.setFlying(false);
        }
    }

    @Override
    public float getFlySpeed()
    {
        return this.abilities.getFlyingSpeed();
    }

    @Override
    public void setFlySpeed(final double flySpeed)
    {
        this.abilities.setFlyingSpeed((float) flySpeed);
    }

    @Override
    public float getWalkSpeed()
    {
        return this.abilities.getWalkingSpeed();
    }

    @Override
    public void setWalkSpeed(final double walkSpeed)
    {
        this.abilities.setWalkingSpeed((float) walkSpeed);
    }

    @Override
    public PacketPlayServerAbilities getAbilities()
    {
        return this.abilities;
    }

    @Override
    public void setAbilities(final PacketPlayServerAbilities abilities)
    {
        this.abilities = abilities;
    }

    @Override
    public void setAbilities(final PacketPlayClientAbilities abilities)
    {
        // TOOD: I should check what player want change here (cheats)
        this.abilities.setCanFly(abilities.isCanFly());
        this.abilities.setFlying(abilities.isFlying());
        this.abilities.setWalkingSpeed(abilities.getWalkingSpeed());
        this.abilities.setFlyingSpeed(abilities.getFlyingSpeed());
        this.abilities.setInvulnerable(abilities.isInvulnerable());
        this.abilities.setCanInstantlyBuild(abilities.isCanInstantlyBuild());
    }

    @Override
    public PlayerInventoryImpl getInventory()
    {
        return this.inventory;
    }

    @Override
    public GroupablePermissionsContainer getPermissionsContainer()
    {
        return this.permissionContainer;
    }

    @Override
    public EntityType getType()
    {
        return EntityType.PLAYER;
    }

    @Override
    public EntityEquipment getEquipment()
    {
        return this.inventory.getArmorInventory();
    }

    @Override
    public void closeInventory(final int id)
    {
        final PlayerCraftingInventoryImpl ci = this.inventory.getCraftingInventory();
        for (int i = 0, s = ci.size(); i < s; i++)
        {
            final ItemStack itemStack = ci.setItem(i, null);
            if (itemStack != null)
            {
                this.dropItem(new BaseItemStack(itemStack.getMaterial(), itemStack.getAmount()));
            }
        }
        final ItemStack cur = this.inventory.setCursorItem(null);
        if (cur != null)
        {
            this.dropItem(new BaseItemStack(cur.getMaterial(), cur.getAmount()));
        }
    }

    @Override
    public Human getPlayer()
    {
        return this;
    }

    @Override
    public void sendMessage(final ChatPosition position, final BaseComponent component)
    {
        // not needed
    }

    @Override
    public String getName()
    {
        return this.gameProfile.getName();
    }

    @Override
    public UUID getUniqueID()
    {
        return this.gameProfile.getId();
    }

    @Override
    public boolean isOp()
    {
        return this.permissionContainer.isOp();
    }

    @Override
    public boolean setOp(final boolean op)
    {
        return this.permissionContainer.setOp(op);
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).appendSuper(super.toString()).append("gameProfile", this.gameProfile).toString();
    }
}