package ups.email.sgmchecker;

import android.os.AsyncTask;

import net.sourceforge.queried.PlayerInfo;
import net.sourceforge.queried.QueriEd;
import net.sourceforge.queried.ServerInfo;

import java.util.ArrayList;
import java.util.Iterator;

public class GetServerInfoTask extends AsyncTask<ArrayList<Server>, Server, ArrayList> {

    ArrayList<Server> servers = new ArrayList<Server>();
    private static Iterator iter;

    @Override
    protected ArrayList<Server> doInBackground(ArrayList<Server>... params) {

        for (Server temp : params[0]) {
            ServerInfo serverInfo = QueriEd.serverQuery(temp.getGameType(), temp.getIP(), temp.getPort());
            if (serverInfo == null) {
                temp.setPlayerCount("0");
                temp.setMaxPlayerCount("0");
            }
            else {
                temp.setPlayerCount(serverInfo.getPlayerCount());
                temp.setMaxPlayerCount(serverInfo.getMaxPlayers());
            }
            ArrayList playerInfo = (ArrayList) QueriEd.playerQuery(temp.getGameType(), temp.getIP(), temp.getPort());
            ArrayList<String> tempName = new ArrayList<String>();
            if(playerInfo != null && playerInfo.size() > 0) {
                iter = playerInfo.iterator();
                while(iter.hasNext()) {
                    PlayerInfo pInfo = (PlayerInfo) iter.next();
                    String name = pInfo.getName();
                    if (name != "") {
                        tempName.add(name);
                    }
                }
                temp.setPlayers(tempName);
            }
            else {
                temp.setPlayers(tempName);
            }
            servers.add(temp);
            publishProgress(temp);
        }
        return servers;
    }

    @Override
    protected void onProgressUpdate(Server... server) {
        //Hacky way of updating a button, simulates a click when each server has been pinged
        server[0].getButton().performLongClick();
    }
}