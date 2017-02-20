package com.webianks.exp.scout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.ibm.watson.developer_cloud.alchemy.v1.AlchemyLanguage;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AlchemyLanguage service = new AlchemyLanguage();
        service.setApiKey(BuildConfig.API_KEY);
    }
}
