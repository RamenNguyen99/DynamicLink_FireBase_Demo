package com.example.dynamiclink_firebase_demo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.example.dynamiclink_firebase_demo.databinding.ActivityMainBinding
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btn.setOnClickListener {
            startActivity(Intent(this, AppLinksActivity::class.java))
        }
        binding.btnShare.setOnClickListener {
            generateSharingLink(
                deepLink = "${Constants.PREFIX}/test".toUri()
            ) { generatedLink ->
                // Use this generated Link to share via Intent
            }
        }
        FirebaseDynamicLinks.getInstance().getDynamicLink(intent)
            .addOnSuccessListener(this) { pendingDynamicLinkData ->
                var deepLink: Uri? = null
                if (pendingDynamicLinkData != null) {
                    deepLink = pendingDynamicLinkData.link
                }
                deepLink?.let { uri ->
                    when {
                        uri.toString().contains("test") -> {
                            startActivity(Intent(this, AppLinksActivity::class.java))
                        }
                    }
                }
            }.addOnCanceledListener(this) {
                Log.d("MainActivity", "getDynamicLinks:")
            }
    }

    private fun generateSharingLink(
        deepLink: Uri,
        getShareableLink: (String) -> Unit = {},
    ) {
        FirebaseDynamicLinks.getInstance().createDynamicLink().run {
            // What is this link parameter? You will get to know when we will actually use this function.
            link = deepLink
            // [domainUriPrefix] will be the domain name you added when setting up Dynamic Links at Firebase Console.
            // You can find it in the Dynamic Links dashboard.
            domainUriPrefix = Constants.PREFIX

            setAndroidParameters(DynamicLink.AndroidParameters.Builder().build())
            setIosParameters(DynamicLink.IosParameters.Builder("com.example.ios").build())
            buildShortDynamicLink()
        }.also {
            it.addOnSuccessListener { dynamicLink ->
                // This lambda will be triggered when short link generation is successful
                Log.d("xxxxx", "onCreate: ${dynamicLink.shortLink.toString()}")
                // Retrieve the newly created dynamic link so that we can use it further for sharing via Intent.
                getShareableLink.invoke(dynamicLink.shortLink.toString())
            }
            it.addOnFailureListener { ex ->
                // This lambda will be triggered when short link generation failed due to an exception
                Log.d("xxxxx", "onCreate: ${ex.message}")
                // Handle
            }
        }
    }
}
