package org.diorite.command.sender;

public interface BlockCommandSender extends CommandSender
{
    @Override
    default boolean isConsole()
    {
        return false;
    }

    @Override
    default boolean isPlayer()
    {
        return false;
    }

    @Override
    default boolean isCommandBlock()
    {
        return true;
    }

    // getBlock();
}
