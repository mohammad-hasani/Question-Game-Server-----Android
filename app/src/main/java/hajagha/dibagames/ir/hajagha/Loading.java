package hajagha.dibagames.ir.hajagha;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Pars on 5/14/2016.
 */
public class Loading extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);
        App.setContext(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Intent i = new Intent(Loading.this, Signin.class);
                    startActivity(i);
                    finish();
                }
            }
        }).start();
    }
}
