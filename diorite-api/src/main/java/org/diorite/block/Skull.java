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

package org.diorite.block;

import org.diorite.material.SkullType;

public interface Skull extends BlockState
{
    /**
     * Checks if the skull has an owner.
     *
     * @return true if the skull has an owner.
     */
    boolean hasOwner();

    /**
     * Returns the owner of the skull.
     *
     * @return the owner of the skull, or null if none exist.
     */
    String getOwner();

    /**
     * Sets the owner of the skull.
     *
     * @param owner is the new owner of the skull.
     */
    void setOwner(String owner);

    /**
     * Gets the rotation of the skull.
     *
     * @return the rotation of the skull.
     */
    BlockFace getRotation();

    /**
     * Sets the rotation of the skull.
     *
     * @param rotation is the new rotation of the skull.
     */
    void setRotation(BlockFace rotation);

    /**
     * Gets the type of the skull.
     *
     * @return the type of the skull.
     */
    SkullType getSkullType();

    /**
     * Sets the type of the skull.
     *
     * @param type is the new type of the skull.
     */
    void setSkullType(SkullType type);
}
