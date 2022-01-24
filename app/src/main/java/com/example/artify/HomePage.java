package com.example.artify;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationBuilderWithBuilderAccessor;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ArrayList<Museo> musei = new ArrayList<Museo>();
    private FirebaseDatabase rootNode;
    private final String ROOT_PATH = "musei/";
    private RecyclerView listaMusei;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FirebaseAuth mAuth;
    private RelativeLayout rLLogout;
    private ArrayList<BluetoothDevice> devices = new ArrayList<>();
    private BroadcastReceiver broadcastReceiver;
    private BluetoothAdapter bluetoothAdapter;
    private NotificationManagerCompat notificationManager;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        listaMusei = findViewById(R.id.ListaMusei);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        rootNode = FirebaseDatabase.getInstance("https://artify-2ead0-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference rootPath = rootNode.getReference(ROOT_PATH);


        //Handle click in drawer menu
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        setNavigationViewListener();
        bluetoothManager();

        //Handle click on logout
        rLLogout = (RelativeLayout) findViewById(R.id.logout_layout);
        rLLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(HomePage.this, "Disconnesso!", Toast.LENGTH_LONG).show();
                Intent i = new Intent(HomePage.this, login.class);
                startActivity(i);
                finish();
            }
        });

        rootPath.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                initRv(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), R.string.ReadDbError,Toast.LENGTH_LONG).show();
            }
        });

        hideItem();
    }


    private void initRv(DataSnapshot snapshot){

        for (DataSnapshot sn : snapshot.getChildren()) {
            Museo museo = new Museo();

            String nomeMuseo = sn.getKey();
            museo.setNome(nomeMuseo);

            String urlImg = sn.getValue(Museo.class).getImg();
            museo.setImg(urlImg);

            HomePage.this.musei.add(museo);

        }

        lista_musei_adapter adapter = new lista_musei_adapter(musei, getApplicationContext());
        listaMusei.setAdapter(adapter);
        listaMusei.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent i;
        switch (item.getItemId()) {

            case R.id.login_menu: {
                i = new Intent(this, login.class);
                startActivity(i);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }

            case R.id.dashboard_menu: {
                i = new Intent(this, dashboard.class);
                startActivity(i);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        }
        return true;
    }


    private void setNavigationViewListener() {
        navigationView = (NavigationView) findViewById(R.id.side_menu);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void hideItem()
    {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.login_menu).setVisible(true);
        }else{
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.login_menu).setVisible(false);
            rLLogout.setVisibility(View.VISIBLE);
        }
    }

    private void bluetoothManager() {
        NotificationChannel channel = new NotificationChannel("ArtifyChannel", "Artify", NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth not supported", Toast.LENGTH_SHORT).show();
        } else {
            if (!bluetoothAdapter.isEnabled()) {
                Toast.makeText(this, "Enable Bluetooth", Toast.LENGTH_SHORT).show();
            }
            checkPermission();
                new Thread() {
                    @Override
                    public void run() {
                        while (true) {
                            bluetoothAdapter.startDiscovery();
                        }
                    }
                }.start();
                broadcastReceiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        String action = intent.getAction();
                        int i = 0;
                        if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                            if (!device.getName().isEmpty() && device.getName().contains("artify")) {
                                Log.d("device","device"+device.getName());
                                devices.add(device);
                                NotificationCompat.Builder builder = new NotificationCompat.Builder(HomePage.this, "ArtifyChannel")
                                        .setSmallIcon(R.drawable.ic_baseline_message_24)
                                        .setContentTitle("Opera trovata!!")
                                        .setContentText("Opera:" + device.getName())
                                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                                notificationManager = NotificationManagerCompat.from(HomePage.this);
                                notificationManager.notify(i, builder.build());
                                i++;
                            }
                        }
                    }
                };
                IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                registerReceiver(broadcastReceiver, intentFilter);
            }
        }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.BLUETOOTH_ADMIN}, 3);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

}