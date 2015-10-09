package org.diorite.material.items.tool.tool;

import java.util.Map;

import org.diorite.material.ToolMaterial;
import org.diorite.material.ToolType;
import org.diorite.utils.collections.maps.CaseInsensitiveMap;
import org.diorite.utils.lazy.LazyValue;
import org.diorite.utils.math.DioriteMathUtils;

import gnu.trove.map.TShortObjectMap;
import gnu.trove.map.hash.TShortObjectHashMap;

@SuppressWarnings("ClassHasNoToStringMethod")
public class IronSwordMat extends SwordMat
{
    /**
     * Sub-ids used by diorite/minecraft by default
     */
    public static final int USED_DATA_VALUES = 1;

    public static final IronSwordMat IRON_SWORD = new IronSwordMat();

    private static final Map<String, IronSwordMat>     byName = new CaseInsensitiveMap<>(USED_DATA_VALUES, SMALL_LOAD_FACTOR);
    private static final TShortObjectMap<IronSwordMat> byID   = new TShortObjectHashMap<>(USED_DATA_VALUES, SMALL_LOAD_FACTOR, Short.MIN_VALUE);

    protected final LazyValue<IronSwordMat> next = new LazyValue<>(() -> (this.haveValidDurability()) ? getByDurability(this.getDurability() + 1) : null);
    protected final LazyValue<IronSwordMat> prev = new LazyValue<>(() -> (this.haveValidDurability()) ? getByDurability(this.getDurability() - 1) : null);

    @SuppressWarnings("MagicNumber")
    protected IronSwordMat()
    {
        super("IRON_SWORD", 267, "minecraft:iron_Sword", "NEW", (short) 0, ToolMaterial.IRON);
    }

    protected IronSwordMat(final int durability)
    {
        super(IRON_SWORD.name(), IRON_SWORD.getId(), IRON_SWORD.getMinecraftId(), Integer.toString(durability), (short) durability, ToolMaterial.IRON);
    }

    protected IronSwordMat(final String enumName, final int id, final String minecraftId, final String typeName, final short type, final ToolMaterial toolMaterial, final ToolType toolType)
    {
        super(enumName, id, minecraftId, typeName, type, toolMaterial, toolType);
    }

    protected IronSwordMat(final String enumName, final int id, final String minecraftId, final int maxStack, final String typeName, final short type, final ToolMaterial toolMaterial, final ToolType toolType)
    {
        super(enumName, id, minecraftId, maxStack, typeName, type, toolMaterial, toolType);
    }

    @Override
    public IronSwordMat[] types()
    {
        return new IronSwordMat[]{IRON_SWORD};
    }

    @Override
    public IronSwordMat getType(final String type)
    {
        return getByEnumName(type);
    }

    @Override
    public IronSwordMat getType(final int type)
    {
        return getByID(type);
    }

    @Override
    public IronSwordMat increaseDurability()
    {
        return this.next.get();
    }

    @Override
    public IronSwordMat decreaseDurability()
    {
        return this.prev.get();
    }

    @Override
    public IronSwordMat setDurability(final int durability)
    {
        return this.getType(durability);
    }

    /**
     * Returns one of IronSword sub-type based on sub-id.
     *
     * @param id sub-type id
     *
     * @return sub-type of IronSword.
     */
    public static IronSwordMat getByID(final int id)
    {
        IronSwordMat mat = byID.get((short) id);
        if (mat == null)
        {
            mat = new IronSwordMat(id);
            if ((id > 0) && (id < IRON_SWORD.getBaseDurability()))
            {
                IronSwordMat.register(mat);
            }
        }
        return mat;
    }

    /**
     * Returns one of IronSword sub-type based on durability.
     *
     * @param id durability of type.
     *
     * @return sub-type of IronSword.
     */
    public static IronSwordMat getByDurability(final int id)
    {
        return getByID(id);
    }

    /**
     * Returns one of IronSword-type based on name (selected by diorite team).
     * If item contains only one type, sub-name of it will be this same as name of material.<br>
     * Returns null if name can't be parsed to int and it isn't "NEW" one.
     *
     * @param name name of sub-type
     *
     * @return sub-type of IronSword.
     */
    public static IronSwordMat getByEnumName(final String name)
    {
        final IronSwordMat mat = byName.get(name);
        if (mat == null)
        {
            final Integer idType = DioriteMathUtils.asInt(name);
            if (idType == null)
            {
                return null;
            }
            return getByID(idType);
        }
        return mat;
    }

    /**
     * Register new sub-type, may replace existing sub-types.
     * Should be used only if you know what are you doing, it will not create fully usable material.
     *
     * @param element sub-type to register
     */
    public static void register(final IronSwordMat element)
    {
        byID.put(element.getType(), element);
        byName.put(element.getTypeName(), element);
    }

    /**
     * @return array that contains all sub-types of this item.
     */
    public static IronSwordMat[] ironSwordTypes()
    {
        return new IronSwordMat[]{IRON_SWORD};
    }

    static
    {
        IronSwordMat.register(IRON_SWORD);
    }
}