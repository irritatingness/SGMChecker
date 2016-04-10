package ups.email.sgmchecker;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import android.app.ActionBar;
import android.content.Intent;
import android.os.StrictMode;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.view.MenuInflater;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class MainActivity extends ActionBarActivity {

    public static ArrayList<Server> servers = new ArrayList<>();
    public static LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy thpolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(thpolicy);
        }

        //Only do this if it's the first time we're creating the view, based on servers list being empty
        if (servers.isEmpty()) {
            //Load server-list from GitHub page
            try {
                URL url = new URL("https://raw.githubusercontent.com/irritatingness/SGMChecker/master/SERVERS");
                Scanner s = new Scanner(url.openStream());
                while (s.hasNextLine()) {
                    String[] tokens = s.nextLine().split(",");
                    Server tempServer = new Server(tokens[0], "HL2", tokens[1], 27015);
                    servers.add(tempServer);
                }
                s.close();

            }
            catch(IOException ex) {
                Toast.makeText(MainActivity.this, "Could not load server list.", Toast.LENGTH_LONG).show();
            }
        }

        //Create the view
        int count = 0;
        layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        for (Server temp : servers) {

            LinearLayout row = new LinearLayout(this);
            row.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            Button button = new Button(this);
            button.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            button.setText(temp.getName() + "    " + temp.getPlayerCount() + "/" + temp.getMaxPlayerCount() + " players");
            button.setId(count);
            button.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            //Click listener! This is where we'll add new activities on press that will list all players on a server.
            button.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    int id  = v.getId();
                    for (Server temp2 : servers) {
                        if (temp2.getButtonID() == id) {
                            //Gonna wanna make a new activity to show the players on the server.
                            //Call method to do so, pass it server's player list
                            displayPlayers(temp2.getPlayers(), temp2.getName(), temp2.getMap());
                        }
                    }
                }
            });

            button.setOnLongClickListener(new Button.OnLongClickListener() {
                public boolean onLongClick(View v) {
                    int id  = v.getId();
                    for (Server temp2 : servers) {
                        if (temp2.getButtonID() == id) {
                            //Update button by viewID!
                            Button pressed = (Button)findViewById(temp2.getButtonID());
                            pressed.setText(temp2.getName() + "    " + temp2.getPlayerCount() + "/" + temp2.getMaxPlayerCount() + " players");
                        }
                    }
                    return false;
                }
            });

            temp.setButton(button);

            row.addView(button);

            temp.setButtonID(button.getId());

            count++;
            layout.addView(row);
        }
        setContentView(layout);
        new GetServerInfoTask().execute(servers);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Get item selected
        int id = item.getItemId();

        //If the item selected is the refresh button
        if (id == R.id.action_refresh) {

            //Check network connection
            if (isNetworkAvailable()) {
                new GetServerInfoTask().execute(servers);
                Toast toast = Toast.makeText(getApplicationContext(), "Refreshing data", Toast.LENGTH_SHORT);
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

    /**
     * Check network connection
     * @return true if network is connected
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void displayPlayers(ArrayList<String> players, String name, String map) {
        Intent intent = new Intent(this, DisplayPlayersActivity.class);
        intent.putExtra("Players", players);
        intent.putExtra("Name", name);
        intent.putExtra("Map", map);
        startActivity(intent);
    }
}