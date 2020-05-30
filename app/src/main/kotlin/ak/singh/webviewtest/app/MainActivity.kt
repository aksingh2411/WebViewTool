package ak.singh.webviewtest.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ak.singh.webviewtest.R
import ak.singh.webviewtest.config.platform.ConfigPresenter
import ak.singh.webviewtest.qr.ui.URL_EXTRA

internal class MainActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)

        if (intent != null && intent.hasExtra(URL_EXTRA)){
            intent?.getStringExtra(URL_EXTRA)?.let {
                ViewModelProvider(this).get(ConfigPresenter::class.java).setCurrentUrl(it)
            }
        }
    }
}
