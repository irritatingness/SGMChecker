package net.sourceforge.queried.gametypes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.queried.PlayerInfo;
import net.sourceforge.queried.ScoreComparator;
import net.sourceforge.queried.ServerInfo;
import net.sourceforge.queried.Util;

public class BFVServerInfo {

  public static ServerInfo getDetails(int localPort, String ipStr, int port, int infoType, int queryType,
          int gameType) {

    String queryResult = Util.getInfo(localPort, ipStr, port, infoType, queryType, gameType);
    ServerInfo serverInfo = null;
    if ((queryResult != null) && (queryResult.length() > 0)) {
      serverInfo = new ServerInfo();
      serverInfo.setGame(Util.getPartGS2(queryResult, "game_id"));
      serverInfo.setGameVersion(Util.getPartGS2(queryResult, "gamever"));
      serverInfo.setIp(ipStr);
      serverInfo.setPort(Util.getPartGS2(queryResult, "hostport"));
      serverInfo.setName(Util.getPartGS2(queryResult, "hostname"));
      serverInfo.setMap(Util.getPartGS2(queryResult, "mapname"));
      serverInfo.setPlayerCount(Util.getPartGS2(queryResult, "numplayers"));
      serverInfo.setMaxPlayers(Util.getPartGS2(queryResult, "maxplayers"));
      serverInfo.setFullResponse(queryResult);
    }

    return serverInfo;
  }

  public static List<PlayerInfo> getPlayers(int localPort, String ipStr, int port, int infoType, int queryType,
          int gameType) {

    String recStr = Util.getInfo(localPort, ipStr, port, infoType, queryType, gameType);
    // kills_.. (until next) ..
    char chr = 00;
    char chr2 = 02;
    int start = recStr.indexOf("kills_" + chr + chr) + 8;
    int end = recStr.indexOf(chr + chr + chr2);

    if (start >= end) {
      return Collections.EMPTY_LIST;
    }
    String stripped = recStr.substring(start, end);

    ArrayList<PlayerInfo> playerInfo = new ArrayList<PlayerInfo>();
    String[] pieces = stripped.split(chr + "");
    for (int i = 0; i < pieces.length; i++) {
//            System.out.println("piece["+ i +"]: "+ pieces[i]);
      PlayerInfo player = new PlayerInfo();
      // name
      player.setName(pieces[i++]);
      // score
      player.setScore(Integer.valueOf(pieces[i++]).intValue());
      // deaths
      player.setDeaths(Integer.valueOf(pieces[i++]).intValue());
      // team
      i++;
      // ping
      i++;
      // kills
      player.setKills(Integer.valueOf(pieces[i]).intValue());
      playerInfo.add(player);
    }
    Collections.sort(playerInfo, new ScoreComparator());

    return playerInfo;
  }
}
