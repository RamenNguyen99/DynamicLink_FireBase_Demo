package com.example.dynamiclink_firebase_demo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.dynamiclink_firebase_demo.databinding.ActivityMainBinding
import com.example.dynamiclink_firebase_demo.model.Posts
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks

class MainActivity : AppCompatActivity() {

    companion object {
        internal const val KEY_POSTS = "KEY_POST"
    }

    private lateinit var binding: ActivityMainBinding

    private val items = listOf(
        Posts(
            1,
            "Dog",
            "The dog (Canis familiaris[4][5] or Canis lupus familiaris[5]) is a domesticated descendant of the wolf. Also called the domestic dog, it is derived from the extinct Pleistocene wolf,[6][7] and the modern wolf is the dog's nearest living relative.[8] Dogs were the first species to be domesticated[9][8] by hunter-gatherers over 15,000 years ago[7] before the development of agriculture.",
            "https://paradepets.com/.image/c_limit%2Ccs_srgb%2Cq_auto:good%2Cw_1254/MTkxMzY1Nzg4NjczMzIwNTQ2/cutest-dog-breeds-jpg.webp"
        ),
        Posts(
            2,
            "Cat",
            "The cat (Felis catus) is a domestic species of small carnivorous mammal.[1][2] It is the only domesticated species in the family Felidae and is commonly referred to as the domestic cat or house cat to distinguish it from the wild members of the family.[4] Cats are commonly kept as house pets but can also be farm cats or feral cats; the feral cat ranges freely and avoids human contact.[5] Domestic cats are valued by humans for companionship and their ability to kill rodents. About 60 cat breeds are recognized by various cat registries.",
            "https://i.guim.co.uk/img/media/26392d05302e02f7bf4eb143bb84c8097d09144b/446_167_3683_2210/master/3683.jpg?width=620&quality=45&dpr=2&s=none"
        ),
        Posts(
            3,
            "Tiger",
            "The tiger (Panthera tigris) is the largest living cat species and a member of the genus Panthera. It is most recognisable for its dark vertical stripes on orange fur with a white underside. An apex predator, it primarily preys on ungulates, such as deer and wild boar. It is territorial and generally a solitary but social predator, requiring large contiguous areas of habitat to support its requirements for prey and rearing of its offspring.",
            "https://files.worldwildlife.org/wwfcmsprod/images/Tiger_resting_Bandhavgarh_National_Park_India/hero_full/77ic6i4qdj_Medium_WW226365.jpg"
        ),
        Posts(
            4,
            "Lion",
            "The lion (Panthera leo) is a large cat of the genus Panthera native to Africa and India. It has a muscular, broad-chested body; short, rounded head; round ears; and a hairy tuft at the end of its tail. It is sexually dimorphic; adult male lions are larger than females and have a prominent mane. It is a social species, forming groups called prides. A lion's pride consists of a few adult males, related females, and cubs. Groups of female lions usually hunt together, preying mostly on large ungulates.",
            "https://cdn.britannica.com/55/2155-050-604F5A4A/lion.jpg"
        ),
        Posts(
            5,
            "Panda",
            "The giant panda (Ailuropoda melanoleuca, sometimes panda bear or simply panda), is a bear species endemic to China.[4] It is characterised by its bold black-and-white coat and rotund body. The name \"giant panda\" is sometimes used to distinguish it from the red panda, a neighboring musteloid. Though it belongs to the order Carnivora, the giant panda is a folivore, with bamboo shoots and leaves making up more than 99% of its diet.",
            "https://www.treehugger.com/thmb/3PeKibW4s2NYkIUnpyvS7bsyHFw=/750x0/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/giant-panda-96cd8616b8304fbba5b9bbe59545ecf7.jpg"
        ),
        Posts(
            6,
            "Monkey",
            "Monkey is a common name that may refer to most mammals of the infraorder Simiiformes, also known as the simians. Traditionally, all animals in the group now known as simians are counted as monkeys except the apes, which constitutes an incomplete paraphyletic grouping; however, in the broader sense based on cladistics, apes (Hominoidea) are also included, making the terms monkeys and simians synonyms in regards to their scope.",
            "https://dichthuatlightway.com/wp-content/uploads/2021/09/monkey-around.webp"
        ),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecyclerView()
        FirebaseDynamicLinks.getInstance().getDynamicLink(intent)
            .addOnSuccessListener(this) { pendingDynamicLinkData ->
                var deepLink: Uri? = null
                if (pendingDynamicLinkData != null) {
                    deepLink = pendingDynamicLinkData.link
                }
                deepLink?.let { uri ->
                    val path = uri.toString().substring(deepLink.toString().lastIndexOf("/") + 1)
                    when {
                        uri.toString().contains("post") -> {
                            val postId = path.toInt()
                            startActivity(Intent(this, DetailPostsActivity::class.java).apply {
                                putExtra(KEY_POSTS, items.find { item -> item.id == postId })
                            })
                        }
                    }
                }
            }.addOnCanceledListener(this) {
                Log.d("MainActivity", "getDynamicLinks:")
            }
    }


    private fun initRecyclerView() {
        binding.rvPosts.run {
            adapter = PostAdapter(items) { id ->
                startActivity(Intent(this@MainActivity, DetailPostsActivity::class.java).apply {
                    putExtra(KEY_POSTS, items.find { item -> item.id == id })
                })
            }
        }
    }
}
