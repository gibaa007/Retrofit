package com.g7.retrofit;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private TextView mResponseTv;
    private APIService mAPIService;
    private String TAG = "Retrofit";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText titleEt = (EditText) findViewById(R.id.et_title);
        final EditText bodyEt = (EditText) findViewById(R.id.et_body);
        Button btn_submit_post = (Button) findViewById(R.id.btn_submit_post);
        Button btn_submit_get = (Button) findViewById(R.id.btn_submit_get);
        Button btn_submit_multi = (Button) findViewById(R.id.btn_submit_multi);
        mResponseTv = (TextView) findViewById(R.id.tv_response);

        mAPIService = ApiUtils.getAPIService();

        btn_submit_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titleEt.getText().toString().trim();
                String body = bodyEt.getText().toString().trim();
                if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(body)) {
                    sendPost(title, body);
                }
            }
        });
        btn_submit_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendGet();
            }
        });
        btn_submit_multi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMultiPart();
            }
        });
    }

    public void sendPost(String title, String body) {
        // RxJava
        mAPIService.login(title, body).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CommonModal<UserPojo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.getMessage());
                    }

                    @Override
                    public void onNext(CommonModal<UserPojo> post) {
                        if (post.isStatus())
                            showResponse(post.getBody().toString());
                        else Toast.makeText(MainActivity.this, "Failure", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void sendGet() {
        APIService gitHubService = APIService.retrofit.create(APIService.class);
        final Call<List<Contributor>> call = gitHubService.repoContributors("square", "retrofit");
        new NetworkCall().execute(call);
    }

    public void sendMultiPart() {
        APIService gitHubService = APIService.retrofit.create(APIService.class);


        // create RequestBody instance from file
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("RestImage.png").getFile());

        RequestBody reqFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), reqFile);

        // add book id part within the multipart request
        RequestBody id = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(1l));

        final Call<Contributor> call = gitHubService.addBookCover(id, body);
        new NetworkCall().execute(call);
    }


    private class NetworkCall extends AsyncTask<Call, Void, String> {

        @Override
        protected String doInBackground(Call[] calls) {
            try {
                Call<List<Contributor>> call = calls[0];
                Response<List<Contributor>> response = call.execute();
                return response.body().toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            mResponseTv.setText(result);
        }
    }


    public void showResponse(String response) {
        if (mResponseTv.getVisibility() == View.GONE) {
            mResponseTv.setVisibility(View.VISIBLE);
        }
        mResponseTv.setText(response);
    }
}
