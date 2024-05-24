package com.budget.buddy.presentation.activity

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.CookieManager
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.budget.buddy.R
import com.budget.buddy.presentation.ui.them.Colors

class WebActivity : ComponentActivity() {
    private var shared: SharedPreferences? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            shared = getSharedPreferences("mono", MODE_PRIVATE)
            if (shared?.contains("key_mono") == false) {
                Web()
            } else {
                YouAlreadyLogged()
            }
        }
        onBackPressedDispatcher.addCallback(this) {
            startActivity(Intent(this@WebActivity, MainActivity::class.java))
            finish()
        }
    }

    @Composable
    fun YouAlreadyLogged() {
        Box(
            modifier = androidx.compose.ui.Modifier
                .fillMaxSize()
                .background(
                    Colors.Background
                ),
            contentAlignment = androidx.compose.ui.Alignment.Center,
        ) {
            Column(horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
                Text(
                    text = "You already logged in",
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.open)),
                    fontSize = 24.sp
                )
                Text(text = "\uD83D\uDE0E", fontSize = 60.sp)
                Spacer(modifier = Modifier.size(10.dp))
                Button(
                    onClick = {
                        onBackPressedDispatcher.onBackPressed()
                    },
                    modifier = Modifier
                        .width(200.dp)
                        .padding(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                ) {
                    Text(
                        text = "Back",
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.open))
                    )
                }
            }
        }
    }

    @Composable
    fun Web() {
        AndroidView(factory = { context ->
            WebView(context).apply {
                webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(
                        view: WebView?,
                        request: WebResourceRequest?
                    ): Boolean {
                        val url = request?.url?.toString() ?: return super.shouldOverrideUrlLoading(
                            view,
                            request
                        )
                        if (!url.startsWith("http")) {
                            return try {
                                val intent = Intent(Intent.ACTION_VIEW)
                                intent.data = Uri.parse(url)
                                Log.d("TEST_WEB_UIS", "shouldOverrideUrlLoading: $intent")
                                if (intent.dataString?.contains("market://") == false) {
                                    view?.context?.startActivity(intent)
                                }
                                true
                            } catch (e: ActivityNotFoundException) {
                                view?.let {
                                    Toast.makeText(
                                        it.context,
                                        "${
                                            url.substringBefore("://").uppercase()
                                        } supported applications not found",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                true
                            }
                        }
                        return super.shouldOverrideUrlLoading(view, request)
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        chek(view) // Delay of 1 second
                    }

                    private fun chek(view: WebView?) {
                        view?.postDelayed({
                            view.evaluateJavascript(
                                """
                                                    (function() {
                                                        var element = document.querySelector('.id');
                                                        if (element) {
                                                            console.log("Found element with class 'id':", element);
                                                            return element.getAttribute('id') || element.innerHTML || element.textContent || element.innerText;
                                                        } else {
                                                            console.log("No element found with class 'id'.");
                                                            return null;
                                                        }
                                                    })();
                                                    """
                            ) { value ->
                                value?.let {
                                    Log.d("TEST_WEB", "Element ID: $it")
                                    shared?.edit()?.putString("key_mono", "u_cJh6ZmPkcZonYBaZnz85q-1Cuwrdn5qlYaGUpR7De4")?.apply()
                                    if (it != "null") {
                                    }else{
                                        chek(view)
                                    }
                                }
                            }
                        }, 1000)
                    }
                }
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true

                CookieManager.getInstance().setAcceptThirdPartyCookies(this, true)
                CookieManager.getInstance().setAcceptCookie(true)
                loadUrl("https://api.monobank.ua/")
            }
        })
    }
}

