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

import org.diorite.inventory.block.BrewingStandInventory;
import org.diorite.inventory.item.ItemStack;

/**
 * Represents a brewing stand block.
 */
public interface BrewingStand extends BlockState, BlockContainer
{
    /**
     * Gets the left input of the brewing stand.
     *
     * @return the left input of the brewing stand.
     */
    ItemStack getLeftInput();

    /**
     * Sets the left input of the brewing stand.
     * @param input is a new input.
     */
    void setLeftInput(ItemStack input);

    /**
     * Gets the middle input of the brewing stand.
     *
     * @return the middle input of the brewing stand.
     */
    ItemStack getMiddleInput();

    /**
     * Sets the middle input of the brewing stand.
     *
     * @param input is a new input.
     */
    void setMiddleInput(ItemStack input);

    /**
     * Gets the right input of the brewing stand.
     *
     * @return the right input of the brewing stand.
     */
    ItemStack getRightInput();

    /**
     * Sets the right input of the brewing stand.
     *
     * @param input is a new input.
     */
    void setRightInput(ItemStack input);

    /**
     * Gets the ingredient of the brewing stand.
     *
     * @return the ingredient of the brewing stand.
     */
    ItemStack getIngredient();

    /**
     * Sets the ingredient of the brewing stand.
     *
     * @param ingredient is a new ingredient.
     */
    void setIngredient(ItemStack ingredient);

    /**
     * Gets the fuel of the brewing stand.
     *
     * @return the fuel of the brewing stand.
     */
    ItemStack getFuel();

    /**
     * Sets the fuel of the brewing stand.
     *
     * @param fuel is a new fuel.
     */
    void setFuel(ItemStack fuel);
}
