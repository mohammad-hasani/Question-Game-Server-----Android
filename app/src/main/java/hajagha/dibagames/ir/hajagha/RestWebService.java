package hajagha.dibagames.ir.hajagha;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;

import Interfaces.OnDataReceive;

/**
 * Created by Pars on 5/14/2016.
 */
public class RestWebService {

    private ProgressDialog progressDialog = null;
    public RestWebService(final Context context, final OnDataReceive receive, String[] keys, String[] values, String url) {
        params = new ArrayList<NameValuePair>();
        headers = new ArrayList<NameValuePair>();
        final RestWebService restWebService = new RestWebService(App.url + url);
        for (int i = 0; i < keys.length; i++) {
            restWebService.AddParam(keys[i], values[i]);
        }
        final Handler handler = new Handler(context.getMainLooper());
        restWebService.AddHeader("GData-Version", "2");
        new Thread(new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog = new ProgressDialog(context);
                        progressDialog.setMessage("لطفا صبر کنید");
                        progressDialog.show();
                    }
                });
                try {
                    restWebService.Execute(RequestMethod.POST);
                } catch (Exception e) {
                    e.printStackTrace();
                    e.getMessage();
                }
                String response = restWebService.getResponse();
                if (response == null)
                    response = "false";
                final String info = response;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(progressDialog.isShowing())
                        {
                            progressDialog.dismiss();
                        }
                        receive.Received(info);
                    }
                });
            }
        }).start();


    }

    private ArrayList<NameValuePair> params;
    private ArrayList<NameValuePair> headers;

    private String url;

    private int responseCode;
    private String message;

    private String response;

    enum RequestMethod {
        GET,
        POST
    }

    public String getResponse() {
        return response;
    }

    public String getErrorMessage() {
        return message;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public RestWebService(String url) {
        this.url = url;
        params = new ArrayList<NameValuePair>();
        headers = new ArrayList<NameValuePair>();
    }

    public void AddParam(String name, String value) {
        params.add(new BasicNameValuePair(name, value));
    }

    public void AddHeader(String name, String value) {
        headers.add(new BasicNameValuePair(name, value));
    }

    public void Execute(RequestMethod method) throws Exception {
        switch (method) {
//            case GET:
//            {
//                //add parameters
//                String combinedParams = "";
//                if(!params.isEmpty()){
//                    combinedParams += "?";
//                    for(NameValuePair p : params)
//                    {
//                        String paramString = p.getName() + "=" + URLEncoder.encode(p.getValue(),”UTF-8″);
//                        if(combinedParams.length() > 1)
//                        {
//                            combinedParams  +=  "&" + paramString;
//                        }
//                        else
//                        {
//                            combinedParams += paramString;
//                        }
//                    }
//                }
//
//                HttpGet request = new HttpGet(url + combinedParams);
//
//                //add headers
//                for(NameValuePair h : headers)
//                {
//                    request.addHeader(h.getName(), h.getValue());
//                }
//
//                executeRequest(request, url);
//                break;
//            }
            case POST: {
                HttpPost request = new HttpPost(url);

                //add headers
                for (NameValuePair h : headers) {
                    request.addHeader(h.getName(), h.getValue());
                }

                if (!params.isEmpty()) {
                    request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                }

                executeRequest(request, url);
                break;
            }
        }
    }

    private void executeRequest(HttpUriRequest request, String url) {
        HttpClient client = new DefaultHttpClient();

        HttpResponse httpResponse;

        try {
            httpResponse = client.execute(request);
            responseCode = httpResponse.getStatusLine().getStatusCode();
            message = httpResponse.getStatusLine().getReasonPhrase();

            HttpEntity entity = httpResponse.getEntity();

            if (entity != null) {

                InputStream instream = entity.getContent();
                response = convertStreamToString(instream);

                // Closing the input stream will trigger connection release
                instream.close();
            }

        } catch (ClientProtocolException e) {
            client.getConnectionManager().shutdown();
            e.printStackTrace();
        } catch (IOException e) {
            client.getConnectionManager().shutdown();
            e.printStackTrace();
        }
    }

    private static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}