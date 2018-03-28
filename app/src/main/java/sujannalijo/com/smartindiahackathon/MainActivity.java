package sujannalijo.com.smartindiahackathon;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    //prodevelopers
    Button login;
    EditText email,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login = (Button)findViewById(R.id.login);
        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().toString().equals("mastersujitamin@gmail.com") &&
                        password.getText().toString().equals("9987892291")) {
                    Toast.makeText(getApplicationContext(), "success",Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(getApplicationContext(), "Wrong Credentials",Toast.LENGTH_SHORT).show();
                   /* AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                           finish();
                        }
                    });
                    AlertDialog alertDialog;
                    alertDialog = builder.create();
                    alertDialog.show();
*/                }
            }
        });

    }
}
