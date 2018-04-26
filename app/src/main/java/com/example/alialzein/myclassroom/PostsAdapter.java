package com.example.alialzein.myclassroom;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import static java.text.DateFormat.getDateTimeInstance;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostsViewHolder> {

    private List<Posts> posts_list;
    private FirebaseAuth mAuth;
    private DatabaseReference Ref;
    private Context ctx;



    public PostsAdapter(List<Posts> posts_list) {
        this.posts_list = posts_list;
    }

    @Override
    public PostsViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.posts_layout_display, parent, false);
        ctx = parent.getContext();
        return new PostsViewHolder(v);
    }



    @Override
    public void onBindViewHolder(final PostsViewHolder holder, final int position) {
        mAuth=FirebaseAuth.getInstance();
        String userEmailOnline= mAuth.getCurrentUser().getEmail();
        Posts posts = posts_list.get(position);


        holder.InstEmail.setText(posts.getEmail());

        String messageType = posts.getType();
        final String classroom_id = posts.getClassroom_id();
        final String post_push_id = posts.getPost_push_id();
        long timeStamp = posts.getTime();
        DateFormat dateFormat = getDateTimeInstance();
        Date netDate = (new Date(timeStamp));
       // String t=dateFormat.format(netDate).
        String time= dateFormat.format(netDate);
        holder.timeStamp.setText(time);




        if (messageType.equals("text")) {
            holder.postText.setText(posts.getMessage());
            holder.messagePicture.setVisibility(View.GONE);
        }
        else
            {

               holder.postText.setVisibility(View.GONE);
               holder.postText.setPadding(0,0,0,0);
                Picasso.with(holder.messagePicture.getContext()).load(posts.getMessage())
                        .placeholder(R.drawable.default_profile_img).into(holder.messagePicture);



        }
        String instEmail = posts.getEmail();
        if(userEmailOnline.equals(instEmail)){

        holder.myView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new MaterialDialog.Builder(ctx)
                        .title("Do you want to delete the post?")
                        .positiveText("Yes")
                        .negativeText("No")
                        .theme(Theme.DARK)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference()
                                        .child("posts");
                                postsRef.child(classroom_id).child(post_push_id).removeValue()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    posts_list.remove(position);
                                                  //  notifyDataSetChanged();
                                                  notifyItemRemoved(position);




                                                    //   Toast.makeText(getActivity(), "ClassRoom Removed Successfully", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });


                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                            }
                        })
                        .show();
                return false;
            }
        });

        }

        holder.comment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toComment = new Intent(ctx, commentActivity.class);
                toComment.putExtra("classroom_id", classroom_id);
                toComment.putExtra("post_push_id", post_push_id);
               ctx.startActivity(toComment);
            }
        });
       // Ref= FirebaseDatabase.getInstance().getReference().child("")

      //
    }

    @Override
    public int getItemCount() {
        return posts_list.size();
    }

    public class PostsViewHolder extends RecyclerView.ViewHolder {
        public TextView postText;
        public TextView InstEmail;
        public ImageView messagePicture;
        public TextView timeStamp;
        public Button comment_btn;
        public View myView;

        public PostsViewHolder(View view) {
            super(view);
            myView = view;


            postText = (TextView) view.findViewById(R.id.post_text_message);
            InstEmail = (TextView) view.findViewById(R.id.instructor_email);
            messagePicture = (ImageView) view.findViewById(R.id.message_imageView);
            timeStamp = (TextView) view.findViewById(R.id.time_stamp);
            comment_btn = (Button) view.findViewById(R.id.comment_btn);
        }

    }





}
