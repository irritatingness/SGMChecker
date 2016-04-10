package ups.email.sgmchecker;

import android.widget.Button;

import java.util.ArrayList;

public class Server {
    private String name;
    private String gameType;
    private String IP;
    private int port;
    private int buttonID;
    private Button button;
    private String playerCount;
    private String maxPlayerCount;
    private String map;
    private ArrayList<String> players;

    public Server(String name, String gameType, String IP, int port) {
        this.name = name;
        this.gameType = gameType;
        this.IP = IP;
        this.port = port;
        this.playerCount = "0";
        this.maxPlayerCount = "36";
    }

    public void setButton(Button button) {
        this.button = button;
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

    public void setMap(String map) {
        this.map = map;
    }

    public void setButtonID(int ID) {
        this.buttonID = ID;
    }

    public Button getButton() {
        return button;
    }

    public String getName() {
        return name;
    }

    public String getMap() {
        return map;
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

    public int getButtonID() {
        return buttonID;
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