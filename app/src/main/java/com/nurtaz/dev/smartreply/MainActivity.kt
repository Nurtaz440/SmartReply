package com.nurtaz.dev.smartreply

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.mlkit.nl.smartreply.SmartReply
import com.google.mlkit.nl.smartreply.SmartReplyGenerator
import com.google.mlkit.nl.smartreply.SmartReplySuggestion
import com.google.mlkit.nl.smartreply.SmartReplySuggestionResult
import com.google.mlkit.nl.smartreply.TextMessage
import com.nurtaz.dev.smartreply.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    val binding get() = _binding!!

    lateinit var conversation : ArrayList<TextMessage>
    lateinit var smartReplyGenerator: SmartReplyGenerator
    var userUID = "123456"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        conversation = ArrayList()
        smartReplyGenerator = SmartReply.getClient()

        binding.btnSend.setOnClickListener {
            val  message = binding.evSend.text.toString().trim()
            conversation.add(TextMessage.createForRemoteUser(message,System.currentTimeMillis(),userUID))
            smartReplyGenerator.suggestReplies(conversation)
                .addOnSuccessListener {
                    if (it.status == SmartReplySuggestionResult.STATUS_NOT_SUPPORTED_LANGUAGE){
                        binding.tvResult.text = "STATUS_NOT_SUPPORTED_LANGUAGE"
                    }else if (it.status == SmartReplySuggestionResult.STATUS_SUCCESS){
                        var reply = ""
                        for (suggestion:SmartReplySuggestion in it.suggestions){
                            reply = reply + "-" + suggestion.text + "\n"
                        }
                        binding.tvResult.text = (reply)
                    }
                }.addOnFailureListener {
                    binding.tvResult.text = "Error ${it.message}"
                }
        }

    }
}