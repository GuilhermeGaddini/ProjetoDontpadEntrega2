package com.example.projetodontpadentrega2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView mensagemRecyclerView;
    private ChatAdapter adapter;
    private List<TagContent> mensagens;
    private FirebaseUser fireUser;
    private CollectionReference mMsgsReference;
    private LinearLayout container;
    private TagContent tagcontent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.projetodontpadentrega2.R.layout.activity_chat);
        mensagemRecyclerView = findViewById(com.example.projetodontpadentrega2.R.id.mensagensRecyclerView);
        mensagens = new ArrayList<>();
        adapter = new ChatAdapter(this, mensagens);
        Intent intent = getIntent();
        tagcontent = intent.getParcelableExtra("ObjectTagContent");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(false);
        mensagemRecyclerView.setLayoutManager(linearLayoutManager);
        mensagemRecyclerView.setAdapter(adapter);
        mensagemRecyclerView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatActivity.this, TagAndTextActivity.class);
            }
        });
    }

    private void setupFirebase() {
        fireUser = FirebaseAuth.getInstance().getCurrentUser();
        mMsgsReference = FirebaseFirestore.getInstance().collection("mensagens");
        getRemoteMsgs();
    }

    private void getRemoteMsgs() {
        mMsgsReference.addSnapshotListener((queryDocumentSnapshots, e) -> {
            mensagens.clear();
            if (queryDocumentSnapshots != null) {
                for (DocumentSnapshot docSnap : queryDocumentSnapshots.getDocuments()) {
                    TagContent m = docSnap.toObject(TagContent.class);
                    mensagens.add(m);
                }
            }
            Collections.sort(mensagens);
            adapter.notifyDataSetChanged();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        setupFirebase();
    }

    public void enviarMensagem(TagContent tagcontent) {
        mMsgsReference.add(tagcontent);
    }

    public void editTAG(View view) {
        Intent intent = new Intent(ChatActivity.this, TagAndTextActivity.class);
        startActivity(intent);
    }

}

class ChatViewHolder extends RecyclerView.ViewHolder{
    TextView tag;
    TextView text;
    ImageView imageView;
    LinearLayout container;

    public ChatViewHolder(@NonNull View itemView) {
        super(itemView);
        this.container = itemView.findViewById(com.example.projetodontpadentrega2.R.id.container);
        this.tag = itemView.findViewById(com.example.projetodontpadentrega2.R.id.tag_name);
        this.text = itemView.findViewById(com.example.projetodontpadentrega2.R.id.textField);
        this.imageView = itemView.findViewById(com.example.projetodontpadentrega2.R.id.image);
    }
}

class ChatAdapter extends RecyclerView.Adapter <ChatViewHolder>{

    private Context context;
    private List<TagContent> mensagens;

    public ChatAdapter(Context context, List<TagContent> mensagem) {
        this.context = context;
        this.mensagens = mensagem;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View raiz = inflater.inflate(com.example.projetodontpadentrega2.R.layout.list_item, parent,false);
        return new ChatViewHolder(raiz);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        TagContent m = mensagens.get(position);//mensagens na lista
        holder.tag.setText(m.getTag());
        holder.text.setText(m.getText());
        StorageReference pictureStorageReference = FirebaseStorage.getInstance().getReference(String.format(Locale.getDefault(),
                "images/%s/profilePic.jpg",m.getEmail().replace("@", "")));
    }

    @Override
    public int getItemCount() {
        return mensagens.size();
    }

}
