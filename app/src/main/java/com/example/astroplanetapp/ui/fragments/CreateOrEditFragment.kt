package com.example.astroplanetapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import com.example.astroplanetapp.R
import com.example.astroplanetapp.databinding.FragmentCreateOrEditBinding
import com.example.astroplanetapp.ui.MainActivity

class CreateOrEditFragment : Fragment() {

    private var _binding: FragmentCreateOrEditBinding? = null
    private val binding get() = _binding!!

    private var actionBarActivity:MainActivity?=null

    private var mIsEdit: Boolean = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCreateOrEditBinding.inflate(inflater, container, false)
        val rootView = binding.root

        return rootView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        actionBarActivity = activity as? MainActivity

        if (actionBarActivity != null) {
            // createEntityStore()
            setupActionBar()
            // setupTextFields()
        }
    }

    private fun setupActionBar() {

        val toolbar = activity?.findViewById<Toolbar>(R.id.toolbar_id)
        actionBarActivity?.let {
            if (toolbar != null) {
                it.loadActionBarInActivity(toolbar)
                it.loadButtonBackInScreen(true)
                it.loadItemsOnMenuActionBar(toolbar)
            }

        }
    }
}