/**
 * Created by Chris on 7/15/2015.
 */
package ups.email.sgmchecker;

import java.util.ArrayList;

public class Server {
    String name;
    String gameType;
    String IP;
    int port;
    String playerCount;
    String maxPlayerCount;
    ArrayList<String> players;

    public Server(String name, String gameType, String IP, int port) {
        this.name = name;
        this.gameType = gameType;
        this.IP = IP;
        this.port = port;
    }

    public void setPlayerCount(String playerCount) {
        this.playerCount = playerCount;
    }

    public void setMaxPlayerCount(String maxPlayerCount) {
        this.maxPlayerCount = maxPlayerCount;
    }

    public void setPlayers(ArrayList<String> players) {
        this.players = players;
    }

    public String getName() {
        return name;
    }

    public String getGameType() {
        return gameType;
    }

    public String getIP() {
        return IP;
    }

    public int getPort() {
        return port;
    }

    public String getPlayerCount() {
        return playerCount;
    }

    public String getMaxPlayerCount() {
        return maxPlayerCount;
    }

    public ArrayList<String> getPlayers() {
        return players;
    }
}