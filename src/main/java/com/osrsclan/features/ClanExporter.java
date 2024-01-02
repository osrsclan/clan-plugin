package com.osrsclan.features;

import com.google.gson.Gson;
import com.osrsclan.models.OsrsClanMember;
import com.osrsclan.models.OsrsClanRank;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.clan.ClanMember;
import net.runelite.api.clan.ClanRank;
import net.runelite.api.clan.ClanSettings;
import net.runelite.api.events.ClanChannelChanged;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.menus.MenuManager;
import net.runelite.client.menus.WidgetMenuOption;
import net.runelite.client.util.Text;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
public class ClanExporter {
    @Inject
    private Client client;

    @Inject
    private MenuManager menuManager;

    private static final WidgetMenuOption CLAN_CHAT;

    private static final WidgetMenuOption SYNC_RANKS;

    static {
        CLAN_CHAT = new WidgetMenuOption("Export Members", "OSRS Members", 46333956);
        SYNC_RANKS = new WidgetMenuOption("Export Ranks", "OSRSClan Ranks", 46333956);
    }

    public void registerExporterMenu()
    {
        this.menuManager.addManagedCustomMenu(CLAN_CHAT, null);
        this.menuManager.addManagedCustomMenu(SYNC_RANKS, null);
    }

    @Subscribe
    private void onClanChannelChanged(ClanChannelChanged event)
    {
        if (event.isGuest())
        {
            return;
        }

        ClanSettings clanSettings = client.getClanSettings();

        if (clanSettings == null)
        {
            return;
        }

        var playerMember = clanSettings.findMember(client.getLocalPlayer().getName());

        if (playerMember == null)
        {
            return;
        }

        var rank = clanSettings.titleForRank(playerMember.getRank());

        // TODO: Make the rank name from an option on the osrsclan.pro portal, this would be an array of ordinals for the clan rank enum. For example, Owner / Deputy Owner = 16, Administrator = 11.
        if (rank == null || ! Objects.equals(rank.getName(), "Deputy Owner"))
        {
            return;
        }

        this.registerExporterMenu();
    }

    public List<OsrsClanRank> getRanks()
    {
        ArrayList<OsrsClanRank> rankList = new ArrayList<>();

        ClanSettings clanSettings = client.getClanSettings();

        assert clanSettings != null;

        for (ClanMember clanMember : clanSettings.getMembers())
        {
            var osrsClanRank = new OsrsClanRank();
            osrsClanRank.setName(clanSettings.titleForRank(clanMember.getRank()).getName());
            osrsClanRank.setId(clanMember.getRank().ordinal());

            if (! hasFetchedRank(rankList, osrsClanRank.getName()))
            {
                rankList.add(osrsClanRank);
            }
        }

        return rankList;
    }

    public List<OsrsClanMember> getMembers()
    {
        ArrayList<OsrsClanMember> memberList = new ArrayList<>();

        ClanSettings clanSettings = client.getClanSettings();

        assert clanSettings != null;

        for (ClanMember clanMember : clanSettings.getMembers())
        {
            var osrsClanMember = new OsrsClanMember();
            osrsClanMember.setName(Text.toJagexName(clanMember.getName()));
            osrsClanMember.setRank(clanMember.getRank().ordinal());

            memberList.add(osrsClanMember);
        }

        return memberList;
    }

    @Subscribe
    public void onMenuOptionClicked(MenuOptionClicked event)
    {
        if (Text.removeTags(event.getMenuTarget()).equals("OSRS Members"))
        {
            getMembers();
        }

        if (Text.removeTags(event.getMenuTarget()).equals("OSRSClan Ranks"))
        {
            getRanks();
        }
    }

    private static boolean hasFetchedRank(List<OsrsClanRank> rankList, String targetRank) {
        for (OsrsClanRank rank : rankList) {
            if (rank.getName().equals(targetRank)) {
                return true;
            }
        }
        return false;
    }
}
