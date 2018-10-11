package hajagha.dibagames.ir.hajagha;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import Interfaces.OnDataReceive;

/**
 * Created by Pars on 5/14/2016.
 */
public class Question extends Activity {
    private TextView txtTitle;
    private TextView txtBody;
    private Button btnSend;
    private String strAnswer = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question);
        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);
        ui(id);
        load(id);
    }

    private void load(int id) {
        String[] values = {String.valueOf(id)};
        new RestWebService(this, new OnDataReceive() {
            @Override
            public void Received(String data) {
                String[] info = data.split(App.spliter1);
                txtTitle.setText(info[1].replaceAll(App.DOTE_CHARACTER, App.READ_DOTE_CHARACTER));
                txtBody.setText(info[2].replaceAll(App.DOTE_CHARACTER, App.READ_DOTE_CHARACTER));
                strAnswer = info[3].replaceAll(App.DOTE_CHARACTER, App.READ_DOTE_CHARACTER);
            }
        }, App.keysGetOnQuestion, values, App.urlquestion);
    }

    private void ui(final int id) {
        txtTitle = (TextView) findViewById(R.id.txttitle);
        txtBody = (TextView) findViewById(R.id.txtbody);
        btnSend = (Button) findViewById(R.id.btnanswer);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(Question.this);
                LayoutInflater layoutInflater = LayoutInflater.from(Question.this);
                View view = layoutInflater.inflate(R.layout.questionalert, null);
                final EditText edtAnswer = (EditText) view.findViewById(R.id.edtanswer);
                edtAnswer.setText(strAnswer);
                alert.setView(view);
                alert.setPositiveButton(getResources().getString(R.string.send), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String values[] = {String.valueOf(id), edtAnswer.getText().toString().replaceAll(App.READ_DOTE_CHARACTER, App.DOTE_CHARACTER)};
                        new RestWebService(Question.this, new OnDataReceive() {
                            @Override
                            public void Received(String data) {
                                if (data != null)
                                    if (data.startsWith("true")) {
                                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.accepttheanswer), Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.failinsenddata), Toast.LENGTH_SHORT).show();
                                    }
                            }
                        }, App.keysSetAnswer, values, App.urlsetanswer);
                    }
                });
                alert.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alert.show();
            }
        });
    }
}
