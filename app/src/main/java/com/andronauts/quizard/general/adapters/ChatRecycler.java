package com.andronauts.quizard.general.adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andronauts.quizard.dataModels.Chat;
import com.andronauts.quizard.databinding.RecyclerChatBinding;
import com.andronauts.quizard.utils.DateFormatter;
import com.andronauts.quizard.utils.FileManager;
import com.andronauts.quizard.utils.SharedPrefs;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.itextpdf.text.pdf.parser.Line;

import org.mortbay.util.ajax.JSON;

import java.io.File;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.http.GET;

public class ChatRecycler extends RecyclerView.Adapter<ChatRecycler.ViewHolder> {
    private RecyclerChatBinding binding;
    private Context context;
    private List<Chat> data;
    private SharedPrefs prefs;
    private FileManager fileManager;
    private String userId;

    public ChatRecycler(Context context, List<Chat> chats,String userId) {
        this.context = context;
        this.data = chats;
        this.userId = userId;

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
        holder.time.setText(new DateFormatter(chat.getTime()).getDateAndTime());

        if(chat.getMedia()!=null){
            holder.media.setVisibility(View.VISIBLE);
        }

        holder.media.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File mediaFile = fileManager.getChatMediaFile();

                FirebaseStorage.getInstance().getReference(chat.getMedia()).getFile(mediaFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        fileManager.openImageFile(mediaFile);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        if(chat.getReceiver() == userId){
            holder.container.setGravity(Gravity.RIGHT);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView message,time;
        ImageView media;
        LinearLayout container;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            message = binding.message;
            time = binding.time;
            media =binding.media;
            container = binding.container;
        }
    }
}
