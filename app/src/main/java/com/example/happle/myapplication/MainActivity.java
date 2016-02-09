package com.example.happle.myapplication;

import static objects.Constant.*;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManagerFactory;
import static objects.Function.*;
import it.unisa.dia.gas.jpbc.Element;
import objects.Attribute;
import objects.Coupon;
import objects.Msg;
import objects.Price;
import objects.SMDecrypt;
import objects.SecurityParam;
import objects.Token;
import objects.UserClient;
import objects.songs;
import objects.transSP;


public class MainActivity extends Activity {
    private ListView list;
    private MediaPlayer m;
    private ImageButton bt1;
    private ImageButton bt5;
    private TextView tx, tv8, tttv;
    private ImageView iv;
    private loginThread loginTask;
    private CheckThread checkTask;
    private storethread storeTask;
    private storethread1 storeTask1;
    private Registerthread RegisterTask;
    private PurchaseThread PurchaseTask;
    private rechargeThread rechargeTask;
    private Adapter<songs> Values, adp3;
    private priceAdapter<Price> adapter1;
    private ArrayAdapter<String> adp;
    private ArrayList<songs> store, transfercart, shoppingcart;
    private ArrayList<Integer> check, ids, prices, pids;
    private ArrayList<File> mysongs;
    private SeekBar sb;
    private int Flag = 0;
    private final Handler handler = new Handler();
    private AudioManager audioManager;
    private ProgressDialog progressDialog;
    private List<String> list4;
    private Boolean login = false;
    private UserClient userClient = null;
    private String r, b;
    private String uid = "";
    private String passwd = "";
    private int ppp = 0;
    private int l = 0;
    private int q = 0;
    private final Runnable updatePositionRunnable = new Runnable() {
        public void run() {
            updatePosition();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // set the actionbar color
        ActionBar bar = getActionBar();
        if (bar != null) {
            bar.setBackgroundDrawable(new ColorDrawable(0xffff6625));
        }
        // Initialize widget  ID with parameter
        list = (ListView) findViewById(R.id.listView);
        bt1 = (ImageButton) findViewById(R.id.play_pause);
        ImageButton bt2 = (ImageButton) findViewById(R.id.skip_next);
        ImageButton bt3 = (ImageButton) findViewById(R.id.skip_previous);
        bt5 = (ImageButton) findViewById(R.id.loop);
        tx = (TextView) findViewById(R.id.textView2);
        tttv = (TextView) findViewById(R.id.tttv);
        iv = (ImageView) findViewById(R.id.imageView);
        transfercart = new ArrayList<songs>();
        list4 = new ArrayList<String>();
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        shoppingcart = new ArrayList<>();
        store = new ArrayList<>();
        check = new ArrayList<>();
        SeekBar volControl = (SeekBar) findViewById(R.id.seekBar2);
        //set seekbar for the controlling volumn
        volControl.setMax(maxVolume);
        volControl.setProgress(curVolume);
        volControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        tx.setText("0");
        sb = (SeekBar) findViewById(R.id.seekBar);
        //read local music
        localread();
        //list read the adpater to show the local music list on the screen
        list.setAdapter(adp);
        iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_equalizer1_white_36dp));
        shoppingcart = new ArrayList<>();
        /////SELECT FUNCTION && listview click event handler
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tttv.setText(String.valueOf(position));
                if (Flag == 2) {
                    //when we change to read the shoping cart music list, listen the list item clickable event
                    list.setChoiceMode(ListView.CHOICE_MODE_NONE);
                    //read the all the id of the songs in the  shopping cart list
                    ids = new ArrayList<>();
                    //read the all the price id  of the songs in the  shopping cart list
                    pids = new ArrayList<>();
                    //read the all the prices of the songs in the  shopping cart list
                    prices = new ArrayList<>();
                    for (int i = 0; i < shoppingcart.size(); i++) {
                        int a = shoppingcart.get(i).id;
                        ids.add(a);
                        int b = shoppingcart.get(i).priceList.get(0).price;
                        prices.add(b);
                        int c = shoppingcart.get(i).priceList.get(0).pid;
                        pids.add(c);
                    }
                    //Pop out a window to confirm all the songs in the shopping cart list
                    AlertDialog.Builder confirmbuy = new AlertDialog.Builder(MainActivity.this, AlertDialog.THEME_HOLO_LIGHT);
                    confirmbuy.setTitle("CONFIRM");
                    confirmbuy.setMessage("Are you confirm to buy these songs?");
                    String p = position + "";
                    tx.setText(p);
                    confirmbuy.setNegativeButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Begin the prucharse thread to pruchase the songs
                            PurchaseTask = new PurchaseThread();
                            PurchaseTask.execute(ids, prices);
                        }
                    });
                    confirmbuy.setPositiveButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    confirmbuy.show();
                } else if (Flag == 1) {
                    //when we change to read the store music list, listen the list item clickable event
                    if (login) {
                        // if the user has login
                        list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                        ppp = position;
                        if (!store.get(position).priceList.get(0).conditions.equals(String.valueOf(0)) || store.get(position).priceList.size() > 1) {
                            //if the selected songs has condition price, then pop out a window to show priceslist of these window
                            AlertDialog.Builder set = new AlertDialog.Builder(MainActivity.this, AlertDialog.THEME_HOLO_LIGHT);
                            LayoutInflater li1 = LayoutInflater.from(MainActivity.this);
                            final View promptsView1 = li1.inflate(R.layout.condition, null);
                            set.setView(promptsView1);
                            final ListView cb = (ListView) promptsView1
                                    .findViewById(R.id.lv2);
                            adapter1 = new priceAdapter<>(MainActivity.this, R.layout.activity_griditem, store.get(position).priceList);
                            cb.setAdapter(adapter1);
                            cb.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position2, long id) {
                                    //Pricelist item click event, listening the click event to choose the selected price of the conditions
                                    int c = Integer.parseInt(tttv.getText().toString());
                                    if (transfercart.size() <= c) {
                                        transfercart = store;
                                    }
                                    transfercart.get(c).priceList.clear();
                                    //if the select song has been purchased, set the state of these songs true.
                                    if (userClient.G.contains(store.get(c).id * 25)) {
                                        transfercart.get(c).state = true;
                                    }
                                    transfercart.get(c).priceList.add(store.get(c).priceList.get(position2));
                                    list4 = new ArrayList<>();
                                    if (shoppingcart.size() == 0) {
                                        l = shoppingcart.size();
                                        shoppingcart.add(transfercart.get(c));
                                    } else {
                                        l = shoppingcart.size();
                                        list4.clear();
                                        for (int i = 0; i < shoppingcart.size(); i++) {
                                            String[] a = {shoppingcart.get(i).name};
                                            Collections.addAll(list4, a);
                                        }
                                        b = transfercart.get(ppp).name;
                                        if (!list4.contains(b)) {
                                            //if the selected item has not been added to the shoppingcart, it will add this item to the shoppingcart.
                                            shoppingcart.add(transfercart.get(ppp));
                                        } else if (list4.contains(b)) {
                                            //if the selecte item has been added to the shoppincart, it will remove the item form shopping cart
                                            shoppingcart.remove(transfercart.get(ppp));
                                        }
                                    }
                                }
                            });
                            set.setNegativeButton("Confirm", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }

                            });
                            set.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                            set.show();
                        } else {
                            //if the seleted song does not have conditions
                            if (shoppingcart.size() == 0) {
                                shoppingcart.add(store.get(position));
                                return;
                            } else {
                                list4.clear();
                                for (int i = 0; i < shoppingcart.size(); i++) {
                                    String[] a = {shoppingcart.get(i).name};
                                    Collections.addAll(list4, a);
                                }
                                b = store.get(position).name;
                                if (!list4.contains(b)) {
                                    //if the selected item has not been added to the shoppingcart, it will add this item to the shoppingcart.
                                    shoppingcart.add(store.get(position));
                                } else {
                                    shoppingcart.remove(store.get(position));
                                    //if the selecte item has been added to the shoppincart, it will remove the item form shopping cart
                                }
                            }
                        }
                        list.invalidateViews();
                    } else {
                        // if user has not login, use login function
                        signin();
                    }
                } else if (Flag == 0) {
                    //when we change to read the local music list, listen the list item clickable event
                    if (m != null) {
                        // if no song has been played, paly the selected song.
                        m.reset();
                        Uri u = Uri.parse(mysongs.get(position).toString());
                        m = MediaPlayer.create(getApplicationContext(), u);
                        sb.setMax(m.getDuration());
                        m.start();
                        String s = position + "";
                        tx.setText(s);
                        bt1.setImageResource(R.drawable.ic_pause_white_24dp);
                        tx.setSelected(true);
                        //update the seekbar of progress of the songs
                        updatePosition();
                        // Begin the animation
                        animate();
                        // if the song complete, begin the complete function
                        complete();
                    } else {
                        Uri u = Uri.parse(mysongs.get(position).toString());
                        m = MediaPlayer.create(getApplicationContext(), u);
                        sb.setMax(m.getDuration());
                        m.start();
                        //update the seekbar of progress of the songs
                        updatePosition();
                        String s = position + "";
                        tx.setText(s);
                        bt1.setImageResource(R.drawable.ic_pause_white_24dp);
                        tx.setSelected(true);
                        // Begin the animation
                        animate();
                        // if the song complete, begin the complete function
                        complete();

                    }

                }
            }
        });

        ////DELETE FUNCTION  &&  listview long click event handler
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            // Handle the long click event
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position4, long id) {
                q = position4;
                if (Flag == 0) {
                    //when we change to read the local music list, listen the list item long clickable event
                    // pop out a window to confirm you want to delete the songs
                    AlertDialog.Builder confirmd = new AlertDialog.Builder(MainActivity.this, AlertDialog.THEME_HOLO_LIGHT);
                    confirmd.setTitle("CONFIRM");
                    confirmd.setMessage("Are you confirm to delete seleted songs?");
                    confirmd.setNegativeButton("CONFIRM", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int position = q;
                            adp.remove(adp.getItem(position));
                            adp.notifyDataSetChanged();
                            File file = new File(mysongs.get(position).toString());
                            boolean deleted = file.delete();
                        }
                    });
                    confirmd.setPositiveButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    confirmd.show();

                }
                //when we change to read the store music list, listen the list item long clickable event
                if (Flag == 1)
                    if (!login) {
                        //if user has not login, use login function
                        signin();
                    }
                if (Flag == 2) {
                    //when we change to read the shopping cart music list, listen the list item long clickable event
                    //popout a window to help the user confirm to delete the selected song
                    AlertDialog.Builder confirmds = new AlertDialog.Builder(MainActivity.this, AlertDialog.THEME_HOLO_LIGHT);
                    confirmds.setTitle("CONFIRM");
                    confirmds.setMessage("Are you confirm to remove seleted song from shoppingcart?");
                    confirmds.setNegativeButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int position = q;
                            shoppingcart.remove(shoppingcart.get(position));
                            adp3 = new Adapter<>(MainActivity.this, R.layout.activity_griditem, shoppingcart);
                            adp3.notifyDataSetChanged();
                            list.setAdapter(adp3);
                            list.invalidateViews();
                            list4.clear();
                        }
                    });
                    confirmds.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    confirmds.show();
                }
                return true;
            }
        });
        /////PLAY BUTTON
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mysongs.size() > 0) {
                    //if there exist at least one songs store in local music dir
                    if (m != null) {
                        //if no songs has been played. play the song
                        if (m.isPlaying()) {
                            // there is a song playing, click button stop playing
                            m.pause();
                            bt1.setImageResource(R.drawable.ic_play_arrow_white_24dp);
                            iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_equalizer1_white_36dp));
                        } else {
                            // if there isnot a song playing, play the first song
                            m.start();
                            updatePosition();
                            bt1.setImageResource(R.drawable.ic_pause_white_24dp);
                            animate();
                            complete();
                        }
                    } else {
                        //there existed song song has played before, continue playing the song
                        Uri u = Uri.parse(mysongs.get(0).toString());
                        m = MediaPlayer.create(getApplicationContext(), u);
                        sb.setMax(m.getDuration());
                        String l = 0 + "";
                        tx.setText(l);
                        m.start();
                        updatePosition();
                        bt1.setImageResource(R.drawable.ic_pause_white_24dp);
                        animate();
                        complete();
                    }
                } else {
                    //tell user there is no songs downloaded
                    toast("There is no song downloaded");
                }
            }
        });
        /////NEXT BUTTON
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mysongs.size() > 0) {
                    //if there exist at least one songs store in local music dir
                    m.reset();
                    int position = Integer.valueOf(tx.getText().toString());
                    int p = position + 1;
                    String s = p + "";
                    m.setLooping(false);
                    if (mysongs.size() > position + 1) {
                        //there exist next  song, play the next song
                        Uri u = Uri.parse(mysongs.get(position + 1).toString());
                        m = MediaPlayer.create(getApplicationContext(), u);
                        sb.setMax(m.getDuration());
                        animate();
                        complete();
                        m.start();
                        updatePosition();
                        tx.setText(s);
                        bt1.setImageResource(R.drawable.ic_pause_white_24dp);
                        bt5.setImageResource(R.drawable.loop248);
                    } else if (mysongs.size() <= position + 1) {
                        //there exist no next song, play the first song in the local music list
                        Uri u = Uri.parse(mysongs.get(0).toString());
                        m = MediaPlayer.create(getApplicationContext(), u);
                        sb.setMax(m.getDuration());
                        m.start();
                        updatePosition();
                        animate();
                        complete();
                        tx.setText("0");
                        bt1.setImageResource(R.drawable.ic_pause_white_24dp);
                        bt5.setImageResource(R.drawable.loop248);
                    }
                } else {
                    // tell user there is no songs downloaded
                    toast("There is no songs downloaded");
                }
            }
        });
        ////PREVIOUS BUTTON
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mysongs.size() > 0) {
                    //if there exist at least one songs store in local music dir
                    m.reset();
                    int position = Integer.valueOf(tx.getText().toString());
                    int p = position - 1;
                    String s = p + "";
                    if (p >= 0) {
                        ///if there exist previous songs, play the previous song
                        Uri u = Uri.parse(mysongs.get(p).toString());
                        m = MediaPlayer.create(getApplicationContext(), u);
                        sb.setMax(m.getDuration());
                        m.start();
                        animate();
                        updatePosition();
                        tx.setText(s);
                        complete();
                        bt1.setImageResource(R.drawable.ic_pause_white_24dp);
                        bt5.setImageResource(R.drawable.loop248);
                    } else if (p <= 0) {
                        ///if there exist no previous songs, play the first song
                        Uri u = Uri.parse(mysongs.get(0).toString());
                        m = MediaPlayer.create(getApplicationContext(), u);
                        sb.setMax(m.getDuration());
                        m.start();
                        updatePosition();
                        complete();
                        tx.setText(0 + "");
                        bt1.setImageResource(R.drawable.ic_pause_white_24dp);
                        bt5.setImageResource(R.drawable.loop248);
                    }
                } else {
                    //tell user there is no songs downloaded
                    toast("There is no songs  downloaded");
                }
            }
        });
        /////LOOP FUNCTION
        bt5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (m != null) {
                    //if there exist a song has been played
                    if (m.isLooping()) {
                        //if the state of the loop has been setted to true, set it to false
                        m.setLooping(false);
                        bt5.setImageResource(R.drawable.loop248);
                    } else {
                        //if the state of the loop has been setted to false, set it to true
                        m.setLooping(true);
                        bt5.setImageResource(R.drawable.loop148);
                    }
                }
            }
        });
        /////MUSIC PROGRESS FUNCTION
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            boolean userTouch;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ////if the user touch the seekbar, the music will update to the point of progress
                if (m != null && userTouch) {
                    m.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                userTouch = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                userTouch = false;
            }
        });
    }

    ///////READ LOCAL MUSIC FUNCTION
    private void localread() {
        mysongs = findSongs(new File("/sdcard/Music/"));
        String[] items = new String[mysongs.size()];
        for (int i = 0; i < mysongs.size(); i++) {
            items[i] = mysongs.get(i).getName().replace(".mp3", "");
        }
        List<String> list3 = new ArrayList<>();
        Collections.addAll(list3, items);
        adp = new ArrayAdapter<>(this, R.layout.activity_griditem, R.id.textView, list3);
    }

    ////////COMPLETE FUNCTION
    ///////when the songs finished, it will use this function
    private void complete() {
        m.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                int position = Integer.valueOf(tx.getText().toString());
                int p = position + 1;
                localread();
                if (m.isLooping()) {
                    tx.setText(position + "");
                } else {
                    if (p < mysongs.size()) {
                        Uri u = Uri.parse(mysongs.get(position + 1).toString());
                        AnimationDrawable animation = (AnimationDrawable) iv.getDrawable();
                        animation.start();
                        m = MediaPlayer.create(getApplicationContext(), u);
                        sb.setMax(m.getDuration());
                        sb.setProgress(0);
                        m.start();
                        updatePosition();
                        bt5.setImageResource(R.drawable.loop248);
                    } else {
                        Uri u = Uri.parse(mysongs.get(0).toString());
                        AnimationDrawable animation = (AnimationDrawable) iv.getDrawable();
                        animation.start();
                        tx.setText("0");
                        m = MediaPlayer.create(getApplicationContext(), u);
                        sb.setMax(m.getDuration());
                        sb.setProgress(0);
                        m.start();
                        updatePosition();
                        bt5.setImageResource(R.drawable.loop248);
                    }
                }
            }
        });
    }

    ////////UPADATE PROGRESS FUNCTION
    private void updatePosition() {
        handler.removeCallbacks(updatePositionRunnable);
        sb.setProgress(m.getCurrentPosition());
        handler.postDelayed(updatePositionRunnable, 2000);
    }

    ///////LOGIN FUNCTION
    private void signin() {
        //pop out a signin window
        AlertDialog.Builder Signin = new AlertDialog.Builder(MainActivity.this, AlertDialog.THEME_HOLO_LIGHT);
        LayoutInflater li = LayoutInflater.from(MainActivity.this);
        View promptsView = li.inflate(R.layout.login, null);
        // Add action buttons
        Signin.setView(promptsView);
        final EditText un = (EditText) promptsView
                .findViewById(R.id.username);
        final EditText pd = (EditText) promptsView
                .findViewById(R.id.password);
        Signin.setPositiveButton("SIGN UP", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //click signup pop out  a signup window
                AlertDialog.Builder SignUp = new AlertDialog.Builder(MainActivity.this, AlertDialog.THEME_HOLO_LIGHT);
                LayoutInflater li1 = LayoutInflater.from(MainActivity.this);
                View promptsView1 = li1.inflate(R.layout.login2, null);
                SignUp.setView(promptsView1);
                final EditText un1 = (EditText) promptsView1
                        .findViewById(R.id.un1);
                final EditText pd1 = (EditText) promptsView1
                        .findViewById(R.id.pd1);
                final EditText repd1 = (EditText) promptsView1
                        .findViewById(R.id.repd1);
                un1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        String username = un1.getText().toString().trim();
                        if (!username.isEmpty())
                            //check the imput user name exist or not
                            new CheckThread().execute(username);
                    }
                });
                SignUp.setNegativeButton("SIGN UP", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String u = un1.getText().toString().trim();
                        String p = pd1.getText().toString().trim();
                        String r = repd1.getText().toString().trim();
                        if (!(u.isEmpty() || p.isEmpty() || r.isEmpty())) {
                            if (r.equals(p)) {
                                try {
                                    //user register function to creat a new account
                                    RegisterTask = new Registerthread();
                                    RegisterTask.execute(u, p);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                // input different password, hint
                                toast("Please Enter the same Password");
                                signin();
                            }
                        } else {
                            // not all the information has been input, hint
                            toast("Information must be imput");
                            signin();
                        }
                    }
                });
                SignUp.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                SignUp.show();
            }
        });


        Signin.setNegativeButton("SIGN IN", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String u = un.getText().toString();
                String p = pd.getText().toString();
                //Start login thread
                loginTask = new loginThread();
                loginTask.execute(u, p);

            }
        });
        Signin.show();
    }

    //ANIMATE FUNCTION
    private void animate() {
        iv.setImageResource(R.drawable.ic_equalizer_white_36dp);
        AnimationDrawable animation = (AnimationDrawable) iv.getDrawable();
        animation.start();
    }

    //READ ALL THE MP3 FILE UNDER THE SPECIFIC DIR
    public ArrayList<File> findSongs(File root) {
        ArrayList<File> a1 = new ArrayList<>();
        File[] files = root.listFiles();
        for (File singleFile : files) {
            if (singleFile.isDirectory() && !singleFile.isHidden()) {
                a1.addAll(findSongs(singleFile));
            } else {
                if (singleFile.getName().endsWith(".mp3")) {
                    a1.add(singleFile);
                }
            }
        }
        return a1;
    }

    //DESTORY FUNCTION, END THREAD
    @Override
    protected void onDestroy() {
        handler.removeCallbacks(updatePositionRunnable);
        if (loginTask != null) {
            loginTask.onCancelled();
        }
        if (RegisterTask != null) {
            RegisterTask.onCancelled();
        }
        if (checkTask != null) {
            checkTask.onCancelled();
        }
        if (storeTask != null) {
            storeTask.onCancelled();
        }
        super.onDestroy();
    }

    //TOAST FUNCTION, SHOW HINTS
    public void toast(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    //CREAT MENU
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        tv8 = new TextView(this);
        tv8.setText(" Please Login ");
        tv8.setTextColor(getResources().getColor(R.color.accentColor));
        tv8.setPadding(0, 5, 0, 5);
        tv8.setTypeface(null, Typeface.BOLD);
        tv8.setTextSize(14);
        menu.add(0, 1, 1, "xixi").setActionView(tv8).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        //add search view
        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView search = (SearchView) menu.findItem(R.id.action_search).getActionView();
        search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Flag = 1;

                storeTask = new storethread();
                storeTask.execute("<#SEARCH#>" + query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_Sign_in) {
            signin();
            return true;
        }
        if (id == R.id.action_store) {
            Flag = 1;
            if (!login) {
                storeTask = new storethread();
                storeTask.execute("<#SEARCH#>");
            } else {
                tv8.setText("Current Balance:" + userClient.currentBalance);
                //start store1 thread
                storeTask1 = new storethread1();
                storeTask1.execute("<#SEARCH#>");
            }
            return true;
        }
        if (id == R.id.action_Local) {
            Flag = 0;
            localread();
            list.setAdapter(adp);
            return true;
        }
        if (id == R.id.action_ShoppingCart) {
            Flag = 2;
            if (login) {
                tv8.setText("Current Balance:" + userClient.currentBalance);
            }
            adp3 = new Adapter<>(MainActivity.this, R.layout.activity_griditem, shoppingcart);
            list.setAdapter(adp3);
            return true;
        }
        if (id == R.id.action_Recharge) {
            if (!login) {
                signin();
            } else {
                //pop out recharge window
                AlertDialog.Builder recharge = new AlertDialog.Builder(MainActivity.this, AlertDialog.THEME_HOLO_LIGHT);
                LayoutInflater li1 = LayoutInflater.from(MainActivity.this);
                View promptsView1 = li1.inflate(R.layout.recharge, null);
                recharge.setView(promptsView1);
                final EditText pd2 = (EditText) promptsView1
                        .findViewById(R.id.pd2);
                recharge.setNegativeButton("Recharge", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String p = pd2.getText().toString();
                        rechargeTask = new rechargeThread();
                        rechargeTask.execute(p);
                    }
                });
                recharge.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                recharge.show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    //LOGIN THREAD
    private class loginThread extends AsyncTask<String, String, UserClient> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected UserClient doInBackground(String... params) {
            passwd = params[1];
            ObjectOutputStream out;
            ObjectInputStream in;
            UserClient result = null;
            try {
                KeyStore keystore = KeyStore.getInstance(SSL_KEY_KEYSTORE);
                keystore.load(getResources().openRawResource(R.raw.server_trust), KEY_PASSWD.toCharArray());
                TrustManagerFactory tmf = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                tmf.init(keystore);
                SSLContext context = SSLContext.getInstance(SSL_AGREEMENT);
                context.init(null, tmf.getTrustManagers(), null);
                SSLSocket socket = (SSLSocket) context.getSocketFactory().createSocket(SERVER_IP, SERVER_PORT);
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());
                out.writeUTF("<#LOGIN#>" + params[0] + "|" + hash(params[1]));
                out.flush();
                r = in.readUTF();
                if (r.startsWith("T")) {
                    String[] sa = r.split("\\|");
                    uid = sa[1];
                    byte[] a = (byte[]) in.readObject();
                    Object o = decryptObject(a, params[1]);
                    result = (UserClient) o;
                    result.check(Integer.parseInt(sa[2]));
                }
                out.writeUTF("<#LOGOUT#>");
                out.flush();
                out.close();
                in.close();
                socket.close();
                return result;
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(UserClient result) {
            super.onPostExecute(result);
            userClient = result;
            //handle different result within login thread
            if (r.startsWith("T")) {
                tv8.setText("CurrentBalance:" + userClient.currentBalance);
                storeTask1 = new storethread1();
                storeTask1.execute("<#SEARCH#>");
                if (login) {
                    shoppingcart.clear();
                    list4.clear();
                }
                login = true;
            } else if (r.startsWith("W")) {
                // WAIT MS
                login = false;
                toast("This user should Wait" + r.substring(2) + "ms");
                signin();
            } else if (r.startsWith("N")) {
                // NULL ID
                login = false;
                toast("User not exist!");
                signin();
            } else if (r.startsWith("F")) {
                // WRONG PASSWORD
                login = false;
                toast("Username or Password Error!");
                signin();
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values[0]);

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    //REGISTER FUNCTION
    private class Registerthread extends AsyncTask<String, String, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(String... params) {
            ObjectOutputStream out;
            ObjectInputStream in;
            try {
                Integer idx = 0;
                KeyStore keystore = KeyStore.getInstance(SSL_KEY_KEYSTORE);
                keystore.load(getResources().openRawResource(R.raw.server_trust), KEY_PASSWD.toCharArray());
                TrustManagerFactory tmf = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                tmf.init(keystore);
                SSLContext context = SSLContext.getInstance(SSL_AGREEMENT);
                context.init(null, tmf.getTrustManagers(), null);
                SSLSocket socket = (SSLSocket) context.getSocketFactory().createSocket(SERVER_IP, SERVER_PORT);
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());
                passwd = params[1];
                out.writeUTF("<#REGISTER#>" + params[0] + "|" + hash(passwd));
                out.flush();
                idx = in.readInt();
                if (idx != 0) {
                    UserClient userClient1 = new UserClient(0, ((transSP) in.readObject()));
                    out.writeObject(encryptObject(userClient1, params[1]));
                    userClient = userClient1;
                    out.flush();
                    if (!in.readBoolean())
                        idx = 0;
                }
                out.writeUTF("<#LOGOUT#>");
                out.flush();
                out.close();
                in.close();
                socket.close();
                return idx;
            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            uid = String.valueOf(result);

            tv8.setText("Current Balance:" + userClient.currentBalance);
            storeTask1 = new storethread1();
            storeTask1.execute("<#SEARCH#>");
            if (login) {
                shoppingcart.clear();
                list4.clear();
            }
            login = true;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values[0]);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    //CHECK THREAD
    private class CheckThread extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            ObjectOutputStream out;
            ObjectInputStream in;
            try {
                KeyStore keystore = KeyStore.getInstance(SSL_KEY_KEYSTORE);
                keystore.load(getResources().openRawResource(R.raw.server_trust), KEY_PASSWD.toCharArray());
                TrustManagerFactory tmf = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                tmf.init(keystore);
                SSLContext context = SSLContext.getInstance(SSL_AGREEMENT);
                context.init(null, tmf.getTrustManagers(), null);
                SSLSocket socket = (SSLSocket) context.getSocketFactory().createSocket(SERVER_IP, SERVER_PORT);
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());
                out.writeUTF("<#CHECK#>" + params[0]);
                out.flush();
                String result = in.readUTF();
                out.writeUTF("<#LOGOUT#>");
                out.flush();
                out.close();
                in.close();
                socket.close();
                return result;
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result.equals("0")) {
                toast("UserName Existed");
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values[0]);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    //STORE THREAD FOR NON-LOGIN  USER
    private class storethread extends AsyncTask<String, String, ArrayList<songs>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<songs> doInBackground(String... params) {
            ObjectOutputStream out;
            ObjectInputStream in;
            try {
                ArrayList<songs> cart = new ArrayList<>();
                KeyStore keystore = KeyStore.getInstance(SSL_KEY_KEYSTORE);
                keystore.load(getResources().openRawResource(R.raw.server_trust), KEY_PASSWD.toCharArray());
                TrustManagerFactory tmf = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                tmf.init(keystore);
                SSLContext context = SSLContext.getInstance(SSL_AGREEMENT);
                context.init(null, tmf.getTrustManagers(), null);
                SSLSocket socket = (SSLSocket) context.getSocketFactory().createSocket(SERVER_IP, SERVER_PORT);
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());
                out.writeUTF(params[0]);
                out.flush();
                int num = in.readInt();
                for (int i = 0; i < num; ++i) {
                    Object obj = in.readObject();
                    songs song = (songs) obj;
                    cart.add(song);
                }
                out.writeUTF("<#LOGOUT#>");
                out.flush();
                out.close();
                in.close();
                socket.close();
                return cart;
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<songs> cart) {
            super.onPostExecute(cart);
            store = cart;
            transfercart = store;
            Flag = 1;
            Values = new Adapter<>(MainActivity.this, R.layout.activity_griditem, cart);
            list.setAdapter(Values);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values[0]);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    //STORE THREAD FOR LOGIN USER
    private class storethread1 extends AsyncTask<String, String, ArrayList<songs>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<songs> doInBackground(String... params) {
            ObjectOutputStream out;
            ObjectInputStream in;
            try {
                ArrayList<songs> cart = new ArrayList<>();
                KeyStore keystore = KeyStore.getInstance(SSL_KEY_KEYSTORE);
                keystore.load(getResources().openRawResource(R.raw.server_trust), KEY_PASSWD.toCharArray());
                TrustManagerFactory tmf = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                tmf.init(keystore);
                SSLContext context = SSLContext.getInstance(SSL_AGREEMENT);
                context.init(null, tmf.getTrustManagers(), null);
                SSLSocket socket = (SSLSocket) context.getSocketFactory().createSocket(SERVER_IP, SERVER_PORT);
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());
                out.writeUTF(params[0]);
                out.flush();
                int num = in.readInt();
                for (int i = 0; i < num; ++i) {
                    Object obj = in.readObject();
                    songs song = (songs) obj;

                    if (userClient.G.contains(song.id * 25)) {
                        song.state = true;
                    }
                    cart.add(song);
                }
                out.writeUTF("<#LOGOUT#>");
                out.flush();
                out.close();
                in.close();
                socket.close();
                return cart;
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<songs> cart) {
            super.onPostExecute(cart);
            store = cart;
            Flag = 1;
            Values = new Adapter<>(MainActivity.this, R.layout.activity_griditem, cart);
            list.setAdapter(Values);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values[0]);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    //PURCHASE FUNCTION
    private class PurchaseThread extends AsyncTask<ArrayList<Integer>, String, ArrayList<String>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(MainActivity.this, "Wait", "Purchasing and Downloading ...");
        }

        @Override
        protected ArrayList<String> doInBackground(ArrayList<Integer>... params) {
            ObjectOutputStream out;
            ObjectInputStream in;
            ArrayList<String> result = new ArrayList<>();
            try {
                KeyStore keystore = KeyStore.getInstance(SSL_KEY_KEYSTORE);
                keystore.load(getResources().openRawResource(R.raw.server_trust), KEY_PASSWD.toCharArray());
                TrustManagerFactory tmf = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                tmf.init(keystore);
                SSLContext context = SSLContext.getInstance(SSL_AGREEMENT);
                context.init(null, tmf.getTrustManagers(), null);
                SSLSocket socket = (SSLSocket) context.getSocketFactory().createSocket(SERVER_IP, SERVER_PORT);
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());
                int totalPrice = 0;
                ArrayList<Integer> newG = new ArrayList<>(userClient.G);
                ArrayList<Integer> finalG = new ArrayList<>(userClient.G);
                ArrayList<Integer> purchaseList = new ArrayList<>();
                ArrayList<Integer> purchasePidList = new ArrayList<>();
                for (int i = 0; i < ids.size(); i++) {
                    int idx = ids.get(i);
                    int price = prices.get(i);
                    int pid = pids.get(i);
                    if (!newG.contains(idx * 25)) {
                        newG.addAll(Attribute.GetAttributes(idx, price));
                        purchaseList.add(idx);
                        purchasePidList.add(pid);
                        totalPrice += price;
                    }
                }
                if (userClient.currentBalance < totalPrice) {
                    publishProgress("s");
                    return result;
                }
                out.writeUTF("<#BUY#>" + uid);
                out.flush();
                SecurityParam sp = userClient.tSP.toSP();
                int totalAttributeCnt = in.readInt();
                SMDecrypt smd = new SMDecrypt(sp, newG);
                Element PG = smd.genToken();
                Element sigmaA = smd.GenAProof(totalAttributeCnt,
                        userClient.totalDeposit);
                Element sigmaR = smd.GenRProof(userClient.G, sp.pairing.getZr().newElementFromBytes(userClient.sk));
                Token tp = new Token(PG, sigmaA, sigmaR);
                out.writeObject(tp);
                out.flush();
                int size = in.readInt();
                for (int i = 0; i < size; i++) {
                    Msg msg = (Msg) in.readObject();
                    if (pids.indexOf(msg.pid) == -1)
                        continue;
                    Element D = smd.Decrpyt(msg.cipher);
                    String code = Coupon.elementToCode(D, sp.pairing);
                    if (Coupon.checkValidation(code)) {
                        int p = 0;
                        for (int ii = 0; ii < shoppingcart.size(); ii++)
                            if (msg.idx == shoppingcart.get(ii).id)
                                p = shoppingcart.get(ii).priceList.get(0).price;
                        finalG.addAll(Attribute.GetAttributes(msg.idx, p));
                        result.add(code + "|" + msg.idx);
                        //download form server
                    } else {
                        int ii;
                        if (purchasePidList.contains(msg.pid) && (ii = pids.indexOf(msg.pid)) >= 0) {
                            totalPrice -= prices.get(ii);
                            newG.removeAll(Attribute.GetAttributes(msg.idx, prices.get(ii)));
                        }
                    }
                }
                //send the PG that actually take effective
                smd.setG(finalG);
                Element updatedPG = smd.getTokenByCurrentParam();
                out.writeObject(updatedPG.toBytes());
                //
                if (result.size() == 0) {
                    publishProgress("q");
                    out.writeUTF("<#LOGOUT#>");
                    out.flush();
                    out.close();
                    in.close();
                    socket.close();
                } else {
                    // client update user info
                    int currentBalance = userClient.currentBalance - totalPrice;
                    userClient.update(currentBalance, finalG, smd.GetSk());
                    //back up user information to the server
                    out.writeUTF("<#BACKUP#>|" + uid + "|" + hash(passwd));
                    out.writeObject(encryptObject(userClient, passwd));
                    out.flush();
                    if (!in.readBoolean()) result = null;
                    out.writeUTF("<#LOGOUT#>");
                    out.flush();
                    out.close();
                    in.close();
                    socket.close();
                }
                return result;
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {
            super.onPostExecute(result);
            String filename = "unknown";
            toast(result.size() + "songs success");
            for (String r : result) {
                String[] sa = r.split("\\|");
                for (int i = 0; i < shoppingcart.size(); i++)
                    if (sa[1].equals(shoppingcart.get(i).id + ""))
                        filename = shoppingcart.get(i).name;
                //begin the coupon and download thread
                new couponthread().execute(sa[0], filename);
            }
            shoppingcart.clear();
            check.clear();
            adp3.notifyDataSetChanged();
            list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            tv8.setText("Current Balance:" + userClient.currentBalance);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values[0]);
            if (values[0].equals("s")) {
                progressDialog.cancel();
                AlertDialog.Builder tipcb = new AlertDialog.Builder(MainActivity.this, AlertDialog.THEME_HOLO_LIGHT);
                tipcb.setTitle("CONFIRM");
                tipcb.setMessage("Current Balance is not enough, please recharge");
                int position = 0;
                final String p = position + "";
                tx.setText(p);

                tipcb.setPositiveButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                tipcb.show();
            } else if (values[0].equals("q")) {
                progressDialog.cancel();

            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    //COUNPON AND DOWNLOAD THREAD
    private class couponthread extends AsyncTask<String, String, byte[]> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected byte[] doInBackground(String... params) {
            ObjectOutputStream out;
            ObjectInputStream in;
            try {
                KeyStore keystore = KeyStore.getInstance(SSL_KEY_KEYSTORE);
                keystore.load(getResources().openRawResource(R.raw.server_trust), KEY_PASSWD.toCharArray());
                TrustManagerFactory tmf = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                tmf.init(keystore);
                SSLContext context = SSLContext.getInstance(SSL_AGREEMENT);
                context.init(null, tmf.getTrustManagers(), null);
                SSLSocket socket = (SSLSocket) context.getSocketFactory().createSocket(SERVER_IP, SERVER_PORT);
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());
                out.writeUTF("<#DOWNLOAD#>" + params[0]);
                out.flush();
                byte[] result = (byte[]) in.readObject();
                out.writeUTF("<#LOGOUT#>");
                out.flush();
                out.close();
                in.close();
                socket.close();
                if (result.length == 0)
                    toast("Coupon Code Error!!!");
                else
                    writeFile(result, "/sdcard/Music/" + params[1] + ".mp3");
                return result;
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(byte[] result) {
            super.onPostExecute(result);
            adp.notifyDataSetChanged();
            progressDialog.cancel();
            localread();
            adp.notifyDataSetChanged();
            list.setAdapter(adp);
            Flag = 0;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values[0]);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    //REACHARGE THREAD
    private class rechargeThread extends AsyncTask<String, String, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(String... params) {
            ObjectOutputStream out;
            ObjectInputStream in;
            Integer result;
            try {
                KeyStore keystore = KeyStore.getInstance(SSL_KEY_KEYSTORE);
                keystore.load(getResources().openRawResource(R.raw.server_trust), KEY_PASSWD.toCharArray());
                TrustManagerFactory tmf = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                tmf.init(keystore);
                SSLContext context = SSLContext.getInstance(SSL_AGREEMENT);
                context.init(null, tmf.getTrustManagers(), null);
                SSLSocket socket = (SSLSocket) context.getSocketFactory().createSocket(SERVER_IP, SERVER_PORT);
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());
                out.writeUTF("<#RECHARGE#>" + uid + "|" + params[0]);
                out.flush();
                result = in.readInt();
                out.writeUTF("<#LOGOUT#>");
                out.flush();
                out.close();
                in.close();
                socket.close();
                return result;
            } catch (Exception e) {
                return 0;
            }
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            if (result == -1)
                toast("Recharge Code Invalid OR Used!!!");
            else {
                toast("Recharge Succeed. Value: " + result);
                userClient.check(userClient.totalDeposit + result);
                tv8.setText("Current Balance:" + userClient.currentBalance);
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values[0]);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }
}
