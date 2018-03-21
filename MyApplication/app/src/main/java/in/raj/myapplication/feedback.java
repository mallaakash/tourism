package in.raj.myapplication;
        import android.os.AsyncTask;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.Toast;
        import org.apache.http.HttpEntity;
        import org.apache.http.HttpResponse;
        import org.apache.http.NameValuePair;
        import org.apache.http.client.ClientProtocolException;
        import org.apache.http.client.HttpClient;
        import org.apache.http.client.entity.UrlEncodedFormEntity;
        import org.apache.http.client.methods.HttpPost;
        import org.apache.http.impl.client.DefaultHttpClient;
        import org.apache.http.message.BasicNameValuePair;
        import java.io.IOException;
        import java.util.ArrayList;
        import java.util.List;

public class feedback extends AppCompatActivity {

    String ServerURL = "http://192.168.43.97:8080/imgproc/get_data.php" ;
    EditText name, email, phoneno, feedback ;
    Button button;
    String TempName, TempEmail, TempPhone, TempFeed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);;
        setContentView(R.layout.activity_feedback);

        name = (EditText)findViewById(R.id.Name);
        email = (EditText)findViewById(R.id.email);
        phoneno = (EditText)findViewById(R.id.phoneno);
        feedback = (EditText)findViewById(R.id.feed);
        button = (Button)findViewById(R.id.b5);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GetData();

                InsertData(TempName, TempEmail,TempPhone,TempFeed);

            }
        });
    }

    public void GetData(){

        TempName = name.getText().toString();

        TempEmail = email.getText().toString();

        TempPhone = phoneno.getText().toString();

        TempFeed = feedback.getText().toString();

    }

    public void InsertData(final String name, final String email, final String phoneno, final String feed){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                String NameHolder = name ;
                String EmailHolder = email ;
                String PhoneHolder = phoneno ;
                String FeedHolder = feed ;

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("name", NameHolder));
                nameValuePairs.add(new BasicNameValuePair("email", EmailHolder));
                nameValuePairs.add(new BasicNameValuePair("phoneno", PhoneHolder));
                nameValuePairs.add(new BasicNameValuePair("feed", FeedHolder));


                try {
                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(ServerURL);

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse httpResponse = httpClient.execute(httpPost);

                    HttpEntity httpEntity = httpResponse.getEntity();


                } catch (ClientProtocolException e) {

                } catch (IOException e) {

                }
                return "Data Inserted Successfully";
            }

            @Override
            protected void onPostExecute(String result) {

                super.onPostExecute(result);

                Toast.makeText(feedback.this, "Data Submit Successfully", Toast.LENGTH_LONG).show();

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();

        sendPostReqAsyncTask.execute(name, email);
    }

}