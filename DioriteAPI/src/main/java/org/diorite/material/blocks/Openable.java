package org.diorite.material.blocks;

import org.diorite.material.BlockMaterialData;

/**
 * Implementing block should have open and closed state and allow chaning it.
 */
public interface Openable
{
    /**
     * @return if it's open or closed.
     */
    boolean isOpen();

    /**
     * Get other sub-type based on open state.
     *
     * @param open if it should be an open stete
     *
     * @return sub-type of block
     */
    BlockMaterialData getOpen(boolean open);
}
