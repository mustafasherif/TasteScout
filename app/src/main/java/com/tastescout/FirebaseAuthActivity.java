package com.tastescout;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tastescout.search.SearchActivity;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FirebaseAuthActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN =1;
    private FirebaseAuth mAuth;
    public static String TAG ="AuthenticationSate";
    @BindView(R.id.email_et)EditText emailEditText;
    @BindView(R.id.password_et)EditText passwordEditText;
    @BindView(R.id.login_bt)Button logInButton;
    @BindView(R.id.register_bt)TextView registerButton;
    @BindView(R.id.progress)ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_auth);
        ButterKnife.bind(this);
        checkEditTextsNotEmpty();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser!=null){
            openHome();
        }
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create();
            }
        });
    }
    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
    private void signIn(String email,String password){
        progressBar.setEnabled(true);
        logInButton.setEnabled(false);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            checkIfEmailVerified();
                        } else {
                            // If sign in fails, display a message to the user.
                            logInButton.setEnabled(true);
                            progressBar.setEnabled(false);
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplication(),R.string.check_email,Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }
    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    void checkEditTextsNotEmpty(){
        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0&&passwordEditText.getText().toString().length()>0) {
                    logInButton.setEnabled(true);
                }else {
                    logInButton.setEnabled(false);
                }

            }

        });

        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0&&emailEditText.getText().toString().length()>0) {
                    logInButton.setEnabled(true);
                }else {
                    logInButton.setEnabled(false);
                }
            }

        });
    }
    private void openHome(){
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
        finish();
    }
    private void create(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser==null){
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.EmailBuilder().build(),
                    new AuthUI.IdpConfig.GoogleBuilder().build());
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build(),
                    RC_SIGN_IN);
        }else {
            openHome();
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);


            if (resultCode == RESULT_OK) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    if(user.isEmailVerified()){
                        openHome();
                    }else {
                        sendVerificationEmail();
                    }
                }
            }
        }
    }
    public void login(View view) {
        if (isOnline()){
            String email=emailEditText.getText().toString();
            String password=passwordEditText.getText().toString();
            if(isEmailValid(email)&&password.length()>5){
                signIn(email,password);
            }else {
                if(!isEmailValid(email)){
                    emailEditText.setError("A valid email is required.");
                }
                if(password.length()<6){
                    passwordEditText.setError("Password must be at least 6 characters.");
                }
            }
        }else{
            Toast.makeText(this,R.string.check_connection,Toast.LENGTH_SHORT).show();
        }
    }
    private void sendVerificationEmail() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                FirebaseAuth.getInstance().signOut();
                                Toast.makeText(getApplication(),R.string.vrify_email,Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                               user.delete();
                               Toast.makeText(getApplication(),R.string.cant_send,Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }
    private void checkIfEmailVerified() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            if (user.isEmailVerified())
            {
                logInButton.setEnabled(true);
                progressBar.setEnabled(false);
                openHome();
            }
            else
            {
                logInButton.setEnabled(true);
                progressBar.setEnabled(false);
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getApplication(),R.string.vrify_email,Toast.LENGTH_LONG).show();
            }
        }
    }
}
