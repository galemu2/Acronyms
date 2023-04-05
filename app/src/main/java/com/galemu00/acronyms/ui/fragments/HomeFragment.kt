package com.galemu00.acronyms.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    View.OnClickListener, SearchView.OnCloseListener {

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
        viewModel.getAcronyms("HMM")
        viewModel.acronymList.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading ->{

                }
                is Resource.Success -> {
                    binding.emptyBackground.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE
                    response.data?.forEach { acronymItem ->
                        adapter.differ.submitList(acronymItem.lfs)
                    }
                }
                else -> {
                    binding.emptyBackground.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.GONE
                }
            }
        }

        return binding.root
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
        adapter.differ.submitList(ArrayList<Lf>())
        binding.emptyBackground.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE
        binding.searchBox.clearFocus()
        return true
    }
}