package com.yasarkiremitci.chatgbt.adapter


import android.app.AlertDialog

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.yasarkiremitci.chatgbt.Fragments.HomeChatDirections
import com.yasarkiremitci.chatgbt.R
import com.yasarkiremitci.chatgbt.models.MessageModel



class MessageAdapter(val list: ArrayList<MessageModel>) : Adapter<MessageAdapter.MessageViewHolder>() {
    inner class MessageViewHolder(view: View) : ViewHolder(view) {
        val msgTxt = view.findViewById<TextView>(R.id.right_chat_text_view)
        val msgTxt2 = view.findViewById<TextView>(R.id.left_chat_text_view)
        val msgImage = view.findViewById<LinearLayout>(R.id.right_chat_view)
        val msgImage2 = view.findViewById<ImageView>(R.id.left_chat_view)
        val iconCopy = view.findViewById<ImageView>(R.id.iconCopy)
        val unlockImage = view.findViewById<ImageView>(R.id.unlockImage)
        val unlocktext = view.findViewById<TextView>(R.id.unlocktext)
        val unloklinear = view.findViewById<LinearLayout>(R.id.unlockLinear)



    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        var view: View? = null

        var from = LayoutInflater.from(parent.context)

        if (viewType == 0) {

            view = from.inflate(R.layout.chat_item_right, parent, false)
        } else {
            view = from.inflate(R.layout.chat_item_left, parent, false)


        }

        return MessageViewHolder(view)

    }

    override fun getItemViewType(position: Int): Int {
        val message = list[position]
        return if (message.isUser) 0 else 1


    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = list[position]
        val sharedPreferences =holder.itemView.context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        val value = sharedPreferences.getString("premium", "false")


        if (!message.isUser) {

            if(value=="true"){
                holder.unloklinear.visibility = View.GONE
            }else if( position<=8){
                holder.unloklinear.visibility = View.GONE
            }else if (value=="false" && position>9){
                holder.unloklinear.visibility = View.VISIBLE
            }



            holder.msgTxt2.visibility = View.VISIBLE
            holder.msgImage2.visibility = View.VISIBLE
            holder.msgTxt2.text = message.message
            holder.msgTxt2.setOnLongClickListener {
                val text = holder.msgTxt2.text.toString()
                copyTextToClipboard(holder.itemView.context, text)
                true
            }
            holder.iconCopy.setOnClickListener {
                val text = holder.msgTxt2.text.toString()
                copyTextToClipboard(holder.itemView.context, text)
                showCopySelectedDialog(holder.itemView.context, text)
            }

            holder.unlocktext.setOnClickListener {
                val action = R.id.action_homeChat_to_inApp
                HomeChatDirections.actionHomeChatToSettings()
                holder.itemView.findNavController().navigate(action)
            }

            holder.unlockImage.setOnClickListener {
                val action = R.id.action_homeChat_to_inApp
                HomeChatDirections.actionHomeChatToSettings()
                holder.itemView.findNavController().navigate(action)
            }
        } else {
            holder.msgTxt.visibility = View.VISIBLE
            holder.msgImage.visibility = View.VISIBLE
            holder.msgTxt.text = message.message
            holder.msgTxt.setOnLongClickListener {
                val text = holder.msgTxt.text.toString()
                copyTextToClipboard(holder.itemView.context, text)
                true
            }

        }




    }


    private fun showCopySelectedDialog(context: Context, text: String) {
        val dialogBuilder = AlertDialog.Builder(context, R.style.TransparentAlertDialog)
        val inflater = LayoutInflater.from(context)
        val dialogView = inflater.inflate(R.layout.dialog_copy_selected, null)
        dialogBuilder.setView(dialogView)

        val image12 = dialogView.findViewById<ImageView>(R.id.imageView12)
        // image12 ImageView'ı kullanabilirsiniz

        // Diğer bileşenleri kaldırma

        val alertDialog = dialogBuilder.create()
        alertDialog.show()

        // 3 saniye sonra alertDialog'u gizle
        Handler(Looper.getMainLooper()).postDelayed({
            alertDialog.dismiss()
        }, 3000)

        val layoutParams = WindowManager.LayoutParams().apply {
            //R.style.TransparentAlertDialog
            width = WindowManager.LayoutParams.WRAP_CONTENT
            height =WindowManager.LayoutParams.WRAP_CONTENT // Set the width to wrap content
            gravity = Gravity.CENTER_HORIZONTAL and Gravity.CENTER_VERTICAL // Set the gravity to center horizontally
        }
        alertDialog.window?.attributes = layoutParams
        alertDialog.window?.apply {
            setBackgroundDrawableResource(R.drawable.copyselected)
            setGravity(Gravity.CENTER)
        }
    }




    private fun copyTextToClipboard(context: Context, text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Copied Text", text)
        clipboard.setPrimaryClip(clip)

    }



    fun submitList(newList: ArrayList<MessageModel>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }






}

