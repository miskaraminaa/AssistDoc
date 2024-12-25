package ma.ensa.www.assistdoc;

import android.annotation.SuppressLint;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    @SuppressLint("StaticFieldLeak")
    public static final MyApplication context = (MyApplication) MyApplication.getInstance().getApplicationContext();

    @SuppressLint("StaticFieldLeak")
    public static final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public static final FirebaseAuth auth = FirebaseAuth.getInstance();

    private static String userid = "";

    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int REQUEST_IMAGE_PICK = 2;
    public static final int MESSAGE_RIGHT = 1;
    public static final int MESSAGE_LEFT = 2;

    // Method to get the logged-in user's UID
    public static String getUidLoggedIn() {
        if (auth.getCurrentUser() != null) {
            userid = auth.getCurrentUser().getUid();
        }
        return userid;
    }

    // Method to get the current time as a string in HH:mm:ss format
    public static String getTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        return formatter.format(date);
    }
}
