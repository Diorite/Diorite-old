package org.diorite.material.blocks.nether;

import java.util.Map;

import org.diorite.material.BlockMaterialData;
import org.diorite.utils.collections.maps.CaseInsensitiveMap;

import gnu.trove.map.TByteObjectMap;
import gnu.trove.map.hash.TByteObjectHashMap;

/**
 * Class representing 'Netherrack' block material in minecraft. <br>
 * ID of block: 87 <br>
 * String ID of block: minecraft:netherrack <br>
 * Hardness: 0,4 <br>
 * Blast Resistance 2
 */
@SuppressWarnings("JavaDoc")
public class NetherrackMat extends BlockMaterialData
{
    /**
     * Sub-ids used by diorite/minecraft by default
     */
    public static final int USED_DATA_VALUES = 1;

    public static final NetherrackMat NETHERRACK = new NetherrackMat();

    private static final Map<String, NetherrackMat>    byName = new CaseInsensitiveMap<>(USED_DATA_VALUES, SMALL_LOAD_FACTOR);
    private static final TByteObjectMap<NetherrackMat> byID   = new TByteObjectHashMap<>(USED_DATA_VALUES, SMALL_LOAD_FACTOR, Byte.MIN_VALUE);

    @SuppressWarnings("MagicNumber")
    protected NetherrackMat()
    {
        super("NETHERRACK", 87, "minecraft:netherrack", "NETHERRACK", (byte) 0x00, 0.4f, 2);
    }

    protected NetherrackMat(final String enumName, final int id, final String minecraftId, final int maxStack, final String typeName, final byte type, final float hardness, final float blastResistance)
    {
        super(enumName, id, minecraftId, maxStack, typeName, type, hardness, blastResistance);
    }

    @Override
    public NetherrackMat getType(final String name)
    {
        return getByEnumName(name);
    }

    @Override
    public NetherrackMat getType(final int id)
    {
        return getByID(id);
    }

    /**
     * Returns one of Netherrack sub-type based on sub-id, may return null
     *
     * @param id sub-type id
     *
     * @return sub-type of Netherrack or null
     */
    public static NetherrackMat getByID(final int id)
    {
        return byID.get((byte) id);
    }

    /**
     * Returns one of Netherrack sub-type based on name (selected by diorite team), may return null
     * If block contains only one type, sub-name of it will be this same as name of material.
     *
     * @param name name of sub-type
     *
     * @return sub-type of Netherrack or null
     */
    public static NetherrackMat getByEnumName(final String name)
    {
        return byName.get(name);
    }

    /**
     * Register new sub-type, may replace existing sub-types.
     * Should be used only if you know what are you doing, it will not create fully usable material.
     *
     * @param element sub-type to register
     */
    public static void register(final NetherrackMat element)
    {
        byID.put((byte) element.getType(), element);
        byName.put(element.getTypeName(), element);
    }

    @Override
    public NetherrackMat[] types()
    {
        return NetherrackMat.netherrackTypes();
    }

    /**
     * @return array that contains all sub-types of this block.
     */
    public static NetherrackMat[] netherrackTypes()
    {
        return byID.values(new NetherrackMat[byID.size()]);
    }

    static
    {
        NetherrackMat.register(NETHERRACK);
    }
}
