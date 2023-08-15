package com.example.ejercicio_32;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.ejercicio_32.Adapter.NoteAdapter;
import com.example.ejercicio_32.Clases.Note;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Recycler extends AppCompatActivity {

    private EditText editText;
    private Button addButton,btnback;
    private RecyclerView recycler;
    private NoteAdapter noteAdapter;
    private List<Note> notesList;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        editText = findViewById(R.id.edit_text);
        addButton = findViewById(R.id.add_button);
        recycler = findViewById(R.id.recycler_view);
        btnback = findViewById(R.id.btnback);

        notesList = new ArrayList<>();
        noteAdapter = new NoteAdapter(notesList);

        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(noteAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("notes");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                notesList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Note note = dataSnapshot.getValue(Note.class);
                    notesList.add(note);
                }
                noteAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Manejar el error si es necesario
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String noteText = editText.getText().toString().trim();
                if (!noteText.isEmpty()) {
                    Note newNote = new Note(noteText);
                    databaseReference.push().setValue(newNote);
                    editText.setText("");
                }
            }
        });

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Recycler.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}