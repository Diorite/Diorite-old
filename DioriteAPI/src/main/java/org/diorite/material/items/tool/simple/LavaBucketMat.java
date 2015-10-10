package org.diorite.material.items.tool.simple;

import java.util.Map;

import org.diorite.material.FuelMat;
import org.diorite.material.ItemMaterialData;
import org.diorite.utils.collections.maps.CaseInsensitiveMap;

import gnu.trove.map.TShortObjectMap;
import gnu.trove.map.hash.TShortObjectHashMap;

/**
 * Class representing 'Lava Bucket' item material in minecraft. <br>
 * ID of material: 327 <br>
 * String ID of material: minecraft:lava_bucket <br>
 * Max item stack size: 1
 */
@SuppressWarnings("JavaDoc")
public class LavaBucketMat extends ItemMaterialData implements FuelMat
{
    /**
     * Sub-ids used by diorite/minecraft by default
     */
    public static final int USED_DATA_VALUES = 1;

    public static final LavaBucketMat LAVA_BUCKET = new LavaBucketMat();

    private static final Map<String, LavaBucketMat>     byName = new CaseInsensitiveMap<>(USED_DATA_VALUES, SMALL_LOAD_FACTOR);
    private static final TShortObjectMap<LavaBucketMat> byID   = new TShortObjectHashMap<>(USED_DATA_VALUES, SMALL_LOAD_FACTOR, Short.MIN_VALUE);

    @SuppressWarnings("MagicNumber")
    protected LavaBucketMat()
    {
        super("LAVA_BUCKET", 327, "minecraft:lava_bucket", 1, "LAVA_BUCKET", (short) 0x00);
    }

    protected LavaBucketMat(final String enumName, final int id, final String minecraftId, final String typeName, final short type)
    {
        super(enumName, id, minecraftId, LAVA_BUCKET.getMaxStack(), typeName, type);
    }

    protected LavaBucketMat(final String enumName, final int id, final String minecraftId, final int maxStack, final String typeName, final short type)
    {
        super(enumName, id, minecraftId, maxStack, typeName, type);
    }

    @Override
    public LavaBucketMat getType(final int type)
    {
        return getByID(type);
    }

    @Override
    public LavaBucketMat getType(final String type)
    {
        return getByEnumName(type);
    }

    @SuppressWarnings("MagicNumber")
    @Override
    public int getFuelPower()
    {
        return 100000;
    }

    /**
     * Returns one of LavaBucket sub-type based on sub-id, may return null
     *
     * @param id sub-type id
     *
     * @return sub-type of LavaBucket or null
     */
    public static LavaBucketMat getByID(final int id)
    {
        return byID.get((short) id);
    }

    /**
     * Returns one of LavaBucket sub-type based on name (selected by diorite team), may return null
     * If item contains only one type, sub-name of it will be this same as name of material.
     *
     * @param name name of sub-type
     *
     * @return sub-type of LavaBucket or null
     */
    public static LavaBucketMat getByEnumName(final String name)
    {
        return byName.get(name);
    }

    /**
     * Register new sub-type, may replace existing sub-types.
     * Should be used only if you know what are you doing, it will not create fully usable material.
     *
     * @param element sub-type to register
     */
    public static void register(final LavaBucketMat element)
    {
        byID.put(element.getType(), element);
        byName.put(element.getTypeName(), element);
    }

    @Override
    public LavaBucketMat[] types()
    {
        return LavaBucketMat.lavaBucketTypes();
    }

    /**
     * @return array that contains all sub-types of this item.
     */
    public static LavaBucketMat[] lavaBucketTypes()
    {
        return byID.values(new LavaBucketMat[byID.size()]);
    }

    static
    {
        LavaBucketMat.register(LAVA_BUCKET);
    }
}

