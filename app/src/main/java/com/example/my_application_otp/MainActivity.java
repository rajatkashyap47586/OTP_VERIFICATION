package com.example.my_application_otp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private Spinner spinner;
    private EditText number;
    private EditText edt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edt=(EditText)findViewById(R.id.number);
        edt.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View v) {
                edt.setText("");
            }
        });

        spinner=findViewById(R.id.country_code);
        spinner.setAdapter(new ArrayAdapter<String>(
                this,android.R.layout.simple_spinner_dropdown_item,Codes.countrynames));

        number=findViewById(R.id.number);

        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v)
            {
                String A_code=Codes.countryAreaCodes[spinner.getSelectedItemPosition()];
                String num=number.getText().toString().trim();
                if(num.isEmpty() || num.length()<10)
                {
                    number.setError("Valid Number is required");
                    number.requestFocus();
                    return;
                }
                String pn="+"+A_code+num;
                Intent intent=new Intent(MainActivity.this,VerifyPhoneActivity.class);
                intent.putExtra("pn",pn);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onStart()
    {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            Intent intent=new Intent(this,MyProfileActivity.class);
            //it closes all activity upto
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}
