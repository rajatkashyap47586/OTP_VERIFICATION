package com.example.my_application_otp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.SECONDS;

public class VerifyPhoneActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String id;
    private ProgressBar p_bar;
    private EditText otp1,otp2,otp3,otp4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);

        mAuth=FirebaseAuth.getInstance();
        p_bar=findViewById(R.id.bar);
        otp1=findViewById(R.id.otp1);
        otp2=findViewById(R.id.otp2);
        otp3=findViewById(R.id.otp3);
        otp4=findViewById(R.id.otp4);
        String pn=getIntent().getStringExtra("pn");
        sendVerificationCode(pn);
        findViewById(R.id.verify).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                String otp=otp1.getText().toString().trim()
                        +otp2.getText().toString().trim()
                        +otp3.getText().toString().trim()
                        +otp4.getText().toString().trim();
                if(otp.isEmpty() || otp.length()<4) //invalid otp
                {
                    Toast.makeText(VerifyPhoneActivity.this,"Not valid",Toast.LENGTH_LONG).show();
                }

                verifyCode(otp);
            }
        });

    }

    //it work when auto detected fails
    private void verifyCode(String code)
    {
        PhoneAuthCredential pac = PhoneAuthProvider.getCredential(id,code);
        signInWithCredential(pac);
    }

    private void signInWithCredential(PhoneAuthCredential pac) {
        mAuth.signInWithCredential(pac).addOnCompleteListener(new OnCompleteListener<AuthResult>(){
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent intent=new Intent(VerifyPhoneActivity.this,MyProfileActivity.class);
                    //it closes all activity upto
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(VerifyPhoneActivity.this
                            ,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }


            }
        }
        );
    }

    private void sendVerificationCode(String number) {
        p_bar.setVisibility(View.VISIBLE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack=new PhoneAuthProvider.OnVerificationStateChangedCallbacks()
    {
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            id=s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential pac)
        {
            String my_code=pac.getSmsCode();
            if(my_code !=null)
            {

                verifyCode(my_code);

            }
        }

        @Override
        public void onVerificationFailed(FirebaseException exc)
        {
            Toast.makeText(VerifyPhoneActivity.this,exc.getMessage(),Toast.LENGTH_LONG).show();
        }



    };
}
