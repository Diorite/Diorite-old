/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Diorite (by Bartłomiej Mazur (aka GotoFinal))
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

package org.diorite.material.items;

import java.util.Map;

import org.diorite.material.ItemMaterialData;
import org.diorite.material.PlaceableMat;
import org.diorite.utils.collections.maps.CaseInsensitiveMap;

import it.unimi.dsi.fastutil.shorts.Short2ObjectMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectOpenHashMap;

/**
 * Class representing 'Cauldron' item material in minecraft. <br>
 * ID of material: 380 <br>
 * String ID of material: minecraft:cauldron <br>
 * Max item stack size: 64
 */
@SuppressWarnings("JavaDoc")
public class CauldronMat extends ItemMaterialData implements PlaceableMat
{
    /**
     * Sub-ids used by diorite/minecraft by default
     */
    public static final int USED_DATA_VALUES = 1;

    public static final CauldronMat CAULDRON = new CauldronMat();

    private static final Map<String, CauldronMat>     byName = new CaseInsensitiveMap<>(USED_DATA_VALUES, SMALL_LOAD_FACTOR);
    private static final Short2ObjectMap<CauldronMat> byID   = new Short2ObjectOpenHashMap<>(USED_DATA_VALUES, SMALL_LOAD_FACTOR);

    @SuppressWarnings("MagicNumber")
    public CauldronMat()
    {
        super("CAULDRON", 380, "minecraft:cauldron", "CAULDRON", (short) 0x00);
    }

    public CauldronMat(final String enumName, final int id, final String minecraftId, final String typeName, final short type)
    {
        super(enumName, id, minecraftId, typeName, type);
    }

    public CauldronMat(final String enumName, final int id, final String minecraftId, final int maxStack, final String typeName, final short type)
    {
        super(enumName, id, minecraftId, maxStack, typeName, type);
    }

    @Override
    public CauldronMat getType(final int type)
    {
        return getByID(type);
    }

    @Override
    public CauldronMat getType(final String type)
    {
        return getByEnumName(type);
    }

    /**
     * Returns one of Cauldron sub-type based on sub-id, may return null
     *
     * @param id sub-type id
     *
     * @return sub-type of Cauldron or null
     */
    public static CauldronMat getByID(final int id)
    {
        return byID.get((short) id);
    }

    /**
     * Returns one of Cauldron sub-type based on name (selected by diorite team), may return null
     * If item contains only one type, sub-name of it will be this same as name of material.
     *
     * @param name name of sub-type
     *
     * @return sub-type of Cauldron or null
     */
    public static CauldronMat getByEnumName(final String name)
    {
        return byName.get(name);
    }

    /**
     * Register new sub-type, may replace existing sub-types.
     * Should be used only if you know what are you doing, it will not create fully usable material.
     *
     * @param element sub-type to register
     */
    public static void register(final CauldronMat element)
    {
        allItems.incrementAndGet();
        byID.put((short) element.getType(), element);
        byName.put(element.getTypeName(), element);
    }

    @Override
    public CauldronMat[] types()
    {
        return CauldronMat.cauldronTypes();
    }

    /**
     * @return array that contains all sub-types of this item.
     */
    public static CauldronMat[] cauldronTypes()
    {
        return byID.values().toArray(new CauldronMat[byID.size()]);
    }

    static
    {
        CauldronMat.register(CAULDRON);
    }
}
