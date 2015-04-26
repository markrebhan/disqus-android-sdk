package com.mrebhan.disqus.fragment;

import android.os.UserManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.mrebhan.disqus.DisqusSdkDaggerModule;
import com.mrebhan.disqus.DisqusSdkProvider;
import com.mrebhan.disqus.R;
import com.mrebhan.disqus.auth.AuthManager;
import com.mrebhan.disqus.datamodel.User;
import com.mrebhan.disqus.user.CurrentUserManager;
import com.mrebhan.disqus.widgets.AvatarImageView;
import com.mrebhan.disqus.widgets.EditText;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class PostCommentItem extends RecyclerView.ViewHolder implements ViewHolderItem {

    @Inject
    CurrentUserManager currentUserManager;
    @Inject
    AuthManager authManager;
    @Inject
    Picasso picasso;

    private AvatarImageView imageView;
    private EditText editText;

    public PostCommentItem(View itemView) {
        super(itemView);
        injectThis();
        imageView = (AvatarImageView) itemView.findViewById(R.id.img_avatar);
        editText = (EditText) itemView.findViewById(R.id.edit_text_comment);
        editText.setOnEditorActionListener(new MyOnEditorActionListener());
    }

    public static PostCommentItem createInstance(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_comment_entry, parent, false);
        return new PostCommentItem(view);
    }

    @Override
    public ViewHolderType getViewItemType() {
        return ViewHolderType.POST_COMMENT;
    }

    @Override
    public void onBindViewHolder(Object data, int leftPixelOffset) {
        if (authManager.isAuthenticated()) {
            currentUserManager.getCurrentUser(new MyCurrentUserCallback(), false);
        }
    }

    @Override
    public void injectThis() {
        DisqusSdkProvider.getInstance().getObjectGraph().inject(this);
    }

    private class MyCurrentUserCallback implements Callback<User> {
        @Override
        public void success(User user, Response response) {
            picasso.load(user.getAvatar().getPermalinkUrl()).placeholder(R.drawable.no_avatar_92).into(imageView);
        }

        @Override
        public void failure(RetrofitError error) {
            Log.e("", "error fetching user.", error);
        }
    }

    private class MyOnEditorActionListener implements TextView.OnEditorActionListener {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            boolean isHandled = false;
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                // send the message to the server
                isHandled = true;
            }

            return isHandled;
        }
    }
}
