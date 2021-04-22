package com.cas.hitmeup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    String receiverImage, receiverUID, receiverName, senderUID;
    CircleImageView profileImage;
    TextView tvReceiverName;
    FirebaseDatabase database;
    FirebaseAuth firebaseAuth;
    public static String sImage;
    public static String rImage;

    CardView sendBtn;
    EditText edtMessage;

    String senderRoom, receiverRoom;

    RecyclerView messageAdapter;
    ArrayList<Messages> messagesArrayList;

    MessagesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        TranslatorOptions options = new TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(TranslateLanguage.KOREAN)
                .build();

        final Translator translator = Translation.getClient(options);

        DownloadConditions conditions = new DownloadConditions.Builder()
                .requireWifi()
                .build();
        translator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ChatActivity.this,
                                "Model Downloaded", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Model couldn't be downloaded or other internal error.
                        Toast.makeText(ChatActivity.this,
                                "Download Failed", Toast.LENGTH_SHORT).show();
                    }
                });

        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        receiverName = getIntent().getStringExtra("name");
        receiverImage = getIntent().getStringExtra("ReceiverImage");
        receiverUID = getIntent().getStringExtra("uid");

        messagesArrayList = new ArrayList<>();

        profileImage = findViewById(R.id.profile_image);
        tvReceiverName = findViewById(R.id.receiverName);

        messageAdapter = findViewById(R.id.messageAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        messageAdapter.setLayoutManager(linearLayoutManager);
        adapter = new MessagesAdapter(ChatActivity.this, messagesArrayList);
        messageAdapter.setAdapter(adapter);

        sendBtn = findViewById(R.id.sendBtn);
        edtMessage = findViewById(R.id.edtMessage);

        Picasso.get().load(receiverImage).into(profileImage);
        tvReceiverName.setText(""+receiverName);

        senderUID = firebaseAuth.getUid();

        senderRoom = senderUID+receiverUID;
        receiverRoom = receiverUID+senderUID;



        DatabaseReference reference = database.getReference().child("user").child(firebaseAuth.getUid());
        DatabaseReference chatReference = database.getReference().child("chats").child(senderRoom).child("messages");

        chatReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                messagesArrayList.clear();

                for(DataSnapshot dataSnapshot:snapshot.getChildren()) {
                    Messages messages = dataSnapshot.getValue(Messages.class);
                    messagesArrayList.add(messages);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                sImage = snapshot.child("imageuri").getValue().toString();
                rImage = receiverImage;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = edtMessage.getText().toString();
                if(message.isEmpty()) {
                    Toast.makeText(ChatActivity.this, "Please Enter Valid Message", Toast.LENGTH_SHORT).show();
                    return;
                }

                translator.translate(message).addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        edtMessage.setText("");
                        Date date = new Date();

                        Messages messages = new Messages(message+ "\n" +s, senderUID, date.getTime());

                        database = FirebaseDatabase.getInstance();
                        database.getReference().child("chats")
                                .child(senderRoom)
                                .child("messages")
                                .push()
                                .setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                database.getReference().child("chats")
                                        .child(receiverRoom)
                                        .child("messages")
                                        .push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                    }
                                });
                            }
                        });
                    }
                });
            }
        });

    }
}