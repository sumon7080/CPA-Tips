package islam.somon.cpatips;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Join extends AppCompatActivity {
    private WebView webView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        webView = findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());

        webView.loadUrl("https://www.facebook.com/groups/356045941755177/about/");

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);


    }


    @Override
    public void onBackPressed()
    {
        if(webView.canGoBack() )
        {
            webView.goBack();

        }
        else
        {
            super.onBackPressed();

        }

    }
}
