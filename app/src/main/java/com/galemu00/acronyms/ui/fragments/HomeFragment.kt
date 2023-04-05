package com.galemu00.acronyms.ui.fragments

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.galemu00.acronyms.R
import com.galemu00.acronyms.adapters.AcronymsAdapter
import com.galemu00.acronyms.data.model.Lf
import com.galemu00.acronyms.databinding.FragmentHomeBinding
import com.galemu00.acronyms.ui.AcronymsViewModel
import com.galemu00.acronyms.util.Resource

class HomeFragment : Fragment(R.layout.fragment_home), SearchView.OnCloseListener, SearchView
.OnQueryTextListener {

    private val viewModel: AcronymsViewModel by viewModels()
    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding
        get() = _binding!!

    private val adapter: AcronymsAdapter = AcronymsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val layoutManager = LinearLayoutManager(context)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = this.adapter

        binding.searchBox.setOnCloseListener(this)
        binding.searchBox.setOnQueryTextListener(this)

        viewModel.acronymList.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    loadingState()
                }
                is Resource.Success -> {
                    successResult()
                    hideKeyboard()
                    if ((response.data != null) && (response.data.size > 0)) {
                        response.data.forEach { acronymItem ->
                            adapter.differ.submitList(acronymItem.lfs)
                        }
                    } else {
                        emptyResult()
                    }
                }
                is Resource.Error -> {
                    errorResult()
                }
                is Resource.Idel -> {
                    if (adapter.differ.currentList.isNotEmpty()) {
                        adapter.differ.submitList(ArrayList<Lf>())
                    }
                    defaultUIState()
                }
                else -> {
                    defaultUIState()
                }
            }
        }

        return binding.root
    }

    private fun loadingState() {
        binding.emptyBackground.visibility = View.GONE
        binding.recyclerView.visibility = View.GONE
        binding.emptyResult.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
        binding.errorResult.visibility = View.GONE
    }

    private fun emptyResult() {
        binding.recyclerView.visibility = View.GONE
        binding.emptyBackground.visibility = View.GONE
        binding.emptyResult.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
        binding.errorResult.visibility = View.GONE
    }

    private fun errorResult() {
        binding.emptyBackground.visibility = View.GONE
        binding.recyclerView.visibility = View.GONE
        binding.emptyResult.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        binding.errorResult.visibility = View.VISIBLE
    }

    private fun defaultUIState() {
        binding.emptyBackground.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE
        binding.emptyResult.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        binding.errorResult.visibility = View.GONE
    }

    private fun successResult() {
        binding.recyclerView.visibility = View.VISIBLE
        binding.emptyBackground.visibility = View.GONE
        binding.emptyResult.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        binding.errorResult.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun hideKeyboard() {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as
                InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }

    override fun onClose(): Boolean {
        viewModel.clearResults()
        defaultUIState()
        hideKeyboard()
        return true
    }

    private fun networkCapabilities(): NetworkCapabilities? {
        val connectivityManager = context?.applicationContext?.getSystemService(
            Context
                .CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        return connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        // search method

        val networkActivityInfo =
            networkCapabilities()

        if (networkActivityInfo != null) {
            query?.let { search ->
                viewModel.getAcronyms(search)
            }
        } else {
            Toast.makeText(context, "No network available", Toast.LENGTH_SHORT).show()
        }
        return true
    }


    override fun onQueryTextChange(newText: String?): Boolean {
        return false
    }

}