package com.sputa.avarez;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.JsonReader;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sputa.avarez.adapters.adapter_driving;
import com.sputa.avarez.adapters.adapter_tablo;
import com.sputa.avarez.adapters.item_adapter;
import com.sputa.avarez.adapters.item_cars_adapter;
import com.sputa.avarez.classes.StaticGasGhabz;
import com.sputa.avarez.classes.StaticWaterGhabz;
import com.sputa.avarez.model.DrivingBill;
import com.sputa.avarez.model.ElectricBill;
import com.sputa.avarez.model.ItemDriving;
import com.sputa.avarez.model.TelphoneBill;
import com.sputa.avarez.model.items;
import com.sputa.avarez.model.items_cars;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.sputa.avarez.Functions.Lag;

public class GhabzSearch extends AppCompatActivity {



    private int screenWidth;
    private int screenHeight;
    private boolean is_requested;
    //MyAsyncTask mm;
    private String last_query;
    RelativeLayout lay_main;
    private Functions fun;
    private int tim = 1;
    private Timer timer;


    List<ItemDriving> itemDriving = new ArrayList<>();
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private String rslt_NationalNumber;
    private String rslt_ChassiSerial;
    private String rslt_VinNumber;
    private String public_eshterak;
    private String public_shenase;
    private String ghabz_id;
    private boolean allowBack = true;
    private String ghabz_type = "electric";
    private SQLiteDatabase myDB;
    private String price_all = "0";
    private MyAsyncTask mm;
    private String last_requested_query;
    private String JsonResult = "";
    public JsonTask jsonTask;
    private String PaymentUrl="";

    private void set_size(int vid, Double width, Double height, String typ) {
        View v = findViewById(vid);
        if (typ.equals("cons")) {
            ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) v.getLayoutParams();
            lp.width = (int) (screenWidth * width);
            lp.height = (int) (screenHeight * height);
            ;
            v.setLayoutParams(lp);
        }
        if (typ.equals("line")) {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) v.getLayoutParams();
            lp.width = (int) (screenWidth * width);
            lp.height = (int) (screenHeight * height);
            ;
            v.setLayoutParams(lp);
        }
        if (typ.equals("rel")) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) v.getLayoutParams();
            lp.width = (int) (screenWidth * width);
            lp.height = (int) (screenHeight * height);
            ;
            v.setLayoutParams(lp);
        }

    }

    private void set_size_txt(int vid, Double size, String typ) {
        TextView v = (TextView) findViewById(vid);
        if (typ.equals("cons")) {
            ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) v.getLayoutParams();
            v.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * size));
            v.setLayoutParams(lp);
        }
        if (typ.equals("line")) {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) v.getLayoutParams();
            v.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * size));
            v.setLayoutParams(lp);
        }
        if (typ.equals("rel")) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) v.getLayoutParams();
            v.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * size));
            v.setLayoutParams(lp);
        }
    }

    private void set_size_edit(int vid, Double size, String typ) {
        EditText v = findViewById(vid);
        if (typ.equals("cons")) {
            ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) v.getLayoutParams();
            v.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * size));
            v.setLayoutParams(lp);
        }
        if (typ.equals("line")) {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) v.getLayoutParams();
            v.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * size));
            v.setLayoutParams(lp);
        }
        if (typ.equals("rel")) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) v.getLayoutParams();
            v.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * size));
            v.setLayoutParams(lp);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghabz_search);
        lay_main = findViewById(R.id.lay_main);

        Intent I = getIntent();
        ghabz_type = I.getStringExtra("type");
        TextView lbl_title = findViewById(R.id.lbl_title);
        final TextView lbl_ghabz = findViewById(R.id.lbl_ghabz);
        if (ghabz_type.equals("gas"))
            lbl_title.setText("جستجوی قبض" + " گاز ");
        if (ghabz_type.equals("water"))
            lbl_title.setText("جستجوی قبض" + " آب ");
        if (ghabz_type.equals("electric"))
            lbl_title.setText("جستجوی قبض" + " برق ");
        if (ghabz_type.equals("telphone"))
            lbl_title.setText("جستجوی قبض" + " تلفن ");

        String[] arraySpinner = new String[]{
                "کد ملی", "شماره اشتراک"
        };
        if (ghabz_type.equals("electric")) {
            arraySpinner = new String[]{
                    "شناسه قبض"
            };
        }
        if (ghabz_type.equals("water")) {
            arraySpinner = new String[]{
                    "شناسه قبض"
            };
        }
        if (ghabz_type.equals("telphone")) {
            arraySpinner = new String[]{
                    "شماره تلفن"
            };
        }
        else if (ghabz_type.equals("gas")) {
            arraySpinner = new String[]{
                    "کد اشتراک"
            };
        }
        else if (ghabz_type.equals("driving")) {
            arraySpinner = new String[]{
                    "شماره بارکد"
            };
        }
        Spinner s = (Spinner) findViewById(R.id.spn_search_type);
        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        s.setAdapter(adapter);
        fun = new Functions();

        timer = new Timer("timeout");
        timer.start();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE); // the results will be higher than using the activity context object or the getWindowManager() shortcut
        assert wm != null;
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;

        set_size(R.id.txt_eshterak, .49, .07, "cons");
        set_size_txt(R.id.lbl_title, .065, "cons");
        set_size_txt(R.id.lbl_eshterak, .05, "cons");
        set_size_edit(R.id.txt_eshterak, .06, "cons");
        set_size_txt(R.id.lbl_search_type, .05, "cons");
        set_size_txt(R.id.lbl_msg_right, .039, "line");
        set_size_txt(R.id.lbl_msg_left, .039, "line");
        set_size(R.id.lay_message_main, .9, .18, "cons");
        Spinner spn = findViewById(R.id.spn_search_type);
        set_size(R.id.spn_search_type, .52, .07, "cons");
        spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                EditText txt_eshterak = findViewById(R.id.txt_eshterak);
                EditText txt_parvande = findViewById(R.id.txt_ghabz);

                txt_eshterak.setText("");
                txt_parvande.setText("");
                if (position == 0) {

                    ConstraintLayout lay_motor_search = findViewById(R.id.lay_eshterak_search);
                    lay_motor_search.setVisibility(View.GONE);
                    ConstraintLayout lay_ghabz_search = findViewById(R.id.lay_ghabz_search);
                    lay_ghabz_search.setVisibility(View.VISIBLE);
                    if (ghabz_type.equals("electric") || ghabz_type.equals("water")) {
                        EditText et = findViewById(R.id.txt_ghabz);
                        et.setHint("لطفا شناسه قبض را وارد کنید");
                        lbl_ghabz.setText("شناسه قبض");
                    }
                    if (ghabz_type.equals("gas")) {
                        EditText et = findViewById(R.id.txt_ghabz);
                        et.setHint("لطفا کد اشتراک را وارد کنید");
                        lbl_ghabz.setText("کد اشتراک");
                    }
                    if (ghabz_type.equals("driving")) {
                        EditText et = findViewById(R.id.txt_ghabz);
                        et.setHint("بارکد کارت ماشین ");
//                        set_size_txt(R.id.txt_ghabz, .027, "cons");
                        lbl_ghabz.setText("بارکد");
                    }
                    if (ghabz_type.equals("telphone")) {
                        EditText et = findViewById(R.id.txt_ghabz);
                        et.setHint(" شماره تلفن به همراه کد شهر");
//                        set_size_txt(R.id.txt_ghabz, .027, "cons");
                        lbl_ghabz.setText("شماره تلفن");
                    }
                }
                if (position == 1) {
                    ConstraintLayout lay_motor_search = findViewById(R.id.lay_eshterak_search);
                    lay_motor_search.setVisibility(View.VISIBLE);
                    ConstraintLayout lay_ghabz_search = findViewById(R.id.lay_ghabz_search);
                    lay_ghabz_search.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
//        ConstraintLayout.LayoutParams lp= (ConstraintLayout.LayoutParams) spn.getLayoutParams();
//        spn.sette setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * size));
//        spn.setLayoutParams(lp);

        set_size(R.id.txt_ghabz, .54, .07, "cons");
        set_size_txt(R.id.lbl_ghabz, .04, "cons");
        set_size_edit(R.id.txt_ghabz, .04, "cons");
        set_size_edit(R.id.txt_eshterak, .04, "cons");


        set_size(R.id.btn_search, .3, .065, "cons");
        set_size(R.id.btn_back, .3, .065, "cons");
        set_size_txt(R.id.lbl_search, .054, "line");
        set_size_txt(R.id.lbl_back, .054, "line");
        set_size(R.id.btn_pay, .3, .065, "cons");
        set_size(R.id.btn_detail, .3, .065, "cons");

        set_size_txt(R.id.lbl_help, .033, "cons");
        set_size(R.id.img_help_motor, .08, .06, "cons");
        set_size(R.id.img_help_ghabz, .08, .06, "cons");


        set_size(R.id.lay_confirm, 0.95, .14, "cons");
        set_size(R.id.btn_yes_correct, .12, .052, "cons");
        set_size(R.id.btn_no_will_correct, .3, .052, "cons");
        set_size_txt(R.id.lbl_confirm_msg, .044, "cons");
        set_size_txt(R.id.lbl_yes_correct, .04, "line");
        set_size_txt(R.id.lbl_no_will_correct, .04, "line");

        set_size(R.id.lay_complete_info, 0.83, .76, "rel");
        set_size(R.id.btn_save_complete, 0.26, .06, "cons");
        set_size(R.id.btn_back_complete, 0.26, .06, "cons");

        set_size(R.id.txt_name_complete, .44, .07, "cons");
        set_size(R.id.txt_family_complete, .44, .07, "cons");
        set_size_txt(R.id.lbl_name_complete, .04, "cons");
        set_size_edit(R.id.txt_name_complete, .04, "cons");

        set_size_txt(R.id.lbl_family_complete, .04, "cons");
        set_size_edit(R.id.txt_family_complete, .04, "cons");

        set_size_txt(R.id.lbl_melliId_complete, .04, "cons");
        set_size_edit(R.id.txt_melliID_complete, .044, "cons");
        set_size(R.id.txt_melliID_complete, .44, .07, "cons");

        set_size_txt(R.id.lbl_shasi_complete, .04, "cons");
        set_size_edit(R.id.txt_shasi_complete, .044, "cons");
        set_size(R.id.txt_shasi_complete, .44, .07, "cons");

        set_size_txt(R.id.lbl_VIN_complete, .04, "cons");
        set_size_txt(R.id.lbl_VIN_complete, .04, "cons");
        set_size_edit(R.id.txt_VIN_complete, .044, "cons");
        set_size(R.id.txt_VIN_complete, .44, .07, "cons");

        set_size_txt(R.id.lbl_complete_title, .051, "cons");


        set_size(R.id.lay_more_detail, .85, .8, "rel");
        set_size(R.id.lay_msg_more_detail, .7, .6, "cons");
        set_size_txt(R.id.lbl_more_detail_title, .045, "cons");
        set_size_txt(R.id.lbl_msg_right_detail, .04, "line");
        set_size_txt(R.id.lbl_msg_left_detail, .04, "line");


        myDB = this.openOrCreateDatabase(getString(R.string.DB_name), MODE_PRIVATE, null);


        Cursor cr = myDB.rawQuery("select * from water", null);
        if (cr.getCount() > 0) {
//            cr.moveToFirst();
//            Toast.makeText(this, cr.getString(0), Toast.LENGTH_SHORT).show();
//            cr.moveToNext();
//            Toast.makeText(this, cr.getString(0), Toast.LENGTH_SHORT).show();
        }

    }

    public void clk_search(View view) {
        EditText txt_eshterak = findViewById(R.id.txt_eshterak);
        EditText txt_ghabz = findViewById(R.id.txt_ghabz);


        String
                search_type = "none";
        if (txt_eshterak.getText().toString().length() > 0) {
            search_type = "ok";
        } else if (txt_ghabz.getText().toString().length() > 0) {
            search_type = "ok";
        }

        if (search_type.equals("ok")) {


            if (txt_eshterak.length() > 0 || txt_ghabz.length() > 0) {
                search_ghabz_melli(txt_eshterak.getText().toString(), txt_ghabz.getText().toString(), "eshterak");
            }


        } else {
            Toast.makeText(this, "لطفا اطلاعات موردنظر را وارد کنید", Toast.LENGTH_SHORT).show();
        }
    }

    private void search_ghabz_melli(String s, String s1, String typ) {
        if (ghabz_type.equals("gas")) {
            search_gas_ghabz(s, s1);
        }
        if (ghabz_type.equals("water")) {
            search_water_ghabz(s, s1);
        }
        if (ghabz_type.equals("electric")) {
            search_electric_ghabz(s, s1);
        }

        if (ghabz_type.equals("water")) {
            search_water_ghabz(s, s1);
        }
        if (ghabz_type.equals("telphone")) {
            search_telphone_ghabz(s, s1);
        }
        if (ghabz_type.equals("driving")) {
            search_driving_ghabz(s, s1);
        }

    }

    private void search_gas_ghabz(String eshterak, String ghabz) {
        //Toast.makeText(this, "123", Toast.LENGTH_SHORT).show();


        jsonTask = new JsonTask();
        String url = "http://app.e-paytoll.ir/api/Gas/GetBillInfo/" + ghabz + "/" + Functions.u_id;
        Lag(url);
//        Toast.makeText(this, url, Toast.LENGTH_SHORT).show();
        jsonTask.execute(url, "search_gas");



    }

    private void search_water_ghabz(String eshterak, String ghabz) {
        //Toast.makeText(this, "123", Toast.LENGTH_SHORT).show();

        jsonTask = new JsonTask();
        String url = "http://app.e-paytoll.ir/api/Water/GetBillInfo/" + ghabz + "/" + Functions.u_id;
        Lag(url);
//        Toast.makeText(this, url, Toast.LENGTH_SHORT).show();
        jsonTask.execute(url, "search_water");


    }

    private void search_electric_ghabz(String eshterak, String ghabz) {


        jsonTask = new JsonTask();
        String url = "http://app.e-paytoll.ir/api/Electric/GetBillInfo/" + ghabz + "/" + Functions.u_id;
//        Toast.makeText(this, url, Toast.LENGTH_SHORT).show();
        jsonTask.execute(url, "search_electric");


//        Toast.makeText(this, eshterak+"----"+ghabz, Toast.LENGTH_SHORT).show();
//        if(ghabz.length() == 13)
//        {
//
//
//            String
//                    URL="";
//            URL = "https://saapa.ir/b/"+ghabz;
//            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
//            startActivity(i);
//        }
//        else
//            {
//                Toast.makeText(this, "طول شناسه اشتراک باید 13 رقم باشد", Toast.LENGTH_SHORT).show();
//        }

        //Toast.makeText(this, "123", Toast.LENGTH_SHORT).show();
//        Cursor cr= myDB.rawQuery("select * from power where AboneID='"+eshterak+"'", null);
//        if(eshterak.length()>0) {
//            cr = myDB.rawQuery("select * from power where AboneID='"+eshterak+"'", null);
//            Log.d("majid","esht=");
//        }
//        else if(ghabz.length()>0) {
//            cr = myDB.rawQuery("select * from power where ID='"+ghabz+"'", null);
//            if (cr.getCount() > 0) {
//                cr.moveToFirst();
//                Log.d("majid","ghabz=");
//            }
//        }
//        if(cr!=null) {
//            if (cr.getCount() > 0) {
//                cr.moveToFirst();
//                String ID= cr.getString(0);
//                Log.d("majid","ok");
//                cr.moveToFirst();
//                String
//                        msg = "";
//                msg += " نام مالک"+ "\n" ;
//                msg += " مبلغ قابل پرداخت قبض" + "\n" ;
//                msg += " مبلغ بدهی قبض" + "\n" ;
//                msg += " کارکرد دوره" + "\n" ;
//
//                TextView lbl_msg = findViewById(R.id.lbl_msg_right);
//                lbl_msg.setText(msg);
//                price_all = cr.getString(11);
//
//                msg =" :   "+ cr.getString(2)+" "+cr.getString(3)+ "\n" ;
//                msg +=" :   "+ digiting(cr.getString(11))+ " ریال \n" ;
//                msg +=" :   "+ cr.getString(10)+ "\n" ;
//                msg +=" :   "+ cr.getString(9)+ "\n" ;
//
//                TextView lbl_msg1 = findViewById(R.id.lbl_msg_left);
//                lbl_msg1.setText(msg);
//
//                TextView lbl_msg_right_detail=findViewById(R.id.lbl_msg_right_detail);
//                TextView lbl_msg_left_detail=findViewById(R.id.lbl_msg_left_detail);
//
//                msg = "";
//                msg += "تاریخ قرائت پیشین" + "\n";
//                msg += "تاریخ قرائت فعلی" + "\n";
//                msg += "رقم پیشین شمارشگر" + "\n";
//                msg += "رقم فعلی شمارشگر" + "\n";
//                msg += "مصرف به کیلو وات" + "\n";
//                msg += "بهای برق مصرفی" + "\n";
//
//                lbl_msg_right_detail.setText(msg);
//                msg = "";
//                msg =" :   "+ cr.getString(5) + "\n" ;
//                msg +=" :   "+ cr.getString(6)+ "\n" ;
//                msg +=" :   "+ cr.getString(7)+ "\n" ;
//                msg +=" :   "+ cr.getString(8)+ "\n" ;
//                msg +=" :   "+ cr.getString(9)+ "\n" ;
//                msg +=" :   "+ digiting(cr.getString(11))+ " ریال \n" ;
//
//                lbl_msg_left_detail.setText(msg);
//
//                LinearLayout btn_pay = findViewById(R.id.btn_pay);
//                btn_pay.setVisibility(View.VISIBLE);
//                LinearLayout btn_detail = findViewById(R.id.btn_detail);
//                btn_detail.setVisibility(View.VISIBLE);
//
//
//
//
//                bookmark_ghabz(ID,"electric");
//            }
//        }
//        else
//            Toast.makeText(this, "اشتراک پیدا نشد", Toast.LENGTH_SHORT).show();
    }

    private void search_telphone_ghabz(String eshterak, String ghabz) {
        //Toast.makeText(this, "123", Toast.LENGTH_SHORT).show();
        jsonTask = new JsonTask();
        String url = "http://app.e-paytoll.ir/api/Telphone/GetBillInfo/" + ghabz + "/" + Functions.u_id;
        ghabz_id = ghabz;
        Lag(url);
//        Toast.makeText(this, url, Toast.LENGTH_SHORT).show();
        jsonTask.execute(url, "search_telphone");


    }
    private void search_driving_ghabz(String eshterak, String ghabz) {
        //Toast.makeText(this, "123", Toast.LENGTH_SHORT).show();
        jsonTask = new JsonTask();
        String url = "http://app.e-paytoll.ir/api/DrivingTicket/GetBillInfo/" + ghabz + "/" + Functions.u_id;
        ghabz_id = ghabz;
        Lag(url);
//        Toast.makeText(this, url, Toast.LENGTH_SHORT).show();
        jsonTask.execute(url, "search_driving");


    }

    private String digiting(String string) {
        String new_str = "";
        int j = 0;
        for (int ii = string.length() - 1; ii >= 0; ii--) {
            j++;
            if (j != string.length() && j % 3 == 0)
                new_str = "," + string.charAt(ii) + new_str;
            else
                new_str = string.charAt(ii) + new_str;
        }
        return new_str;
    }


    public void clk_detail(View view) {
        if(!ghabz_type.equals("driving")) {
            fun.enableDisableView(lay_main, false);
            RelativeLayout lay_message = findViewById(R.id.lay_message);
            ConstraintLayout lay_more_detail = findViewById(R.id.lay_more_detail);
            lay_message.setVisibility(View.VISIBLE);
            lay_more_detail.setVisibility(View.VISIBLE);

            allowBack = false;
        }
        else
        {
            fun.enableDisableView(lay_main, false);
            RelativeLayout lay_message = findViewById(R.id.lay_message);
            ConstraintLayout lay_detail = findViewById(R.id.lay_detail);
            lay_message.setVisibility(View.VISIBLE);
            lay_detail.setVisibility(View.VISIBLE);

            allowBack = false;

        }
    }

    private void search_ghabz_eshterak(String eshterak, String ghabz) {
        InputMethodManager imm = (InputMethodManager) getBaseContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        public_eshterak = eshterak;
        String ghabz1_eshterak = "";
        String ghabz2_eshterak = "";
        if (ghabz_type.equals("gas")) {
            ghabz1_eshterak = StaticGasGhabz.ghabz1_eshteraak;
            ghabz2_eshterak = StaticGasGhabz.ghabz2_eshteraak;
        }
        if (ghabz_type.equals("water")) {
            ghabz1_eshterak = StaticWaterGhabz.ghabz1_eshteraak;
            ghabz2_eshterak = StaticWaterGhabz.ghabz2_eshteraak;
        }

        if (eshterak.equals(ghabz1_eshterak) || eshterak.equals(ghabz2_eshterak)) {
            if (ghabz_type.equals("gas")) {
                ghabz_id = StaticGasGhabz.get_ghabz_ID_eshterak(eshterak);
            }
            if (ghabz_type.equals("water")) {
                ghabz_id = StaticWaterGhabz.get_ghabz_ID_eshterak(eshterak);
            }
            String
                    msg = "";
            msg += " نام مالک" + "\n";
            msg += " مبلغ قابل پرداخت قبض" + "\n";
            msg += " مبلغ بدهی قبض" + "\n";
            msg += " کارکرد دوره" + "\n";

            TextView lbl_msg = findViewById(R.id.lbl_msg_right);
            lbl_msg.setText(msg);

            if (ghabz_type.equals("gas")) {
                msg = StaticGasGhabz.get_ghabz_price_eshterak(eshterak);
            }
            if (ghabz_type.equals("water")) {
                msg = StaticWaterGhabz.get_ghabz_price_eshterak(eshterak);
            }
            TextView lbl_msg1 = findViewById(R.id.lbl_msg_left);
            lbl_msg1.setText(msg);

            TextView lbl_msg_right_detail = findViewById(R.id.lbl_msg_right_detail);
            TextView lbl_msg_left_detail = findViewById(R.id.lbl_msg_left_detail);
            if (ghabz_type.equals("gas")) {
                msg = "";
                msg += "تاریخ قرائت پیشین" + "\n";
                msg += "تاریخ قرائت فعلی" + "\n";
                msg += "رقم پیشین شمارشگر" + "\n";
                msg += "رقم فعلی شمارشگر" + "\n";
                msg += "مصرف به متر مکعب" + "\n";
                msg += "بهای گاز مصرفی" + "\n";
                msg += "تعداد واحد" + "\n";

                lbl_msg_right_detail.setText(msg);
                msg = StaticGasGhabz.get_ghabz_detail_eshterak(eshterak);
                lbl_msg_left_detail.setText(msg);
            }
            if (ghabz_type.equals("water")) {
                msg = "";
                msg += "تاریخ قرائت پیشین" + "\n";
                msg += "تاریخ قرائت فعلی" + "\n";
                msg += "رقم پیشین کنتور" + "\n";
                msg += "رقم فعلی کنتور" + "\n";
                msg += "بهای آب مصرفی" + "\n";
                msg += "بهای خدمات فاضلاب" + "\n";
                msg += "مالیات بر ارزش افزوده" + "\n";
                msg += "تکالیف قانون بودجه" + "\n";
                msg += "قابل پرداخت" + "\n";

                lbl_msg_right_detail.setText(msg);
                msg = StaticWaterGhabz.get_ghabz_detail_eshterak(eshterak);
                lbl_msg_left_detail.setText(msg);
            }
            LinearLayout btn_pay = findViewById(R.id.btn_pay);
            btn_pay.setVisibility(View.VISIBLE);
            LinearLayout btn_detail = findViewById(R.id.btn_detail);
            btn_detail.setVisibility(View.VISIBLE);


            //     bookmark_ghabz();

        } else {
            Toast.makeText(this, "اشتراک موردنظر پیدا نشد", Toast.LENGTH_SHORT).show();
        }

    }

    private void search_ghabz_shenase(String shenase, String ghabz) {
        InputMethodManager imm = (InputMethodManager) getBaseContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        public_shenase = shenase;
        String ghabz1_shenase = "";
        String ghabz2_shenase = "";
        if (ghabz_type.equals("gas")) {
            ghabz1_shenase = StaticGasGhabz.ghabz1_shenase;
            ghabz2_shenase = StaticGasGhabz.ghabz2_shenase;
        }
        if (ghabz_type.equals("water")) {
            ghabz1_shenase = StaticWaterGhabz.ghabz1_shenase;
            ghabz2_shenase = StaticWaterGhabz.ghabz2_shenase;
        }

        if (shenase.equals(ghabz1_shenase) || shenase.equals(ghabz2_shenase)) {
            if (ghabz_type.equals("gas")) {
                ghabz_id = StaticGasGhabz.get_ghabz_ID_shenase(shenase);
            }
            if (ghabz_type.equals("water")) {
                ghabz_id = StaticWaterGhabz.get_ghabz_ID_shenase(shenase);
            }
            String
                    msg = "";
            msg += " نام مالک" + "\n";
            msg += " مبلغ قابل پرداخت قبض" + "\n";
            msg += " مبلغ بدهی قبض" + "\n";
            msg += " کارکرد دوره" + "\n";

            TextView lbl_msg = findViewById(R.id.lbl_msg_right);
            lbl_msg.setText(msg);

            if (ghabz_type.equals("gas")) {
                msg = StaticGasGhabz.get_ghabz_price_shenase(shenase);
            }
            if (ghabz_type.equals("water")) {
                msg = StaticWaterGhabz.get_ghabz_price_shenase(shenase);
            }
            TextView lbl_msg1 = findViewById(R.id.lbl_msg_left);
            lbl_msg1.setText(msg);

            TextView lbl_msg_right_detail = findViewById(R.id.lbl_msg_right_detail);
            TextView lbl_msg_left_detail = findViewById(R.id.lbl_msg_left_detail);
            if (ghabz_type.equals("gas")) {
                msg = "";
                msg += "تاریخ قرائت پیشین" + "\n";
                msg += "تاریخ قرائت فعلی" + "\n";
                msg += "رقم پیشین شمارشگر" + "\n";
                msg += "رقم فعلی شمارشگر" + "\n";
                msg += "مصرف به متر مکعب" + "\n";
                msg += "بهای گاز مصرفی" + "\n";
                msg += "تعداد واحد" + "\n";

                lbl_msg_right_detail.setText(msg);
                msg = StaticGasGhabz.get_ghabz_detail_shenase(shenase);
                lbl_msg_left_detail.setText(msg);
            }
            if (ghabz_type.equals("water")) {
                msg = "";
                msg += "تاریخ قرائت پیشین" + "\n";
                msg += "تاریخ قرائت فعلی" + "\n";
                msg += "رقم پیشین کنتور" + "\n";
                msg += "رقم فعلی کنتور" + "\n";
                msg += "بهای آب مصرفی" + "\n";
                msg += "بهای خدمات فاضلاب" + "\n";
                msg += "مالیات بر ارزش افزوده" + "\n";
                msg += "تکالیف قانون بودجه" + "\n";
                msg += "قابل پرداخت" + "\n";

                lbl_msg_right_detail.setText(msg);
                msg = StaticGasGhabz.get_ghabz_detail_shenase(shenase);
                lbl_msg_left_detail.setText(msg);
            }
            LinearLayout btn_pay = findViewById(R.id.btn_pay);
            btn_pay.setVisibility(View.VISIBLE);
            LinearLayout btn_detail = findViewById(R.id.btn_detail);
            btn_detail.setVisibility(View.VISIBLE);


//            bookmark_ghabz();

        } else {
            Toast.makeText(this, "اشتراک موردنظر پیدا نشد", Toast.LENGTH_SHORT).show();
        }

    }

    private void bookmark_ghabzNew(String Id, String type) {


        final String id = Id;
        final String type1 = type;
        Lag("Bookmark Type= "+type);

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(GhabzSearch.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(GhabzSearch.this);
        }
        builder.setTitle("ذخیره اشتراک؟")
                .setMessage("آیا می خواهید این اشتراک به لیست اشتراک های من اضافه شود؟")
                .setPositiveButton("بله", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        if(type1.equals("electric"))
                        {
                            jsonTask = new JsonTask();
                            String url = "http://app.e-paytoll.ir/api/Electric/SaveUserElectricBill/" + id + "/" + Functions.u_id;
                            jsonTask.execute(url, "bookmark_electric");
                        }
                        if(type1.equals("water"))
                        {
                            jsonTask = new JsonTask();
                            String url = "http://app.e-paytoll.ir/api/Water/SaveUserWaterBill/" + id + "/" + Functions.u_id;
                            jsonTask.execute(url, "bookmark_water");
                        }
                        if(type1.equals("telphone"))
                        {
                            jsonTask = new JsonTask();
                            String url = "http://app.e-paytoll.ir/api/Telphone/SaveUserTelphoneBill/" + id + "/" + Functions.u_id;
                            jsonTask.execute(url, "bookmark_telphone");
                        }
                        if(type1.equals("driving"))
                        {
                            jsonTask = new JsonTask();
                            String url = "http://app.e-paytoll.ir/api/DrivingTicket/SaveUserDrivingTicketBill/" + id + "/" + Functions.u_id;
                            jsonTask.execute(url, "bookmark_driving");
                        }
                        if(type1.equals("gas"))
                        {
                            jsonTask = new JsonTask();
                            String url = "http://app.e-paytoll.ir/api/Gas/SaveUserGasBill/" + id + "/" + Functions.u_id;
                            Lag(url);
                            jsonTask.execute(url, "bookmark_gas");
                        }




                    }
                }).setNegativeButton("نه", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // continue with delete

            }
        }).setIcon(android.R.drawable.ic_dialog_alert)
                .show();


    }

    private void bookmark_ghabz(String Id, String type) {


        final String id = Id;
        final String type1 = type;

        Cursor cr = myDB.rawQuery("select * from MyGhabz where AboneID='" + Id + "' and Type='" + type + "'", null);
        if (cr.getCount() == 0) {

            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(GhabzSearch.this, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(GhabzSearch.this);
            }
            builder.setTitle("ذخیره اشتراک؟")
                    .setMessage("آیا می خواهید این اشتراک به لیست اشتراک های من اضافه شود؟")
                    .setPositiveButton("بله", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            myDB.execSQL("insert into MyGhabz (AboneID,Type) values('" + id + "','" + type1 + "')");
                        }
                    }).setNegativeButton("نه", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // continue with delete

                }
            }).setIcon(android.R.drawable.ic_dialog_alert)
                    .show();


        }


//       // Toast.makeText(this, "123", Toast.LENGTH_SHORT).show();
//        SharedPreferences prefs = this.getSharedPreferences("ghabz", Context.MODE_PRIVATE);
//        String ghabz1 = prefs.getString("ghabz_id1", null);
//        String ghabz2 = prefs.getString("ghabz_id2", null);
//        String ghabz3 = prefs.getString("ghabz_id3", null);
//        String ghabz4 = prefs.getString("ghabz_id4", null);
//        //Toast.makeText(this, "123", Toast.LENGTH_SHORT).show();
//        if(ghabz_id.equals(StaticGasGhabz.ghabz1_ID))
//        {
//            if(ghabz1 ==null )
//            {
//                AlertDialog.Builder builder;
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    builder = new AlertDialog.Builder(GhabzSearch.this, android.R.style.Theme_Material_Dialog_Alert);
//                } else {
//                    builder = new AlertDialog.Builder(GhabzSearch.this);
//                }
//                builder.setTitle("ذخیره اشتراک؟")
//                        .setMessage("آیا می خواهید این اشتراک به لیست اشتراک های من اضافه شود؟")
//                        .setPositiveButton("بله", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                SharedPreferences.Editor editor = getSharedPreferences("ghabz", MODE_PRIVATE).edit();
//                                editor.putString("ghabz_id1", ghabz_id);
//                                editor.apply();
//
//                            }
//                        }).setNegativeButton("نه", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        // continue with delete
//
//                    }
//                })
//                        .setIcon(android.R.drawable.ic_dialog_alert)
//                        .show();
//            }
//        }
//        if(ghabz_id.equals(StaticGasGhabz.ghabz2_ID))
//        {
//            if(ghabz2 == null)
//            {
//                AlertDialog.Builder builder;
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    builder = new AlertDialog.Builder(GhabzSearch.this, android.R.style.Theme_Material_Dialog_Alert);
//                } else {
//                    builder = new AlertDialog.Builder(GhabzSearch.this);
//                }
//                builder.setTitle("ذخیره اشتراک؟")
//                        .setMessage("آیا می خواهید این اشتراک به لیست اشتراک های من اضافه شود؟")
//                        .setPositiveButton("بله", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                SharedPreferences.Editor editor = getSharedPreferences("ghabz", MODE_PRIVATE).edit();
//                                editor.putString("ghabz_id2", ghabz_id);
//                                editor.apply();
//
//                            }
//                        }).setNegativeButton("نه", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        // continue with delete
//
//                    }
//                })
//                        .setIcon(android.R.drawable.ic_dialog_alert)
//                        .show();
//            }
//        }
//       // Toast.makeText(this, ghabz_id+"--"+StaticWaterGhabz.ghabz1_ID, Toast.LENGTH_SHORT).show();
//        if(ghabz_id.equals(StaticWaterGhabz.ghabz1_ID))
//        {
//
//            if(ghabz3 == null)
//            {
//                AlertDialog.Builder builder;
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    builder = new AlertDialog.Builder(GhabzSearch.this, android.R.style.Theme_Material_Dialog_Alert);
//                } else {
//                    builder = new AlertDialog.Builder(GhabzSearch.this);
//                }
//                builder.setTitle("ذخیره اشتراک؟")
//                        .setMessage("آیا می خواهید این اشتراک به لیست اشتراک های من اضافه شود؟")
//                        .setPositiveButton("بله", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                SharedPreferences.Editor editor = getSharedPreferences("ghabz", MODE_PRIVATE).edit();
//                                editor.putString("ghabz_id3", ghabz_id);
//                                editor.apply();
//
//                            }
//                        }).setNegativeButton("نه", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        // continue with delete
//
//                    }
//                })
//                        .setIcon(android.R.drawable.ic_dialog_alert)
//                        .show();
//            }
//        }
//
//        if(ghabz_id.equals(StaticWaterGhabz.ghabz2_ID))
//        {
//            if(ghabz4 == null)
//            {
//                AlertDialog.Builder builder;
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    builder = new AlertDialog.Builder(GhabzSearch.this, android.R.style.Theme_Material_Dialog_Alert);
//                } else {
//                    builder = new AlertDialog.Builder(GhabzSearch.this);
//                }
//                builder.setTitle("ذخیره اشتراک؟")
//                        .setMessage("آیا می خواهید این اشتراک به لیست اشتراک های من اضافه شود؟")
//                        .setPositiveButton("بله", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                SharedPreferences.Editor editor = getSharedPreferences("ghabz", MODE_PRIVATE).edit();
//                                editor.putString("ghabz_id4", ghabz_id);
//                                editor.apply();
//
//                            }
//                        }).setNegativeButton("نه", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        // continue with delete
//
//                    }
//                })
//                        .setIcon(android.R.drawable.ic_dialog_alert)
//                        .show();
//            }
//        }
//
//
//
//


    }

    public void clk_message(View view) {
        fun.enableDisableView(lay_main, true);
        RelativeLayout lay_message = findViewById(R.id.lay_message);
        lay_message.setVisibility(View.GONE);
        ConstraintLayout lay_more_detail = findViewById(R.id.lay_more_detail);
        lay_more_detail.setVisibility(View.GONE);
        ConstraintLayout lay_gate = findViewById(R.id.lay_gate);
        lay_gate.setVisibility(View.GONE);

    }

    public void clk_back(View view) {
        finish();
    }

    public void clk_pay(View view) {
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(PaymentUrl));
        startActivity(i);
//        webview.loadUrl("http://e-paytoll.ir/Pages/Common/mobilepayment.aspx?Amount=" + price + "&AdditionalInfo=10000089-CTSCar&MerchantID=118088384&TerminalId=17995091&TransactionKey=AZ24JJ95SS&OrderId=10000089235123552");

    }

    public void clk_back_complete(View view) {
        fun.enableDisableView(lay_main, true);
        RelativeLayout lay_message = findViewById(R.id.lay_message);
        LinearLayout lay_more_detail = findViewById(R.id.lay_more_detail);
        lay_message.setVisibility(View.GONE);
        lay_more_detail.setVisibility(View.GONE);
        //   Toast.makeText(this, "123", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if (!allowBack) {


        } else {
            super.onBackPressed();
        }
    }

    public void clk_back_more_info(View view) {
        // Toast.makeText(this, "321", Toast.LENGTH_SHORT).show();
        fun.enableDisableView(lay_main, true);
        RelativeLayout lay_message = findViewById(R.id.lay_message);
        ConstraintLayout lay_more_detail = findViewById(R.id.lay_more_detail);
        ConstraintLayout lay_gate = findViewById(R.id.lay_gate);
        lay_gate.setVisibility(View.GONE);

        lay_message.setVisibility(View.GONE);
        lay_more_detail.setVisibility(View.GONE);
        allowBack = true;
    }

    public void clk_back_gate(View view) {
        fun.enableDisableView(lay_main, true);
        RelativeLayout lay_message = findViewById(R.id.lay_message);
        ConstraintLayout lay_gate = findViewById(R.id.lay_gate);
        lay_gate.setVisibility(View.GONE);
        ConstraintLayout lay_more_detail = findViewById(R.id.lay_more_detail);
        lay_more_detail.setVisibility(View.GONE);
        lay_message.setVisibility(View.GONE);

        allowBack = true;
    }

    public void clk_back_Detail(View view) {

        fun.enableDisableView(lay_main, true);
        RelativeLayout lay_message = findViewById(R.id.lay_message);
        LinearLayout lay_detail = findViewById(R.id.lay_detail);
        lay_message.setVisibility(View.GONE);
        lay_detail.setVisibility(View.GONE);

    }

    public void clk_back_detail(View view) {
        fun.enableDisableView(lay_main, true);
        RelativeLayout lay_message = findViewById(R.id.lay_message);
        ConstraintLayout lay_detail = findViewById(R.id.lay_detail);
        lay_message.setVisibility(View.GONE);
        lay_detail.setVisibility(View.GONE);
    }


    public class myWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onLoadResource(WebView view, String url) {


            TextView txt = findViewById(R.id.txt_url);
            txt.setText(view.getUrl());
        }
    }

    private void HandleResultElectric(String jsonResult) {
        LinearLayout btn_pay = findViewById(R.id.btn_pay);
        btn_pay.setVisibility(View.GONE);

        LinearLayout btn_detail = findViewById(R.id.btn_detail);
        btn_detail.setVisibility(View.GONE);
        Gson gson = new Gson();
        try {
            ElectricBill electricBill = gson.fromJson(jsonResult, ElectricBill.class);
            TextView lbl_msg = findViewById(R.id.lbl_msg_right);
            TextView lbl_msg_left = findViewById(R.id.lbl_msg_left);
            lbl_msg_left.setVisibility(View.GONE);
            String msg = "نام مشترک : " + electricBill.FullName + "\n";
            msg += "آدرس : " + electricBill.Address + "\n";
            lbl_msg.setText(msg);


            TextView lbl_msg_right_detail = findViewById(R.id.lbl_msg_right_detail);
            TextView lbl_msg_left_detail = findViewById(R.id.lbl_msg_left_detail);

            TextView lbl_price = findViewById(R.id.lbl_price);
            lbl_price.setVisibility(View.VISIBLE);
            lbl_price.setText("قابل پرداخت : " + Functions.Cur(electricBill.Amount));


            msg = "";
            msg += "تاریخ قرائت پیشین" + "\n";
            msg += "تاریخ قرائت فعلی" + "\n";
            msg += "شناسه قبض" + "\n";
            msg += "شناسه پرداخت" + "\n";
            msg += "مهلت پرداخت" + "\n";


            lbl_msg_right_detail.setText(msg);
            msg = "";
            msg += electricBill.PreviousDate + "\n";
            msg += electricBill.CurrentDate + "\n";
            msg += electricBill.BillID + "\n";
            msg += electricBill.PaymentID + "\n";
            msg += electricBill.PaymentDate + "\n";


            lbl_msg_left_detail.setText(msg);
            if (Integer.valueOf(electricBill.Amount) > 0) {
                btn_pay.setVisibility(View.VISIBLE);
                PaymentUrl = electricBill.PaymentUrl;
            }
            else
            {

            }
            btn_detail.setVisibility(View.VISIBLE);
            if (!electricBill.IsBookMarked) {
                bookmark_ghabzNew(electricBill.BillID, "electric");
            }


        } catch (Exception e1) {
            TextView lbl_msg = findViewById(R.id.lbl_msg_right);
            TextView lbl_msg_left = findViewById(R.id.lbl_msg_left);
            lbl_msg_left.setVisibility(View.GONE);
            lbl_msg.setText("خطا در دریافت اطلاعات لطفا شناسه قبض را بررسی کنید");
        }


//
//


    }
    private void HandleResultTelphone(String jsonResult) {
        LinearLayout btn_pay = findViewById(R.id.btn_pay);
        btn_pay.setVisibility(View.GONE);

        LinearLayout btn_detail = findViewById(R.id.btn_detail);
        btn_detail.setVisibility(View.GONE);
        Gson gson = new Gson();
        try {
            TelphoneBill telphoneBill = gson.fromJson(jsonResult, TelphoneBill.class);
            TextView lbl_msg = findViewById(R.id.lbl_msg_right);
            TextView lbl_msg_left = findViewById(R.id.lbl_msg_left);
            lbl_msg_left.setVisibility(View.GONE);
            String msg = "شناسه پرداخت : " + telphoneBill.FinalPaymnetId + "\n";
            msg += "شناسه قبض : " + telphoneBill.FinalBillId + "\n";
            lbl_msg.setText(msg);


            TextView lbl_msg_right_detail = findViewById(R.id.lbl_msg_right_detail);
            TextView lbl_msg_left_detail = findViewById(R.id.lbl_msg_left_detail);

            TextView lbl_price = findViewById(R.id.lbl_price);
            lbl_price.setVisibility(View.VISIBLE);
            lbl_price.setText("قابل پرداخت : " + Functions.Cur(telphoneBill.FinalAmount));


            if (Integer.valueOf(telphoneBill.FinalAmount) > 0) {
                PaymentUrl = telphoneBill.PaymentUrl;
                btn_pay.setVisibility(View.VISIBLE);
            }
            //btn_detail.setVisibility(View.VISIBLE);
            if (!telphoneBill.IsBookMarked) {
                bookmark_ghabzNew(ghabz_id, "telphone");
            }


        } catch (Exception e1) {
            TextView lbl_msg = findViewById(R.id.lbl_msg_right);
            TextView lbl_msg_left = findViewById(R.id.lbl_msg_left);
            lbl_msg_left.setVisibility(View.GONE);
            lbl_msg.setText("خطا در دریافت اطلاعات لطفا شناسه قبض را بررسی کنید");
        }


//
//


    }
    private void HandleResultWater(String jsonResult) {
        LinearLayout btn_pay = findViewById(R.id.btn_pay);
        btn_pay.setVisibility(View.GONE);

        LinearLayout btn_detail = findViewById(R.id.btn_detail);
        btn_detail.setVisibility(View.GONE);
        Gson gson = new Gson();
        try {
            ElectricBill electricBill = gson.fromJson(jsonResult, ElectricBill.class);
            TextView lbl_msg = findViewById(R.id.lbl_msg_right);
            TextView lbl_msg_left = findViewById(R.id.lbl_msg_left);
            lbl_msg_left.setVisibility(View.GONE);
            String msg = "نام مشترک : " + electricBill.FullName + "\n";
            msg += "آدرس : " + electricBill.Address + "\n";
            lbl_msg.setText(msg);


            TextView lbl_msg_right_detail = findViewById(R.id.lbl_msg_right_detail);
            TextView lbl_msg_left_detail = findViewById(R.id.lbl_msg_left_detail);

            TextView lbl_price = findViewById(R.id.lbl_price);
            lbl_price.setVisibility(View.VISIBLE);
            lbl_price.setText("قابل پرداخت : " + Functions.Cur(electricBill.Amount));


            msg = "";
            msg += "تاریخ قرائت پیشین" + "\n";
            msg += "تاریخ قرائت فعلی" + "\n";
            msg += "شناسه قبض" + "\n";
            msg += "شناسه پرداخت" + "\n";
            msg += "مهلت پرداخت" + "\n";


            lbl_msg_right_detail.setText(msg);
            msg = "";
            msg += electricBill.PreviousDate + "\n";
            msg += electricBill.CurrentDate + "\n";
            msg += electricBill.BillID + "\n";
            msg += electricBill.PaymentID + "\n";
            msg += electricBill.PaymentDate + "\n";


            lbl_msg_left_detail.setText(msg);
            if (Integer.valueOf(electricBill.Amount) > 0) {
                PaymentUrl = electricBill.PaymentUrl;
                btn_pay.setVisibility(View.VISIBLE);
            }
            btn_detail.setVisibility(View.VISIBLE);
            if (!electricBill.IsBookMarked) {
                bookmark_ghabzNew(electricBill.BillID, "water");
            }


        } catch (Exception e1) {
            TextView lbl_msg = findViewById(R.id.lbl_msg_right);
            TextView lbl_msg_left = findViewById(R.id.lbl_msg_left);
            lbl_msg_left.setVisibility(View.GONE);
            lbl_msg.setText("خطا در دریافت اطلاعات لطفا شناسه قبض را بررسی کنید");
        }


//
//


    }
   private void HandleResultDriving(String jsonResult) {
        LinearLayout btn_pay = findViewById(R.id.btn_pay);
        btn_pay.setVisibility(View.GONE);

        LinearLayout btn_detail = findViewById(R.id.btn_detail);
        btn_detail.setVisibility(View.GONE);
        Gson gson = new Gson();
        try {
            DrivingBill[] electricBill = gson.fromJson(jsonResult, DrivingBill[].class);
            TextView lbl_msg = findViewById(R.id.lbl_msg_right);
            TextView lbl_msg_left = findViewById(R.id.lbl_msg_left);
            lbl_msg_left.setVisibility(View.GONE);
            if(electricBill.length>0) {
                String msg = "شماره پلاک : " + electricBill[0].PlateNumber + "\n";
                msg += "جمع مبلغ : " + Functions.Cur(electricBill[0].TotalAmount) + "\n";
                lbl_msg.setText(msg);
                ghabz_type="driving";

                for(int i=0;i<electricBill.length;i++)
                {
                    itemDriving.add(new ItemDriving(electricBill[i].City,electricBill[i].Amount,electricBill[i].BillId,electricBill[i].PaymentId,electricBill[i].Location,electricBill[i].Type,electricBill[i].DateTime,electricBill[i].Delivery,electricBill[i].PaymentUrl));

                }
                mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler);
                mRecyclerView.setHasFixedSize(true);
                mLayoutManager = new LinearLayoutManager(GhabzSearch.this);
                mRecyclerView.setLayoutManager(mLayoutManager);
                mAdapter = new adapter_driving(GhabzSearch.this, itemDriving);
                mRecyclerView.setAdapter(mAdapter);
//            if (Integer.valueOf(electricBill.Amount) > 0) {
//                btn_pay.setVisibility(View.VISIBLE);
//            }
                btn_detail.setVisibility(View.VISIBLE);
                if (!electricBill[0].IsBookMarked) {
                    bookmark_ghabzNew(ghabz_id, "driving");
                }
            }

        } catch (Exception e1) {
            TextView lbl_msg = findViewById(R.id.lbl_msg_right);
            TextView lbl_msg_left = findViewById(R.id.lbl_msg_left);
            lbl_msg_left.setVisibility(View.GONE);
            lbl_msg.setText("خطا در دریافت اطلاعات لطفا شناسه قبض را بررسی کنید");
        }


//
//


    }

    private void HandleResultGas(String jsonResult) {
        LinearLayout btn_pay = findViewById(R.id.btn_pay);
        btn_pay.setVisibility(View.GONE);

        LinearLayout btn_detail = findViewById(R.id.btn_detail);
        btn_detail.setVisibility(View.GONE);
        Gson gson = new Gson();
        try {


            ElectricBill electricBill = gson.fromJson(jsonResult, ElectricBill.class);
            TextView lbl_msg = findViewById(R.id.lbl_msg_right);
            TextView lbl_msg_left = findViewById(R.id.lbl_msg_left);
            lbl_msg_left.setVisibility(View.GONE);
            String msg = "نام مشترک : " + electricBill.FullName + "\n";
            msg += "آدرس : " + electricBill.Address + "\n";
            lbl_msg.setText(msg);


            TextView lbl_msg_right_detail = findViewById(R.id.lbl_msg_right_detail);
            TextView lbl_msg_left_detail = findViewById(R.id.lbl_msg_left_detail);

            TextView lbl_price = findViewById(R.id.lbl_price);
            lbl_price.setVisibility(View.VISIBLE);
            lbl_price.setText("قابل پرداخت : " + Functions.Cur(electricBill.Amount));


            msg = "";
            msg += "تاریخ قرائت پیشین" + "\n";
            msg += "تاریخ قرائت فعلی" + "\n";
            msg += "شناسه قبض" + "\n";
            msg += "شناسه پرداخت" + "\n";
            msg += "مهلت پرداخت" + "\n";


            lbl_msg_right_detail.setText(msg);
            msg = "";
            msg += electricBill.PreviousDate + "\n";
            msg += electricBill.CurrentDate + "\n";
            msg += electricBill.BillID + "\n";
            msg += electricBill.PaymentID + "\n";
            msg += electricBill.PaymentDate + "\n";


            lbl_msg_left_detail.setText(msg);
            if (Integer.valueOf(electricBill.Amount) > 0) {
                PaymentUrl = electricBill.PaymentUrl;
                btn_pay.setVisibility(View.VISIBLE);
            }
            btn_detail.setVisibility(View.VISIBLE);


            EditText txt_ghabz = findViewById(R.id.txt_ghabz);
            if (!electricBill.IsBookMarked) {
                bookmark_ghabzNew(txt_ghabz.getText().toString(), "gas");
            }


        } catch (Exception e1) {
            TextView lbl_msg = findViewById(R.id.lbl_msg_right);
            TextView lbl_msg_left = findViewById(R.id.lbl_msg_left);
            lbl_msg_left.setVisibility(View.GONE);
            lbl_msg.setText("خطا در دریافت اطلاعات لطفا شناسه قبض را بررسی کنید");
        }


//
//


    }


    public class Timer extends Thread {

        int oneSecond = 1000;
        int value = 0;
        String TAG = "Timer";
        String typ = "";
        public long milles = 1000;


        //@Override
        public Timer(String type) {
            typ = type;
        }

        @Override
        public void run() {

            for (; ; ) {


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        if (typ.equals("timeout")) {
                            if (is_requested) {
                                tim++;
                                if (tim > Functions.Time_out_limit) {
                                    is_requested = false;
                                    fun.enableDisableView(lay_main, true);
                                    RelativeLayout lay_message = findViewById(R.id.lay_message);
                                    lay_message.setVisibility(View.GONE);
                                    LinearLayout lay_wait = findViewById(R.id.lay_wait);
                                    lay_wait.setVisibility(View.GONE);
                                    tim = 1;
                                    // Log.d("majid",String.valueOf(tim));
                                    Toast.makeText(GhabzSearch.this, "خطای شبکه رخ داد", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        if (typ.equals("break")) {

                        }
                    }
                });


                //   Log.d("majid", String.valueOf(value));
                //Thread.currentThread();
                try {


                    Thread.sleep(milles);
                    //	Log.d(TAG, " " + value);
                } catch (InterruptedException e) {
                    System.out.println("timer interrupted");
                    //value=0;
                    e.printStackTrace();
                }
            }
        }


    }

    private class MyAsyncTask extends AsyncTask<String, Integer, Double> {


        public String ss = "", url = "";


        @Override
        protected Double doInBackground(String... params) {
            // TODO Auto-generated method stub

            //  dd=params[0];
            try {
                postData();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Double result) {

            int
                    start = ss.indexOf("<output>");
            int
                    end = ss.indexOf("</output>");
            String
                    output_str = "";
            String
                    param_str = "";

            Lag(ss);


            int
                    start1 = ss.indexOf("<param>");
            int
                    end1 = ss.indexOf("</param>");
            // Toast.makeText(MyCarList.this, ss, Toast.LENGTH_SHORT).show();
            if (end1 > 0) {
                param_str = ss.substring(start1 + 7, end1);
                // Toast.makeText(DrawerTest.this, param_str, Toast.LENGTH_SHORT).show();
                if (param_str.equals("get_pay_info")) {

                    start1 = ss.indexOf("<result>");

                }


            }
        }


        protected void onProgressUpdate(Integer... progress) {
            //pb.setProgress(progress[0]);
        }

        public void postData() throws IOException {
            HttpClient httpclient = new DefaultHttpClient(); // Create HTTP Client
            HttpGet httpget = new HttpGet(url); // Set the action you want to do
            HttpResponse response = httpclient.execute(httpget); // Executeit
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent(); // Create an InputStream with the response
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf8"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) // Read line by line
                sb.append(line);

            String resString = sb.toString(); // Result is here
            ss = resString;
            //Log.d("majid", resString);
            is.close();
        }
    }

    private class JsonTask extends AsyncTask<String, String, String> {


        String result1;
        String type = "";

        protected void onPreExecute() {
            super.onPreExecute();


        }

        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                type = params[1];
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");


                }

                return buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


            JsonResult = result;
            if (type.equals("search_electric")) {
                HandleResultElectric(JsonResult);
            }
            if (type.equals("search_telphone")) {
                HandleResultTelphone(JsonResult);
            }
            if (type.equals("search_water")) {
                HandleResultWater(JsonResult);
            }
            if (type.equals("search_driving")) {
                HandleResultDriving(JsonResult);
            }
            if (type.equals("search_gas")) {
                HandleResultGas(JsonResult);
            }
            if (type.equals("bookmark_electric") || type.equals("bookmark_water")) {

                    Toast.makeText(GhabzSearch.this, "اشتراک با موفقیت ذخیره شد", Toast.LENGTH_SHORT).show();
            }
            if (type.equals("bookmark_gas")) {

                    Toast.makeText(GhabzSearch.this, "اشتراک با موفقیت ذخیره شد", Toast.LENGTH_SHORT).show();
            }
            if (type.equals("bookmark_driving")) {

                    Toast.makeText(GhabzSearch.this, "بارکد با موفقیت ذخیره شد", Toast.LENGTH_SHORT).show();
            }
            if (type.equals("bookmark_telphone")) {

                    Toast.makeText(GhabzSearch.this, "شماره نلفن با موفقیت ذخیره شد", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
