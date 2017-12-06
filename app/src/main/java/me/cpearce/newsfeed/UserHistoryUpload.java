package me.cpearce.newsfeed;

import android.os.AsyncTask;

import me.cpearce.newsfeed.model.Article;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Robert on 12/2/2017.
 */

public class UserHistoryUpload extends AsyncTask<Article,Void,String> {
    private static final FirebaseDatabase database = FirebaseDatabase.getInstance();

    // public AsyncResponse delegate = null;

    @Override
    protected String doInBackground(Article... parms)
    {
        String response = "";

        try
        {
            //get uid
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            //get table for uid
            DatabaseReference myRef = database.getReference(user.getUid());
            myRef.push().setValue(parms[0]);
            response = "success";
        }

        catch(Exception e)
        {
            e.printStackTrace();
            response = "error saving article";
        }

        finally
        {
            return response;
        }
    }

    @Override
    protected void onPostExecute(String result) {
      //  delegate.porcessFinish(result);
    }
}
