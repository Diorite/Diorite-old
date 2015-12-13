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

package org.diorite.impl.entity.meta.entry;

import org.diorite.impl.connection.packets.PacketDataSerializer;
import org.diorite.impl.entity.meta.EntityMetadataType;
import org.diorite.BlockLocation;

public class EntityMetadataBlockLocationEntry extends EntityMetadataObjectEntry<BlockLocation>
{
    public EntityMetadataBlockLocationEntry(final int index, final BlockLocation data)
    {
        super(index, data);
    }

    @Override
    public EntityMetadataType getDataType()
    {
        return EntityMetadataType.LOCATION;
    }

    @Override
    public void write(final PacketDataSerializer data)
    {
        data.writeInt(this.data.getX());
        data.writeInt(this.data.getY());
        data.writeInt(this.data.getZ());
    }
}