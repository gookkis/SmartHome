package com.gookkis.sh;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.rey.material.widget.FloatingActionButton;
import com.rey.material.widget.ProgressView;
import com.rey.material.widget.Switch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import co.mobiwise.fastgcm.GCMListener;
import co.mobiwise.fastgcm.GCMManager;

public class MainActivity extends AppCompatActivity implements GCMListener {

    private String TAG = "SmartHome";
    private String apiKey = "API KEY GEEKNESIA";
    private AQuery aq;
    private Switch switchBiru, switchKuning, switchPutih, switchMotion, switchGas;
    Toolbar toolbar;
    private ProgressView pv;
    private RelativeLayout rlRoot;
    private TextView tvSuhu, tvGasInfo;
    private FloatingActionButton btnReload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GCMManager.getInstance(this).registerListener(this);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        aq = new AQuery(this);
        pv = (ProgressView) findViewById(R.id.progressBar);
        rlRoot = (RelativeLayout) findViewById(R.id.rl_root);
        ambilData();

    }

    public void displayUI(String suhu, String boolBiru, String boolKuning, String boolPutih, String boolMotion, String boolGas, String gasInfo) {
        pv.setVisibility(View.INVISIBLE);
        rlRoot.setVisibility(View.VISIBLE);

        switchBiru = (Switch) findViewById(R.id.switch_biru);
        switchKuning = (Switch) findViewById(R.id.switch_kuning);
        switchPutih = (Switch) findViewById(R.id.switch_putih);

        switchMotion = (Switch) findViewById(R.id.switch_motion);
        switchGas = (Switch) findViewById(R.id.switch_gas);

        tvSuhu = (TextView) findViewById(R.id.text_suhu);
        tvSuhu.setText("Suhu : " + suhu);

        tvGasInfo = (TextView) findViewById(R.id.text_gas_info);
        tvGasInfo.setText("Gas Status : " + gasInfo);

        btnReload = (FloatingActionButton) findViewById(R.id.fabReload);

        btnReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ambilData();
            }
        });

        if (boolBiru.equals("On")) {
            switchBiru.setChecked(true);
        } else {
            switchBiru.setChecked(false);
        }

        if (boolKuning.equals("On")) {
            switchKuning.setChecked(true);
        } else {
            switchKuning.setChecked(false);
        }

        if (boolPutih.equals("On")) {
            switchPutih.setChecked(true);
        } else {
            switchPutih.setChecked(false);
        }

        if (boolMotion.equals("On")) {
            switchMotion.setChecked(true);
        } else {
            switchMotion.setChecked(false);
        }

        if (boolGas.equals("On")) {
            switchGas.setChecked(true);
        } else {
            switchGas.setChecked(false);
        }

        switchBiru.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(Switch view, boolean checked) {
                if (checked) {
                    controlLampu("onBiru");
                } else {
                    controlLampu("offBiru");
                }
            }

        });

        switchKuning.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(Switch view, boolean checked) {
                if (checked) {
                    controlLampu("onKuning");
                } else {
                    controlLampu("offKuning");
                }
            }

        });

        switchPutih.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(Switch view, boolean checked) {
                if (checked) {
                    controlLampu("onPutih");
                } else {
                    controlLampu("offPutih");
                }
            }

        });

        switchMotion.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(Switch view, boolean checked) {
                if (checked) {
                    controlLampu("onAlarmMotion");
                } else {
                    controlLampu("offAlarmMotion");
                }
            }

        });

        switchGas.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(Switch view, boolean checked) {
                if (checked) {
                    controlLampu("onAlarmGas");
                } else {
                    controlLampu("offAlarmGas");
                }
            }

        });

    }

    public void controlLampu(String action) {
        String url = "http://api.geeknesia.com/api/control/" + action + "?api_key=" + apiKey;
        Map<String, Object> params = new HashMap<String, Object>();
        aq.ajax(url, params, String.class, new AjaxCallback<String>() {
            @Override
            public void callback(String url, String json, AjaxStatus status) {
                if (json.equals("Success")) {
                    Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void ambilData() {
        rlRoot.setVisibility(View.INVISIBLE);
        pv.setVisibility(View.VISIBLE);
        String url = "http://api.geeknesia.com/api/attributes?api_key=" + apiKey;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("limit", 1);
        aq.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                if (json != null) {
                    //Log.d("SmartHome", json.toString());

                    try {
                        JSONObject output = json.getJSONObject("output");
                        JSONArray data = output.getJSONArray("data");
                        JSONObject data0 = data.getJSONObject(0);
                        JSONArray attr = data0.getJSONArray("attributes");

                        JSONObject suhu = attr.getJSONObject(0);
                        JSONObject biru = attr.getJSONObject(1);
                        JSONObject kuning = attr.getJSONObject(2);
                        JSONObject putih = attr.getJSONObject(3);
                        JSONObject motion = attr.getJSONObject(4);
                        JSONObject gas = attr.getJSONObject(5);
                        JSONObject gasInfo = attr.getJSONObject(6);
                        displayUI(suhu.getString("value"), biru.getString("value"), kuning.getString("value"),
                                putih.getString("value"), motion.getString("value"), gas.getString("value"), gasInfo.getString("value"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else {
                    Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDeviceRegisted(String s) {
        Log.d(TAG, "onDeviceRegisted: " + s);
        GCMManager.getInstance(this).subscribeTopic("aqa");
    }

    @Override
    public void onMessage(String s, Bundle data) {
        Log.d(TAG, "onMessage: " + s);

        //Intent intent = new Intent(getApplicationContext(), WarningActivity.class);
//        final Dialog dialog = new Dialog(getApplicationContext());
//        dialog.setContentView(R.layout.activity_warning);
//
//        // set the custom dialog components
//        TextView text = (TextView) dialog.findViewById(R.id.text_title);
//        text.setText(data.get("title").toString());
//        TextView warn = (TextView) dialog.findViewById(R.id.text_warning);
//        warn.setText(data.get("message").toString());
//
//        android.support.design.widget.FloatingActionButton dialogButton =
//                (android.support.design.widget.FloatingActionButton) dialog.findViewById(R.id.fabSoundOff);
//
//        // if button is clicked, close the custom dialog
//        dialogButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//
//        dialog.show();
    }

    @Override
    public void onPlayServiceError() {
        Log.d(TAG, "onPlayServiceError: Play Service Error");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GCMManager.getInstance(this).unRegisterListener();
    }
}
