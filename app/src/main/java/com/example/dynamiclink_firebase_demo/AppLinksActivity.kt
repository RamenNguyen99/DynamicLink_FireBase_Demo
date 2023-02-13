package com.example.dynamiclink_firebase_demo

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.dynamiclink_firebase_demo.databinding.ActivityAppLinksBinding
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks

class AppLinksActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAppLinksBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppLinksBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
