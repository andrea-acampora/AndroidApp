package com.example.next2me;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import static android.content.ContentValues.TAG;



public class LoginActivity extends AppCompatActivity {


    private final int RC_SIGN_IN = 100;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private TextInputEditText emailTIET,passwordTIET;
    private Button loginButton;
    private ProgressDialog progressDialog;
    private SignInButton googleSignInButton;
    private CallbackManager mCallbackManager;
    private LoginButton facebookLoginButton;
    private FirebaseAuth.AuthStateListener authStateListener;
    private AccessTokenTracker accessTokenTracker;

    private static final String TAG ="FacebookAuthentication";



    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Logging User...");

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

         googleSignInButton = findViewById(R.id.google_sign_in_button);
         emailTIET = findViewById(R.id.email_login);
         passwordTIET = findViewById(R.id.password_login);
         loginButton = findViewById(R.id.button_login);
         facebookLoginButton = findViewById(R.id.facevook_login_button);
         facebookLoginButton.setReadPermissions("email");
         mCallbackManager = CallbackManager.Factory.create();

         facebookLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
             @Override
             public void onSuccess(LoginResult loginResult) {
                loginUserWithFacebook(loginResult.getAccessToken());
             }

             @Override
             public void onCancel() { }

             @Override
             public void onError(FacebookException error) { }
         });

         authStateListener = firebaseAuth -> {
             FirebaseUser user = mAuth.getCurrentUser();
             if(user!=null){
                 updateUI(user);
             }
         };

         accessTokenTracker = new AccessTokenTracker() {
             @Override
             protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                 if(currentAccessToken == null){
                     mAuth.signOut();
                 }
             }
         };

         googleSignInButton.setOnClickListener(v -> googleSignIn());

         loginButton.setOnClickListener(new View.OnClickListener() {

             @Override
             public void onClick(View v) {
                String email = emailTIET.getText().toString();
                String pwd = passwordTIET.getText().toString().trim();
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    emailTIET.setError("Invalid Email");
                    emailTIET.setFocusable(true);
                }else if(pwd.length()<6){
                    passwordTIET.setError("Password length at least 6 characters");
                    passwordTIET.setFocusable(true);
                } else{
                    loginUser(email,pwd);
                }

             }
         });
    }

    private void loginUserWithFacebook(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = mAuth.getCurrentUser();
                    if(user!=null){
                        updateUI(user);
                    }
                }else{
                    Toast.makeText(LoginActivity.this,"Authentication failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loginUser(String email, String pwd) {
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email,pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    progressDialog.dismiss();
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this,"Authentication failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @SuppressWarnings("deprecation")
    private void googleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);

            } catch (ApiException e) {
                updateUI(null);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        updateUI(null);
                        Log.e(TAG, "signInWithCredential:failure", task.getException());
                    }
                });
    }


    private void updateUI(FirebaseUser user) {
        if (user != null) {
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(authStateListener != null){
            mAuth.removeAuthStateListener(authStateListener);
        }
    }
}

