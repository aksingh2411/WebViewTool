package ak.singh.webviewtest.config.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import ak.singh.webviewtest.R
import ak.singh.webviewtest.config.platform.ConfigPresenter
import ak.singh.webviewtest.logs.platform.LogPresenter
import ak.singh.webviewtest.qr.ui.QrScannerActivity
import ak.singh.webviewtest.tabs.ui.BaseTabView
import ak.singh.webviewtest.webview.platform.WebViewPresenter
import kotlinx.android.synthetic.main.config_view.*

internal class ConfigView : BaseTabView() {

    private lateinit var presenter: ConfigPresenter
    private lateinit var logPresenter: LogPresenter
    private lateinit var webViewPresenter: WebViewPresenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.config_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = init()
        logPresenter = init()
        webViewPresenter = init()

        bindListeners()

        url.setText(presenter.getDefaultUrl(view.context))
        user_agent_input.doOnTextChanged { text, _, _, _ ->
            validateUserAgent(text.toString())
        }
    }

    override fun onResume() {
        super.onResume()

        user_agent_input.setText(presenter.getUserAgent(requireContext()))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.config_view_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    //override fun onOptionsItemSelected(item: MenuItem): Boolean {
     //   if (item.itemId == R.id.config_view_menu_qrcode) {
      //      startActivity(Intent(context, QrScannerActivity::class.java))
       // }
        //return super.onOptionsItemSelected(item)
    //}

    override fun tabTitle(context: Context) = context.getString(R.string.tab_config_title)

    private fun validateUserAgent(value: String){
        if (value.contains("\n")){
            save_user_agent_button.isEnabled = false
            user_agent_input_invalid_message.isVisible = true
        } else {
            save_user_agent_button.isEnabled = true
            user_agent_input_invalid_message.isVisible = false
        }
    }

    private fun bindListeners() {
        context?.let { context ->
            clear_btn.setOnClickListener { logPresenter.clearLogs() }
            ok_btn.setOnClickListener {
                webViewPresenter.setUrl(url.text.toString())

                if (presenter.isRememberUrl(context)) {
                    presenter.rememberUrl(url.text.toString(), context)
                }

                hideKeyboard()
                presenter.runWebViewAction()
            }

            ssl_switch.isChecked = presenter.isTrustAllSsl(context)
            ssl_switch.setOnCheckedChangeListener { _, isChecked ->
                presenter.setTrustAllSsl(isChecked, context)
            }

            url_switch.isChecked = presenter.isRememberUrl(context)
            url_switch.setOnCheckedChangeListener { _, isChecked ->
                presenter.setRememberUrl(isChecked, url.text.toString(), context)
            }

            save_user_agent_button.setOnClickListener {
                hideKeyboard()
                presenter.saveCustomUserAgent(user_agent_input.text.toString(), context)
            }

            reset_user_agent_button.setOnClickListener {
                presenter.resetCustomUserAgent(context)
                user_agent_switch.isChecked = presenter.shouldOverwriteDefaultUserAgent(context)
                user_agent_input.setText(presenter.getUserAgent(context))
            }

            user_agent_switch.isChecked = presenter.shouldOverwriteDefaultUserAgent(context)
            user_agent_switch.setOnCheckedChangeListener { _, isChecked ->
                presenter.overwriteDefaultUserAgent(isChecked, context)
            }
        }
    }
}
