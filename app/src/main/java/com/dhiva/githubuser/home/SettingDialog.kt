package com.dhiva.githubuser.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.dhiva.githubuser.databinding.FragmentSettingBinding

class SettingDialog: DialogFragment() {
    private var _binding: FragmentSettingBinding? = null
    private val viewModel: MainViewModel by activityViewModels()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        dialog!!.window!!.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.switchDarkMode.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            viewModel.saveThemeSetting(isChecked)
            binding.switchDarkMode.isChecked = isChecked
        }
        viewModel.getThemeSettings().observe(this, {
            binding.switchDarkMode.isChecked = it
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}