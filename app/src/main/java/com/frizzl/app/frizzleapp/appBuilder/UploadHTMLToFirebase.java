package com.frizzl.app.frizzleapp.appBuilder;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Noga on 03/12/2018.
 */

public class UploadHTMLToFirebase {
    private static final String TAG = "UploadHTMLToFirebase";
    private FirebaseFirestore db;

    public UploadHTMLToFirebase(){
        // Access a Cloud Firestore instance from your Activity
        db = FirebaseFirestore.getInstance();
    }

    public void upload(String ID, String HTML){
        Map<String, Object> HTMLObject = new HashMap<>();
        HTMLObject.put("html", HTML);

        // Add a new document with a generated ID
        db.collection("usersAppsHTML")
                .document(ID)
                .set(HTMLObject)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot added"))
                .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
    }
}
