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

package org.diorite.impl.entity;

import org.diorite.impl.connection.CoreNetworkManager;
import org.diorite.impl.world.WorldImpl;
import org.diorite.EntityFactory;
import org.diorite.ILocation;
import org.diorite.auth.GameProfile;
import org.diorite.entity.EntityType;
import org.diorite.nbt.NbtTagCompound;
import org.diorite.world.WorldGroup;

public interface IEntityFactory extends EntityFactory
{
    IPlayer createPlayer(GameProfile profile, CoreNetworkManager networkManager, WorldGroup worldGroup);

    @Override
    IEntity createEntity(EntityType type, ILocation location);

    IEntity createEntity(NbtTagCompound nbt, WorldImpl world);

    int getEntityNetworkID(EntityType type);

    EntityType getEntityTypeByNetworkID(int id, boolean isObject);
}
