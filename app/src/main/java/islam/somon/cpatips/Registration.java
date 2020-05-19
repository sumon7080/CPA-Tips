package islam.somon.cpatips;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class Registration extends AppCompatActivity {
    private CountryCodePicker ccp;
    private EditText phoneText;
    private EditText codeText;
    private Button continueAndNextBtn;
    private String checker = "", phoneNumber = "";
    private RelativeLayout relativeLayout;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth mAuth;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();
        loadingBar = new ProgressDialog(this);


        phoneText = findViewById(R.id.phoneText);
        codeText = findViewById(R.id.codeText);
        continueAndNextBtn = findViewById(R.id.continueNextButton);
        relativeLayout = findViewById(R.id.phoneAuth);
        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        ccp.registerCarrierNumberEditText(phoneText);


        continueAndNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(continueAndNextBtn.getText().equals("Submit") || checker.equals("Code Sent"))
                {
                    String verificationCode = codeText.getText().toString();
                    if(verificationCode.equals(""))
                    {
                        Toast.makeText(Registration.this, "Please input your verfication number here", Toast.LENGTH_SHORT).show();


                    }
                    else
                    {
                        loadingBar.setTitle("Mobile Number Verification");
                        loadingBar.setMessage("Please Wait While your phone number is verifying");
                        loadingBar.setCanceledOnTouchOutside(false);
                        loadingBar.show();

                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, verificationCode);
                        signInWithPhoneAuthCredential(credential);

                    }


                }
                else
                {

                    phoneNumber = ccp.getFullNumberWithPlus();
                    if(!phoneNumber.equals(""))
                    {
                        loadingBar.setTitle("Phone Number Verification");
                        loadingBar.setMessage("Please Waite, While we are verifying your phone number");
                        loadingBar.setCanceledOnTouchOutside(false);
                        loadingBar.show();

                        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber, 60, TimeUnit.SECONDS, Registration.this, mCallbacks);




                    }

                    else
                    {
                        Toast.makeText(Registration.this, "Please Input Correct Number", Toast.LENGTH_SHORT).show();


                    }


                }

            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks()
        {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential)
            {
                signInWithPhoneAuthCredential(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e)
            {
                loadingBar.dismiss();
                Toast.makeText(Registration.this, "Invalid Phone Number", Toast.LENGTH_SHORT).show();
                relativeLayout.setVisibility(View.VISIBLE);

                continueAndNextBtn.setText("Continue");
                codeText.setVisibility(View.GONE);


            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken)
            {
                super.onCodeSent(s, forceResendingToken);

                mVerificationId = s;
                mResendToken = forceResendingToken;

                relativeLayout.setVisibility(View.GONE);
                checker = "Code Sent";
                continueAndNextBtn.setText("Submit");
                codeText.setVisibility(View.VISIBLE);
                loadingBar.dismiss();

                Toast.makeText(Registration.this, "Please Check your Code", Toast.LENGTH_SHORT).show();

            }
        };
    }

    @Override
    protected void onStart()
    {

        super.onStart();

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(firebaseUser != null)
        {
            Intent homeIntent = new Intent(Registration.this, MainActivity.class);
            startActivity(homeIntent);
            finish();

        }

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            loadingBar.dismiss();
                            Toast.makeText(Registration.this, "You are Logged in Successfully", Toast.LENGTH_SHORT).show();
                            sendUserToMainActivity();

                        }
                        else
                        {
                            loadingBar.dismiss();
                            String e = task.getException().toString();
                            Toast.makeText(Registration.this, "Error: " + e, Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private void sendUserToMainActivity()
    {
        Intent intent = new Intent(Registration.this, MainActivity.class);
        startActivity(intent);
        finish();



    }
}
