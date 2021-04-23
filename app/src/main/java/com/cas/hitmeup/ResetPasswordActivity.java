package com.cas.hitmeup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    TextView tvResetPassword;
    EditText etEmail;
    Button btnResetPassword, btnClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        tvResetPassword = findViewById(R.id.tvResetPassword);
        etEmail = findViewById(R.id.etEmail);
        btnResetPassword = findViewById(R.id.btnResetPassword);
        btnClose = findViewById(R.id.btnClose);
    }

    public void btnResetPasswordClick(View v) {
        String email = etEmail.getText().toString().trim();

        if(email.equals("")) {
            etEmail.setError(getString(R.string.resetpw_enter_email));
        }
        else {
            FirebaseAuth fbAuth = FirebaseAuth.getInstance();

            fbAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()) {
                        Toast.makeText(ResetPasswordActivity.this, R.string.reset_password_instructions, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            });
        }
    }

    public void btnCloseClick(View v) {
        finish();
    }
}