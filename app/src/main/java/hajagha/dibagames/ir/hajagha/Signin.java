package hajagha.dibagames.ir.hajagha;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import Interfaces.OnDataReceive;

/**
 * Created by Pars on 5/14/2016.
 */
public class Signin extends Activity {
    private EditText edtUsername;
    private EditText edtPassword;
    private CheckBox chkRememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);
        ui();
    }

    private void ui() {
        edtUsername = (EditText) findViewById(R.id.edtusername);
        edtPassword = (EditText) findViewById(R.id.edtpassword);
        chkRememberMe = (CheckBox) findViewById(R.id.chkrememberme);
        Button btnLogin = (Button) findViewById(R.id.btnlogin);

        validation(edtUsername, getResources().getString(R.string.errorUsername));
        validation(edtPassword, getResources().getString(R.string.errorPassword));

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        load();
    }

    private void validation(final EditText edt, final String error) {
        edt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && edt.getText().toString().length() <= 0) {
                    edt.setError(error);
                }
            }
        });
    }

    private void login() {
        String strUsername = edtUsername.getText().toString().trim();
        String strPassword = edtPassword.getText().toString().trim();
        boolean isChecked = chkRememberMe.isChecked();
        if (isChecked) {
            App.setSetting(this, App.xmlSettingName, "username", strUsername);
            App.setSetting(this, App.xmlSettingName, "password", strPassword);
        }
        String[] values = {strUsername, strPassword, "a"};
        new RestWebService(this, new OnDataReceive() {
            @Override
            public void Received(String data) {
                if (!data.equals("false") || data != null) {
                    String msg = getResources().getString(R.string.welcome);
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    Intent i = new Intent(Signin.this, MainQuestion.class);
                    startActivity(i);
                } else {
                    String msg = getResources().getString(R.string.failinlogin);
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                }
            }
        }, App.keysLogin, values, App.urlsignin);
    }

    private void load() {
        String strUsername = App.getSetting(this, App.xmlSettingName, "username");
        String strPassword = App.getSetting(this, App.xmlSettingName, "password");
        if (strUsername == null || strPassword == null) {
            edtUsername.setText("");
            edtPassword.setText("");
            chkRememberMe.setChecked(false);
        } else {
            edtUsername.setText(strUsername);
            edtPassword.setText(strPassword);
            chkRememberMe.setChecked(true);
        }
    }

}
