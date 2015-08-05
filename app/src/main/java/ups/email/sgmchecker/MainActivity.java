package ups.email.sgmchecker;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Iterator;
import java.util.Scanner;

import android.os.StrictMode;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.view.MenuInflater;
import android.widget.Toast;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import net.sourceforge.queried.PlayerInfo;
import net.sourceforge.queried.QueriEd;
import net.sourceforge.queried.ServerInfo;


public class MainActivity extends ActionBarActivity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    ArrayList<Server> servers = new ArrayList<Server>();
    private static Iterator iter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy thpolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(thpolicy);
        }

        try {
            URL url = new URL("https://raw.githubusercontent.com/irritatingness/SGMChecker/master/SERVERS");
            Scanner s = new Scanner(url.openStream());
            while (s.hasNextLine() == true) {
                String[] tokens = s.nextLine().split(",");
                Server tempServer = new Server(tokens[0], "HL2", tokens[1], 27015);
                servers.add(tempServer);
            }
            s.close();
        }
        catch(IOException ex) {
            // there was some connection problem, or the file did not exist on the server,
            // or your URL was not in the right format.
            ex.printStackTrace(); // for now, simply output it.
        }

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_refresh) {


            if (isNetworkAvailable() == true) {

                // get the listview
                expListView = (ExpandableListView) findViewById(R.id.lvExp);

                // preparing list data
                prepareListData();

                listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

                // setting list adapter
                expListView.setAdapter(listAdapter);

                Toast toast = Toast.makeText(getApplicationContext(), "Data refreshed", Toast.LENGTH_SHORT);
                toast.show();
            }

            else {
                Toast toast = Toast.makeText(getApplicationContext(), "No network connection detected", Toast.LENGTH_SHORT);
                toast.show();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void getData() {
        if (isNetworkAvailable() == true) {
            check(servers);
        }
        else {
            Toast toast = Toast.makeText(getApplicationContext(), "No network connection detected", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public static void check(ArrayList<Server> list) {
        for (Server temp : list) {
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
        }
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        getData();

        // Adding header & child data
        int x = 0;
        for(Server temp : servers) {
            String spacer = "";
            int spaces = 30;
            spaces = spaces - temp.getName().length();
            for (int i = 0; i < spaces; i++) {
                spacer = spacer + " ";
            }
            listDataHeader.add(temp.getName() + spacer + temp.getPlayerCount() + "/" + temp.getMaxPlayerCount() + " players");
            listDataChild.put(listDataHeader.get(x), temp.getPlayers());
            x++;
        }
    }
}