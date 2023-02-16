package com.example.dynamiclink_firebase_demo

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.isVisible
import com.example.dynamiclink_firebase_demo.databinding.ActivityDetailPostsBinding
import com.example.dynamiclink_firebase_demo.model.Posts
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.squareup.picasso.Picasso


class DetailPostsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailPostsBinding
    private var item: Posts? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPostsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
        onClickListener()
    }

    private fun initData() {
        item = intent.getSerializableExtra(MainActivity.KEY_POSTS) as? Posts
        binding.run {
            tvTitlePosts.text = item?.title
            tvDescriptionPosts.text = item?.description
            item?.let {
                Picasso.get().load(it.image).into(imgPosts)
            }
        }
    }

    private fun onClickListener() {
        binding.run {
            imgMenu.setOnClickListener {
                llDropdownMenu.isVisible = !llDropdownMenu.isVisible
            }
            tvCopyLink.setOnClickListener {
                llDropdownMenu.isVisible = false
                generateSharingLink(deepLink = "${Constants.PREFIX}/post/${item?.id}".toUri()) {
                    val clipboard: ClipboardManager =
                        getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText("Copy Link", it)
                    clipboard.setPrimaryClip(clip)
                    Toast.makeText(applicationContext, "Copy successful!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            tvShareLink.setOnClickListener {
                llDropdownMenu.isVisible = false
                generateSharingLink(deepLink = "${Constants.PREFIX}/post/${item?.id}".toUri()) {
                    shareDeepLink(it)
                }
            }
            imgBack.setOnClickListener {
                finish()
            }
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
            val parametersAndroid =
                DynamicLink.AndroidParameters.Builder(BuildConfig.APPLICATION_ID)

            val socialMetaTagParameters = DynamicLink.SocialMetaTagParameters.Builder()
            socialMetaTagParameters.title = item?.title.toString()
            socialMetaTagParameters.description = item?.description.toString()
            socialMetaTagParameters.imageUrl = Uri.parse(item?.image)
            setSocialMetaTagParameters(socialMetaTagParameters.build())
//            parametersAndroid.fallbackUrl = Uri.parse("https://www.facebook.com")
            setAndroidParameters(parametersAndroid.build())
            setIosParameters(DynamicLink.IosParameters.Builder("www.example.ios").build())
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
            }
        }
    }

    private fun shareDeepLink(deepLink: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, "Firebase Deep Link")
        intent.putExtra(Intent.EXTRA_TEXT, deepLink)

        startActivity(intent)
    }
}
