package com.stealthcotper.networktools;


import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import android.widget.ScrollView;
import android.widget.TextView;

import com.stealthcopter.networktools.ARPInfo;
import com.stealthcopter.networktools.IPTools;
import com.stealthcopter.networktools.Ping;
import com.stealthcopter.networktools.PortScan;
import com.stealthcopter.networktools.SubnetDevices;
import com.stealthcopter.networktools.WakeOnLan;
import com.stealthcopter.networktools.ping.PingResult;
import com.stealthcopter.networktools.ping.PingStats;
import com.stealthcopter.networktools.subnet.Device;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    
   




 

    @Override

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    
        editIpAddress = findViewById(R.id.editIpAddress);
        scrollView = findViewById(R.id.scrollView1);
        pingButton = findViewById(R.id.pingButton);
        wolButton = findViewById(R.id.wolButton);
        portScanButton = findViewById(R.id.portScanButton);
        subnetDevicesButton = findViewById(R.id.subnetDevicesButton);

        InetAddress ipAddress = IPTools.getLocalIPv4Address();
      
          Address());
        }

        findViewById(R.id.pingButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            doPing();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        findViewById(R.id.wolButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            doWakeOnLan();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

      ClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            doPortScan();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        findViewById(R.id.subnetDevicesButton).setOnClickListener(new View.OnClickListener() {
            @Override
        
            
                    @Override
                    public void run() {
                        try {
                            findSubnetDevices();
                  
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

    }

) {
    
            @Override
            public void run() {
            
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                  
                    }
                });
            }
        });
    }


     
      
       
              
                  
                }
            }
        });
    }

   l
  

    
           s");
            return;
        }

        setEnabled(pingButton, false);

       
      
        try {
            
      
            e.printStackTrace();
            
            setEnabled(pingButton, true);
            return;
        }


   
       
       , pingResult.getTimeTaken()));


        // Perform an asynchronous ping
        Ping.onAddress(ipAddress).setTimeOutMillis(1000).setTimes(5).doPing(new Ping.PingListener() {
            @Override
        ult) {
                if (pingResult.isReachable) {
                   "%.2f ms", pingResult.getTimeTaken()));
                } else {
                  .timeout));
                }
            }

            @Override
       ) {
       s: %d, Packets lost: %d",
                        pingStats.getNoPings(), pingStats.getPacketsLost()));
            ?/Avg/Max Time: %.2f/%.2f/%.2f ms",
                        pingStats.getMinTimeTaken(), pingStats.getAverageTimeTaken(), pingStats.getMaxTimeTaken()));
                setEnabled(pingButton, true);
            }

            @Override
            public void onError(Exception e) {
                // TODO: STUB METHOD
                setEnabled(pingButton, true);
            }
        });

    }

  
xt().toString();

    )) {
            appendResultsText("Invalid Ip Address");

        }

        setEnabled(wolButton, false);

        );

        // Get mac address from IP (using arp cache)
      FromIPAddress(ipAddress);

        if (macAddress == {
            appendResultsText("Could not fromIPAddress MAC address, cannot send WOL packet without it.");
            setEnabled(wolButton, true);
            return;
        }

     
        appendResultsText("IP address2: " + ARPInfo.getIPAddressFromMAC(macAddress));

        // Send Wake on lan packed to ip/mac
        try {
            WakeOnLan.sendWakeOnLan(ipAddress, macAddress);
        ;
    (IOException e) {

            e.printStackTrace();
        } finally {
            setEnabled(wolButton, true);
        }
    }

 


   
       
          

        }

        setEnabled(portScanButton, false);

      
        appendResultsText("PortScanning IP: " + ipAddress);
        ArrayList<Integer> openPorts = PortScan.onAddress(ipAddress).setPort(21).setMethodTCP().doScan();

        final long startTimeMillis = System.currentTimeMillis();

        // Perform an asynchronous port scan
        PortScan portScan = PortScan.onAddress(ipAddress).setPortsAll().setMethodTCP().doScan(new PortScan.PortListener() {
         
       

            }

            @Override
       oid onFinished(ArrayList<Integer> openPorts) {
                appendResultsText("Open Ports: " + openPorts.size());
                appendResultsText("Time Taken: " + ((System.currentTimeMillis() - startTimeMillis)/1000.0f));
                setEnabled(portScanButton, true);
            }
        });

        // Below is example of how to cancel a running scan
        // portScan.cancel();
    }


    private void findSubnetDevices() {

        setEnabled(subnetDevicesButton, false);

        final long startTimeMillis = System.currentTimeMillis();

        SubnetDevices subnetDevices = SubnetDevices.fromLocalAddress().findDevices(new SubnetDevices.OnSubnetDeviceFound() {
            @Override
            public void onDeviceFound(Device device) {
                appendResultsText("Device: " + device.ip+" "+ device.hostname);
            }

            @Override
            public void onFinished(ArrayList<Device> devicesFound) {
                float timeTaken =  (System.currentTimeMillis() - startTimeMillis)/1000.0f;
                appendResultsText("Devices Found: " + devicesFound.size());
                appendResultsText("Finished "+timeTaken+" s");
                setEnabled(subnetDevicesButton, true);
            }
        });

        // Below is example of how to cancel a running scan
        // subnetDevices.cancel();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_github) {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(getString(R.string.github_url)));
            startActivity(i);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

}
