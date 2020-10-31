package com.andronauts.quizard.general.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andronauts.quizard.dataModels.Chat;
import com.andronauts.quizard.databinding.RecyclerChatBinding;
import com.andronauts.quizard.utils.FileManager;
import com.andronauts.quizard.utils.SharedPrefs;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;

import org.mortbay.util.ajax.JSON;

import java.io.File;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChatRecycler extends RecyclerView.Adapter<ChatRecycler.ViewHolder> {
    private RecyclerChatBinding binding;
    private Context context;
    private List<Chat> data;
    private SharedPrefs prefs;
    private FileManager fileManager;

    public ChatRecycler(Context context, List<Chat> chats) {
        this.context = context;
        this.data = chats;

        prefs = new SharedPrefs(context);
        fileManager = new FileManager(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RecyclerChatBinding.inflate(LayoutInflater.from(context),parent,false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Chat chat = data.get(position);

        holder.message.setText(chat.getMessage());
        holder.time.setText("time");

        if(chat.getMedia()!=null){
            holder.media.setVisibility(View.VISIBLE);

            holder.media.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    File mediaFile = fileManager.getChatMediaFile();

                    FirebaseStorage.getInstance().getReference(chat.getMedia()).getFile(mediaFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            fileManager.openImageFile(mediaFile);
                        }
                    });
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView message,time;
        ImageView media;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            message = binding.message;
            time = binding.time;
            media =binding.media;
        }
    }
}
