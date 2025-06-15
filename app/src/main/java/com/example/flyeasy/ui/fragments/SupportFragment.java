package com.example.flyeasy.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flyeasy.R;
import com.example.flyeasy.model.SupportMessageEntity;
import com.example.flyeasy.ui.adapters.SupportMessageAdapter;
import com.example.flyeasy.viewmodel.SupportViewModel;

import java.util.Date;

public class SupportFragment extends Fragment {

    private SupportViewModel supportViewModel;
    private SupportMessageAdapter adapter;
    private int userId ;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_support, container, false);

        EditText editText = view.findViewById(R.id.editTextSupportMessage);
        Button sendBtn = view.findViewById(R.id.buttonSendMessage);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewSupport);

        adapter = new SupportMessageAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        supportViewModel = new ViewModelProvider(this).get(SupportViewModel.class);

        supportViewModel.getMessagesForUser(userId).observe(getViewLifecycleOwner(), adapter::setMessages);

        sendBtn.setOnClickListener(v -> {
            String msg = editText.getText().toString().trim();
            if (!msg.isEmpty()) {
                SupportMessageEntity message = new SupportMessageEntity(userId, msg, "", new Date().getTime());
                supportViewModel.sendMessage(message);
                editText.setText("");
            }
        });

        return view;
    }
}
