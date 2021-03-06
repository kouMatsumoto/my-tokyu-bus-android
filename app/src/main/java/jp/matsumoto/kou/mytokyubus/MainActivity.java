package jp.matsumoto.kou.mytokyubus;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements DownloadCallback<String> {

    public static final String EXTRA_MESSAGE = "jp.matsumoto.kou.mytokyubus.MESSAGE";

    /** Called when the user taps the Send button */
    public void sendMessage(View view) {
          startDownload();
    }


    // Keep a reference to the NetworkFragment, which owns the AsyncTask object
    // that is used to execute network ops.
    private NetworkFragment mNetworkFragment;

    // Boolean telling us whether a download is in progress, so we don't trigger overlapping
    // downloads with consecutive button clicks.
    private boolean mDownloading = false;


    // Initialize spinner
    private void setSpinner() {
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.planets_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    // manage textView to display result
    private void updateResultText(Boolean visibility, String text) {
        TextView textView = (TextView) findViewById(R.id.text_result);
        textView.setVisibility(visibility ? View.VISIBLE : View.INVISIBLE);
        textView.setText(text);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSpinner();

        mNetworkFragment = NetworkFragment.getInstance(getFragmentManager(), "https://my-tokyu-bus.tokyo/api/?from=2598&to=2336");
    }

    private void startDownload() {
        if (!mDownloading && mNetworkFragment != null) {
            // Execute the async download.
            mNetworkFragment.startDownload();
            mDownloading = true;
        }
    }


    @Override
    public void updateFromDownload(String result) {
        // Update your UI here based on result of download.
        updateResultText(true, "test");
    }

    @Override
    public NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
    }

    @Override
    public void onProgressUpdate(int progressCode, int percentComplete) {
        switch (progressCode) {
            // You can add UI behavior for progress update here.
            case Progress.Error:
                //
                break;
            case Progress.CONNECT_SUCCESS:
                //
                break;
            case Progress.GET_INPUT_STREAM_SUCCESS:
                //
                break;
            case Progress.PROCESS_INPUT_STREAM_IN_PROGRESS:
                //
                break;
            case Progress.PROCESS_INPUT_STREAM_SUCCESS:
                //
                break;
        }
    }

    @Override
    public void finishDownloading() {
        mDownloading = false;
        if (mNetworkFragment != null) {
            mNetworkFragment.cancelDownload();
        }
    }
}
