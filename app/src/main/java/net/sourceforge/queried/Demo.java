package net.sourceforge.queried;

import java.util.Iterator;
import java.util.List;

public class Demo {

  public static void main(String[] args) {
    // check("Q4", "213.208.119.119", 28004);
    // check("BF2", "195.12.56.171", 29900);
    check("BF2142", "213.115.191.17", 29900);
  }

  private static void check(String gameType, String ip, int port) {
    System.out.println("ServerInfo [" + gameType + "]:");

    ServerInfo serverInfo = QueriEd.serverQuery(gameType, ip, port);

    if (serverInfo == null) {
      System.out.println("ServerInfo == null");
    } else {
      System.out.println(
              serverInfo.getName() +
                      "\nIP: " + serverInfo.getIp() + ":" + serverInfo.getPort() +
                      "\nGame: " + serverInfo.getGame() +
                      "\nMap: " + serverInfo.getMap() +
                      "\nPlayers: " + serverInfo.getPlayerCount() + "/" + serverInfo.getMaxPlayers() +
                      "\nVersion: " + serverInfo.getGameVersion());
      System.out.println(
              "Tickets: Team1: " + serverInfo.getTeam1Tickets()
                      + " :: Team2: " + serverInfo.getTeam2Tickets());
    }

    System.out.println("PlayerInfo:");

    List<PlayerInfo> playerInfo = QueriEd.playerQuery(gameType, ip, port);

    if (playerInfo != null && playerInfo.size() > 0) {
      Iterator<?> iter = playerInfo.iterator();
      int count = 1;

      while (iter.hasNext()) {
        PlayerInfo pInfo = (PlayerInfo) iter.next();

        System.out.println(
                count + ") " + pInfo.getName() + " [" + pInfo.getScore()
                        + "/" + pInfo.getKills() + "/" + pInfo.getDeaths() + "]"
                        + "ping: " + pInfo.getPing() + " rate: "
                        + pInfo.getRate());
        System.out.println("Clan: " + pInfo.getClan());
        count++;
      }
    } else {
      System.out.println("No players");
    }
    System.out.println("");
    System.out.println("");

    Iterator<?> games = QueriEd.getSupportedGames().keySet().iterator();

    System.out.print("Supported games");
    while (games.hasNext()) {
      String gameKey = (String) games.next();

      System.out.print(" : " + gameKey);
    }
    System.out.println();
  }

}
