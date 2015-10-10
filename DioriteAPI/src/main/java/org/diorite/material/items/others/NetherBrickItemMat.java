package org.diorite.material.items.others;

import java.util.Map;

import org.diorite.material.ItemMaterialData;
import org.diorite.utils.collections.maps.CaseInsensitiveMap;

import gnu.trove.map.TShortObjectMap;
import gnu.trove.map.hash.TShortObjectHashMap;

/**
 * Class representing 'Nether Brick Item' item material in minecraft. <br>
 * ID of material: 405 <br>
 * String ID of material: minecraft:netherbrick <br>
 * Max item stack size: 64
 */
@SuppressWarnings("JavaDoc")
public class NetherBrickItemMat extends ItemMaterialData
{
    /**
     * Sub-ids used by diorite/minecraft by default
     */
    public static final int USED_DATA_VALUES = 1;

    public static final NetherBrickItemMat NETHER_BRICK_ITEM = new NetherBrickItemMat();

    private static final Map<String, NetherBrickItemMat>     byName = new CaseInsensitiveMap<>(USED_DATA_VALUES, SMALL_LOAD_FACTOR);
    private static final TShortObjectMap<NetherBrickItemMat> byID   = new TShortObjectHashMap<>(USED_DATA_VALUES, SMALL_LOAD_FACTOR, Short.MIN_VALUE);

    @SuppressWarnings("MagicNumber")
    protected NetherBrickItemMat()
    {
        super("NETHER_BRICK_ITEM", 405, "minecraft:netherbrick", "NETHER_BRICK_ITEM", (short) 0x00);
    }

    protected NetherBrickItemMat(final String enumName, final int id, final String minecraftId, final String typeName, final short type)
    {
        super(enumName, id, minecraftId, typeName, type);
    }

    protected NetherBrickItemMat(final String enumName, final int id, final String minecraftId, final int maxStack, final String typeName, final short type)
    {
        super(enumName, id, minecraftId, maxStack, typeName, type);
    }

    @Override
    public NetherBrickItemMat getType(final int type)
    {
        return getByID(type);
    }

    @Override
    public NetherBrickItemMat getType(final String type)
    {
        return getByEnumName(type);
    }

    /**
     * Returns one of NetherBrick sub-type based on sub-id, may return null
     *
     * @param id sub-type id
     *
     * @return sub-type of NetherBrick or null
     */
    public static NetherBrickItemMat getByID(final int id)
    {
        return byID.get((short) id);
    }

    /**
     * Returns one of NetherBrick sub-type based on name (selected by diorite team), may return null
     * If item contains only one type, sub-name of it will be this same as name of material.
     *
     * @param name name of sub-type
     *
     * @return sub-type of NetherBrick or null
     */
    public static NetherBrickItemMat getByEnumName(final String name)
    {
        return byName.get(name);
    }

    /**
     * Register new sub-type, may replace existing sub-types.
     * Should be used only if you know what are you doing, it will not create fully usable material.
     *
     * @param element sub-type to register
     */
    public static void register(final NetherBrickItemMat element)
    {
        byID.put(element.getType(), element);
        byName.put(element.getTypeName(), element);
    }

    @Override
    public NetherBrickItemMat[] types()
    {
        return NetherBrickItemMat.netherBrickTypes();
    }

    /**
     * @return array that contains all sub-types of this item.
     */
    public static NetherBrickItemMat[] netherBrickTypes()
    {
        return byID.values(new NetherBrickItemMat[byID.size()]);
    }

    static
    {
        NetherBrickItemMat.register(NETHER_BRICK_ITEM);
    }
}

