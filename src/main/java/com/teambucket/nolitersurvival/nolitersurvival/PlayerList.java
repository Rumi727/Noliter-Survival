package com.teambucket.nolitersurvival.nolitersurvival;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class PlayerList
{
    public static void Update()
    {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.PLAYER_INFO);
        ArrayList<PlayerInfoData> list = new ArrayList<PlayerInfoData>();

        OfflinePlayer[] offlinePlayers = Bukkit.getOfflinePlayers();
        for (int i = 0; i < offlinePlayers.length; i++)
        {
            OfflinePlayer offlinePlayer = offlinePlayers[i];

            WrappedGameProfile profile;
            if (offlinePlayer instanceof Player)
                profile = WrappedGameProfile.fromPlayer((Player)offlinePlayer);
            else
                profile = WrappedGameProfile.fromOfflinePlayer(offlinePlayer).withName(offlinePlayer.getName());

            list.add(new PlayerInfoData(profile, 0, EnumWrappers.NativeGameMode.NOT_SET, WrappedChatComponent.fromText(offlinePlayer.getName())));
        }

        //packet.getPlayerInfoAction().write(0, EnumWrappers.PlayerInfoAction.ADD_PLAYER);
        packet.getPlayerInfoDataLists().write(0, list);
        ProtocolManager pm = ProtocolLibrary.getProtocolManager();
        Iterator var11 = Bukkit.getOnlinePlayers().iterator();

        while(var11.hasNext())
        {
            Player player = (Player)var11.next();

            try
            {
                pm.sendServerPacket(player, packet);
            }
            catch (InvocationTargetException e)
            {
                e.printStackTrace();
            }
        }
    }
}
