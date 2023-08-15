package com.example.ejercicio_32;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.ejercicio_32.Adapter.CardAdapter;
import com.example.ejercicio_32.Clases.CardClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.List;

public class Card extends AppCompatActivity {

    private EditText editText;
    private Button addButton, btnback;
    private RecyclerView recycler;
    private CardAdapter cardAdapter;
    private List<CardClass> cardItemList;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        editText = findViewById(R.id.edit_text);
        addButton = findViewById(R.id.add_button);
        recycler = findViewById(R.id.recycler_view);
        btnback = findViewById(R.id.btnback);

        cardItemList = new ArrayList<>();
        cardAdapter = new CardAdapter(cardItemList);

        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(cardAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("card_items");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cardItemList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    CardClass cardItem = dataSnapshot.getValue(CardClass.class);
                    cardItemList.add(cardItem);
                }
                cardAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Manejar el error si es necesario
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newItemText = editText.getText().toString().trim();
                if (!newItemText.isEmpty()) {
                    CardClass newCardItem = new CardClass(newItemText);
                    databaseReference.push().setValue(newCardItem);
                    editText.setText("");

                    // Aquí envías la notificación después de agregar un nuevo elemento
                    sendNotification(newItemText);
                }
            }
        });

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Card.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void sendNotification(String newItemText) {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (task.isSuccessful()) {
                            String deviceToken = task.getResult();

                            Log.d("DeviceToken", deviceToken);
                            Log.d("Notification", "Notificación enviada exitosamente");


                            RemoteMessage notificationMessage = new RemoteMessage.Builder(deviceToken)
                                    .setMessageId("1")
                                    .addData("title", "Nuevo elemento agregado")
                                    .addData("body", "Se ha agregado un nuevo elemento: " + newItemText)
                                    .build();

                            FirebaseMessaging.getInstance().send(notificationMessage);
                        }else {
                            Log.e("Notification", "Error al enviar la notificación: " + task.getException());

                        }
                    }
                });
    }
}