package com.example.gymmaster.fragment

import android.os.Binder
import android.os.Bundle
import android.renderscript.ScriptGroup.Binding
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.gymmaster.R
import com.example.gymmaster.databinding.FragmentAddMemberBinding
import com.example.gymmaster.databinding.FragmentAllMemberBinding

class FragmentAllMember : Fragment() {

    private lateinit var binding : FragmentAllMemberBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentAllMemberBinding.inflate(inflater, container, false)
        return binding.root
    }

}