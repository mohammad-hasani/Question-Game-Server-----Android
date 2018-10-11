package hajagha.dibagames.ir.hajagha;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Pars on 5/14/2016.
 */
public class App extends Application {
    private static Context _context;
    public static String DOTE_CHARACTER = "~DOTE~";
    public static String READ_DOTE_CHARACTER = "\\.";
    public static String xmlSettingName = "setting";
    public static String url = "http://151.80.214.76:12321/";
    public static String urlsignin = "login?";
    public static String urlquestions = "mainquestion?";
    public static String urlquestion = "question?";
    public static String urlsetanswer = "setanswer?";
    public static String spliter1 = "~~~";
    public static String spliter2 = ";;;";
    public static String[] keysLogin = {
            "username",
            "password",
            "type"
    };
    public static String[] keysGetQuestions = {
            "index"
    };
    public static String[] keysGetOnQuestion = {
            "id"
    };
    public static String[] keysSetAnswer = {
            "questionid",
            "answer"
    };

    public App() {
        _context = this;
    }

    public static void setSetting(Context context, String xml, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(xml, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(key, value);
        edit.commit();
    }

    public static String getSetting(Context context, String xml, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(xml, Context.MODE_PRIVATE);
        String data = sharedPreferences.getString(key, null);
        return data;
    }

    public static Context getContext() {
        return _context;
    }
    public static void setContext(Context context){
        _context = context;
    }
}
