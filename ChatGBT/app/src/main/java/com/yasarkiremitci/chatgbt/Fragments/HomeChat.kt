package com.yasarkiremitci.chatgbt.Fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.yasarkiremitci.chatgbt.database.AppDatabase
import com.yasarkiremitci.chatgbt.database.ChatMessage
import com.yasarkiremitci.chatgbt.R
import com.yasarkiremitci.chatgbt.api.Util
import com.yasarkiremitci.chatgbt.adapter.MessageAdapter
import com.yasarkiremitci.chatgbt.api.ApiInterface
import com.yasarkiremitci.chatgbt.api.ApiUtilities
import com.yasarkiremitci.chatgbt.database.ChatMessageDao
import com.yasarkiremitci.chatgbt.databinding.FragmentHomeChatBinding
import com.yasarkiremitci.chatgbt.models.ChatRequest
import com.yasarkiremitci.chatgbt.models.MessageModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.HttpException

class HomeChat : Fragment() {

    private lateinit var binding: FragmentHomeChatBinding
    private lateinit var adapter: MessageAdapter
    private var message = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentHomeChatBinding.inflate(inflater, container, false)

        val messageDao = AppDatabase.getInstance(requireContext()).chatMessageDao()

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext()).apply {
            stackFromEnd = false
        }

        adapter = MessageAdapter(ArrayList<MessageModel>())
        binding.recyclerView.adapter = adapter

        lifecycleScope.launch {

            val chatMessages = messageDao.getAllMessages().map { MessageModel(it.isUser, it.message) }
            if(chatMessages.isNotEmpty()){
                adapter.submitList(ArrayList(chatMessages))


                if (adapter.itemCount > 0) {
                    binding.recyclerView.smoothScrollToPosition(adapter.itemCount - 1)
                }
            }else{

                botWelcomeMessage()
            }





        }

        binding.gonder1.setOnClickListener {
            val sharedPreferences = requireActivity().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)

            val value = sharedPreferences.getString("premium", "false")


            message = binding.editText.text.toString()
            binding.editText.text.clear()
            if (message.isEmpty()) {
                Toast.makeText(requireContext(), "Please ask your question?", Toast.LENGTH_SHORT).show()
            }
            else if (adapter.itemCount >= 11 && value=="false") {
                val action = HomeChatDirections.actionHomeChatToInApp()
                findNavController().navigate(action)
            } else {
                sendMessage(message)
                callApi()
            }
        }


        binding.tekrarbotMessageButton.setOnClickListener {
            val lastMessage = adapter.list.lastOrNull { !it.isUser }
            lastMessage?.let {
                val lastMessageText = it.message
                callApi()
                removeLastMessage()
            }
        }

        binding.editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Gerekli değil
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.isNotBlank() == true) {
                    binding.gonder1.setImageResource(R.drawable.btn_chatsending)
                } else {
                    binding.gonder1.setImageResource(R.drawable.btn_chatsend)
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // Gerekli değil
            }
        })

        binding.settingButton.setOnClickListener {
            val action = HomeChatDirections.actionHomeChatToSettings()
            findNavController().navigate(action)
        }

        return binding.root
    }

    private fun sendMessage(message: String) {
        val userMessage = ChatMessage(isUser = true, message = message)
        val messageDao = AppDatabase.getInstance(requireContext()).chatMessageDao()

        lifecycleScope.launch {
            messageDao.insert(userMessage)

            val chatMessages = messageDao.getAllMessages().map { MessageModel(it.isUser, it.message) }
            adapter.submitList(ArrayList(chatMessages))

            binding.recyclerView.smoothScrollToPosition(adapter.itemCount - 1)
        }
    }

    private fun callApi() {
        val apiInterface: ApiInterface = ApiUtilities.getApiInterface()

        val requestBody = RequestBody.create(
            "application/json".toMediaTypeOrNull(),
            Gson().toJson(
                ChatRequest(
                    model = "gpt-3.5-turbo",
                    messages = listOf(
                        ChatRequest.Message(
                            role = "user",
                            content = message
                        )
                    ),
                    temperature = 0.1,
                    max_tokens = 4000
                )
            )
        )

        val contentType = "application/json"
        val authorization = "Bearer ${Util.API_KEY}"

        val messageDao = AppDatabase.getInstance(requireContext()).chatMessageDao()

        lifecycleScope.launch {
            try {
                val response = apiInterface.getChat(contentType, authorization, requestBody)
                val textResponse = response.choices[0].message.content

                val replacedResponse = textResponse.replace("\n", "")
                val responseMessage = ChatMessage(isUser = false, message = replacedResponse)

                messageDao.insert(responseMessage)

                val chatMessages = messageDao.getAllMessages().map { MessageModel(it.isUser, it.message) }
                adapter.submitList(ArrayList(chatMessages))

                binding.recyclerView.smoothScrollToPosition(adapter.itemCount - 1)
            } catch (e: HttpException) {
                // Handle the HTTP 500 error here
                val response = ChatMessage(isUser = false, message = "server error")
                messageDao.insert(response)

                val chatMessages = messageDao.getAllMessages().map { MessageModel(it.isUser, it.message) }
                adapter.submitList(ArrayList(chatMessages))
                e.printStackTrace()
            } catch (e: Exception) {
                // Handle other exceptions here
                val response = ChatMessage(isUser = false, message = "other error")
                messageDao.insert(response)

                val chatMessages = messageDao.getAllMessages().map { MessageModel(it.isUser, it.message) }
                adapter.submitList(ArrayList(chatMessages))
                e.printStackTrace()
            }
        }
    }

    private fun removeLastMessage() {
        val lastMessage = adapter.list.lastOrNull { message -> !message.isUser }
        if (lastMessage != null) {
            val lastMessageIndex = adapter.list.lastIndexOf(lastMessage)
            if (lastMessageIndex != -1) {
                adapter.list.removeAt(lastMessageIndex)
                adapter.notifyItemRemoved(lastMessageIndex)
            }
        }
    }


    fun botWelcomeMessage() {
        val messageDao = AppDatabase.getInstance(requireContext()).chatMessageDao()
        viewLifecycleOwner.lifecycleScope.launch() {
            delay(100)
            messageDao.insert(ChatMessage(isUser = false, message = "Hello! How can i help you?"))

            val chatMessages = messageDao.getAllMessages().map { MessageModel(it.isUser, it.message) }
            adapter.submitList(ArrayList(chatMessages))

            binding.recyclerView.smoothScrollToPosition(adapter.itemCount - 1)
        }
    }


}