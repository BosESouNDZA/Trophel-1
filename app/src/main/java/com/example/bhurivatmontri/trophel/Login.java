package com.example.bhurivatmontri.trophel;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class Login extends AppCompatActivity {

    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private FirebaseAuth mAuth;
    private static final String TAG = "LoginActivity";
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btnTrophel = (Button) findViewById(R.id.login_btnTrophel);
        final EditText edtEmail =(EditText) findViewById(R.id.login_email);
        final EditText edtPassword = (EditText) findViewById(R.id.login_password);

        btnTrophel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edtEmail.getText().toString() ;
                String password = edtPassword.getText().toString();
                /*if(isValidEmail(email) && isValidPassword(password)){
                    Toast.makeText(Login.this, "Processing : " + email + " / " + password , Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login.this,Home.class);
                    intent.putExtra("email",email);
                    startActivity(intent);
                }else{
                    edtEmail.setError("Your email is invalid type. ");
                }*/

                if(true){
                    if(true){
                        Toast.makeText(Login.this, "Processing : " + email + " / " + password , Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Login.this,Home.class);
                        intent.putExtra("email",email);
                        startActivity(intent);
                    }
                    else{
                        edtPassword.setError("Your password must be at least 8 characters , with at least one upper-case and one number");
                    }
                }
                else{
                    edtEmail.setError("Your email is invalid type.");
                }
            }
        });
        //facebook firebase auth
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_btnFacebook);
        loginButton.setReadPermissions("email","public_profile");
        // Other app specific specialization
        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                Toast.makeText(getApplicationContext(),"Cancel.",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);

                Toast.makeText(getApplicationContext(),"error.",Toast.LENGTH_SHORT).show();
            }
        });
        mAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser User = firebaseAuth.getCurrentUser();
                if(User != null)
                {
                    goHomescreen();
                }
            }
        };
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public final  static boolean isValidPassword(final String password){
        return Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[a-zA-Z0-9]{8,}$").matcher(password).matches();
    }

    public void hideKeyBoard(View view){
        InputMethodManager imm = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(),0);
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        mAuth.addAuthStateListener(firebaseAuthListener);
    }
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            progressBar.setVisibility(View.GONE);
                            loginButton.setVisibility(View.VISIBLE);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_LONG).show();
                        }

                        // ...
                    }
                });
    }
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    protected void onStop(){
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthListener);
    }
    public void goHomescreen(){
        Intent intent = new Intent(this, Home.class);
        intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP | intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
