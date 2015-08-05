package net.sourceforge.queried.gametypes;

import java.util.ArrayList;
import java.util.Collections;

import net.sourceforge.queried.PlayerInfo;
import net.sourceforge.queried.ScoreComparator;
import net.sourceforge.queried.ServerInfo;
import net.sourceforge.queried.Util;

public class D3ServerInfo {

  public static ServerInfo getDetails(int localPort, String ipStr, int port, int infoType, int queryType,
          int gameType) {

    String queryResult = Util.getInfo(localPort, ipStr, port, "getInfo", infoType, queryType, gameType);
    if (queryResult == null || queryResult.length() < 1) {
      return null;
    }
    // queryResult = queryResult.substring(queryResult.indexOf("\\"));
    queryResult = queryResult.replaceAll("\\^([0-9a-wyzA-WYZ]|x[0-9a-fA-F]{6})", "");
    ServerInfo serverInfo = null;
    if (queryResult != null && queryResult.length() > 0) {
      serverInfo = new ServerInfo();
      String[] plyrs = queryResult.split("�>");

      serverInfo.setGame(Util.getPartGS2(queryResult, "si_gameType"));
      serverInfo.setIp(ipStr);
      serverInfo.setPort(port + "");
      serverInfo.setName(Util.getPartGS2(queryResult, "si_name"));
      serverInfo.setPlayerCount(plyrs.length - 1 + "");
      serverInfo.setMaxPlayers(Util.getPartGS2(queryResult, "si_maxPlayers"));
      serverInfo.setMap(Util.getPartGS2(queryResult, "si_map"));
      serverInfo.setFullResponse(queryResult);
    }

    return serverInfo;
  }

  public static ArrayList<PlayerInfo> getPlayers(int localPort, String ipStr, int port, int infoType, int queryType,
          int gameType) {

    ArrayList<PlayerInfo> playerInfo = new ArrayList<PlayerInfo>();
    String queryResult = Util.getInfo(localPort, ipStr, port, "getstatus", infoType, queryType, gameType);
    if (queryResult == null || queryResult.length() < 1) {
      return playerInfo;
    }

    if (queryResult.length() > 0) {
      queryResult = queryResult.replaceAll("\\^([0-9a-wyzA-WYZ]|x[0-9a-fA-F]{6})", "");
      // this is a little cheat to get player pieces equal (TODO: get the parsing correct)
      queryResult = queryResult.substring(0, queryResult.length() - 2);
      String[] plyrs = queryResult.split("�>");
      for (int x = 1; x < plyrs.length; x++) {
        String plyr = plyrs[x];
        String name = plyr.substring(2, plyr.length() - 4);

        PlayerInfo player = new PlayerInfo();
        player.setName(name);
        playerInfo.add(player);
      }
      Collections.sort(playerInfo, new ScoreComparator());
    }

    return playerInfo;

  }

}
