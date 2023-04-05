package com.galemu00.acronyms.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
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

class HomeFragment : Fragment(R.layout.fragment_home),
    View.OnClickListener, SearchView.OnCloseListener, SearchView.OnQueryTextListener {

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
        binding.searchBox.setOnSearchClickListener(this)
        binding.searchBox.setOnQueryTextListener(this)

        viewModel.acronymList.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    // TODO loading view
                }
                is Resource.Success -> {

                    binding.emptyBackground.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE
                    binding.emptyResult.visibility = View.GONE

                    hideKeyboard()

                    if ((response.data != null) && (response.data.size > 0)) {
                        response.data.forEach { acronymItem ->
                            adapter.differ.submitList(acronymItem.lfs)
                        }
                    } else {
                        // TODO show empty list
                        Toast.makeText(context, "empty list", Toast.LENGTH_SHORT).show()
                        // hide the resycler view
                        // hide the bank screen
                        // show text for empty list
                        binding.recyclerView.visibility = View.GONE
                        binding.emptyBackground.visibility = View.GONE
                        binding.emptyResult.visibility = View.VISIBLE
                    }
                }
                is Resource.Error -> {
                    // TODO show error
                }
                is Resource.Idel -> {
                    if (adapter.differ.currentList.isNotEmpty()){
                        adapter.differ.submitList(ArrayList<Lf>())
                    }

                }
                else -> {

                    Toast.makeText(
                        context, "back to empty ${adapter.differ.currentList.size}",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.emptyBackground.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.GONE
                    binding.emptyResult.visibility = View.GONE


                }
            }
        }

        return binding.root
    }

    private fun hideKeyboard() {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as
                InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    override fun onClick(v: View?) {
        //TODO search box is clicked

        // Toast.makeText(context, "onClose()", Toast.LENGTH_SHORT).show()
    }

    override fun onClose(): Boolean {
        viewModel.clearResults()
        binding.emptyBackground.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE
        binding.emptyResult.visibility = View.GONE

        hideKeyboard()

        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        // search method
        query?.let { search ->
            viewModel.getAcronyms(search)
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return false
    }

}