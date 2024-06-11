package com.naveed.webviewaccessibilitybug

import android.os.Bundle
import android.view.View
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.naveed.webviewaccessibilitybug.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Define local html content
        val htmlContent =
            """<html>
                <h1>This is a webview</h1>
                <body>
                    <p>This is a paragraph inside web view</p>
                    <p>This is another paragraph inside web view</p>
                </body>
               </html>
            """.trimMargin()
        binding.webView.loadData(htmlContent, "text/html", "utf-8")

        // Set delegate on web view
        binding.webView.accessibilityDelegate = CustomFocusAccessibilityDelegate {
            binding.btnFooter.apply {
                sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_FOCUSED)
                requestFocus()
            }
        }

        // Set delegate on title text view
        binding.tvTitle.accessibilityDelegate = CustomFocusAccessibilityDelegate {
            binding.btnFooter.apply {
                sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_FOCUSED)
                requestFocus()
            }
        }
    }
}

/**
 * This accessibility delegate creates a new accessibility action for the footer button.
 * It allows the user to focus on the footer button by performing the action.
 */
class CustomFocusAccessibilityDelegate(
    private val focusFooterButton: () -> Unit
) : View.AccessibilityDelegate() {

    override fun onInitializeAccessibilityNodeInfo(host: View, info: AccessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(host, info)
        val action = AccessibilityNodeInfo.AccessibilityAction(
            R.id.action_focus_footer,
            "Focus on footer button"
        )
        info.addAction(action)
    }

    override fun performAccessibilityAction(host: View, action: Int, args: Bundle?): Boolean {
        if (action == R.id.action_focus_footer) {
            focusFooterButton()
            return true
        }
        return super.performAccessibilityAction(host, action, args)
    }
}