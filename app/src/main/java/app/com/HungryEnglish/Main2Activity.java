package app.com.HungryEnglish;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Main2Activity extends AppCompatActivity {
    private static String file_url = "https://d3fildg3jlcvty.cloudfront.net/0b32c08c472b56d27189347345b320a05bfb25d2/graphics/App-Store-Icon_200x200.png";
    Button btnShowProgress;
    private ProgressDialog pDialog;
    ImageView my_image;
    public static final int progress_bar_type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        btnShowProgress = (Button) findViewById(R.id.btnProgressBar);
        my_image = (ImageView) findViewById(R.id.my_image);
        /**
         * Show Progress bar click event
         * */
        btnShowProgress.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // starting new Async Task
//                createDirectory();
            }
        });


    }


    /**
     * Showing Dialog
     */


}
