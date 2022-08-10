package com.example.mysalon.view.adapter

import android.content.Intent
import android.net.Uri
import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mysalon.R
import com.example.mysalon.databinding.ViewAboutUsBinding
import com.example.mysalon.model.remote.Constants
import com.example.mysalon.model.remote.data.contacts.Contact
import com.example.mysalon.viewModel.MainViewModel


class AboutUsAdapter(private val fragment: Fragment, val infoList: ArrayList<Contact>) :
    RecyclerView.Adapter<AboutUsAdapter.ContactHolder>() {
    lateinit var mainViewModel: MainViewModel
    lateinit var binding: ViewAboutUsBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactHolder {
        binding =
            ViewAboutUsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        mainViewModel =
            ViewModelProvider(fragment.requireActivity())[MainViewModel::class.java]
        return ContactHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactHolder, position: Int) {
        holder.apply {
            val info = infoList.get(position)
            holder.bind(info)
        }
    }

    override fun getItemCount(): Int {
        return infoList.size
    }


    inner class ContactHolder(val binding: ViewAboutUsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(contact: Contact) {
            binding.tvContactTitle.text = contact.contactTitle
            binding.tvContactDetails.text = contact.contactData
            if (contact.contactType == "PHONE") {
                binding.ivBynIcon1.setImageResource(R.drawable.ic_baseline_call_24)
                binding.ivBynIcon2.visibility = View.VISIBLE
                binding.ivBynIcon2.setImageResource(R.drawable.ic_outline_email_24)
                Glide.with(fragment)
                    .load(Constants.BASE_IMAGE_URL + contact.iconUrl)
                    .into(binding.ivIcon)
                binding.ivBynIcon1.setOnClickListener {
                    val uri: Uri = Uri.parse("tel:" + contact.contactData)
                    val intent = Intent(Intent.ACTION_DIAL, uri)
                    fragment.requireActivity().startActivity(intent)
                }
                binding.ivBynIcon2.setOnClickListener {
                    val uri: Uri = Uri.parse("smsto:" + contact.contactData)
                    val intent = Intent(Intent.ACTION_SENDTO, uri)
                    fragment.requireActivity().startActivity(intent)
                }
            }
            if (contact.contactType == "EMAIL") {
                binding.ivBynIcon1.setImageResource(R.drawable.ic_outline_email_24)
                Glide.with(fragment)
                    .load(Constants.BASE_IMAGE_URL + contact.iconUrl)
                    .into(binding.ivIcon)
                binding.ivBynIcon1.setOnClickListener {

                    val intent = Intent()
                    intent.action = "android.intent.action.SEND"
                    intent.putExtra(
                        Intent.EXTRA_EMAIL,
                        contact.contactData
                    )
                    intent.type = "text/plain"
                    fragment.requireActivity().startActivity(intent)
                }
            }
            if (contact.contactType == "GEO") {
                val arrayOfString: List<String> = contact.contactData.split("###")
                val latitude = arrayOfString[0].toDouble()
                val longitude = arrayOfString[1].toDouble()
                binding.tvContactDetails.text = arrayOfString[2]
                binding.ivBynIcon1.setImageResource(R.drawable.ic_outline_location_on_24)
                Glide.with(fragment)
                    .load(Constants.BASE_IMAGE_URL + contact.iconUrl)
                    .into(binding.ivIcon)
                binding.ivBynIcon1.setOnClickListener {
                    val stringBuilder = StringBuilder()
                    stringBuilder.append("geo:")
                    stringBuilder.append(latitude)
                    stringBuilder.append(",")
                    stringBuilder.append(longitude)
                    val intent =
                        Intent(Intent.ACTION_VIEW, Uri.parse(stringBuilder.toString()))
                    fragment.requireActivity().startActivity(intent)
                }
            }
            if (contact.contactType == "WEB_LINK") {
                Linkify.addLinks(binding.tvContactDetails, Linkify.WEB_URLS)
//                binding.tvContactDetails.setTextColor(fragment.resources.getColor(R.color.teal_200))
                binding.ivBynIcon1.visibility = View.GONE
                Glide.with(fragment)
                    .load(Constants.BASE_IMAGE_URL + contact.iconUrl)
                    .into(binding.ivIcon)
//                binding.tvContactDetails.setOnClickListener {
//                    val intent =
//                        Intent(Intent.ACTION_VIEW, Uri.parse(contact.contactData))
//                    fragment.requireActivity().startActivity(intent)
//                }
            }
            if (contact.contactType == "TEXT") {
                binding.ivIcon.visibility = View.GONE
                binding.ivBynIcon1.visibility = View.GONE
            }

        }
    }
}
