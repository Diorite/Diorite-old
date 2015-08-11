package org.diorite.impl.connection.packets.login.server;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import org.diorite.impl.connection.EnumProtocol;
import org.diorite.impl.connection.EnumProtocolDirection;
import org.diorite.impl.connection.packets.Packet;
import org.diorite.impl.connection.packets.login.PacketLoginServerListener;

public abstract class PacketLoginServer extends Packet<PacketLoginServerListener>
{
    public PacketLoginServer()
    {
    }

    public PacketLoginServer(final byte[] data)
    {
        super(data);
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).appendSuper(super.toString()).append("TYPE", EnumProtocolDirection.CLIENTBOUND + "|" + EnumProtocol.LOGIN).toString();
    }
}
