package ma.ensa.www.assistdoc.mvvm;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ma.ensa.www.assistdoc.Utils;
import ma.ensa.www.assistdoc.entities.Messages;
import ma.ensa.www.assistdoc.model.Users;

public class ChatAppViewModel extends ViewModel {

    private MutableLiveData<String> message = new MutableLiveData<>();
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private MutableLiveData<String> name = new MutableLiveData<>();
    private MutableLiveData<String> imageUrl = new MutableLiveData<>();

    private ExecutorService executorService = Executors.newSingleThreadExecutor(); // ExecutorService for background tasks

    public ChatAppViewModel() {
        getCurrentUser();
    }

    // Method to get list of users from Firestore
    public LiveData<List<Users>> getUsers() {
        MutableLiveData<List<Users>> usersLiveData = new MutableLiveData<>();

        firestore.collection("Users")
                .addSnapshotListener((snapshot, exception) -> {
                    if (exception != null) {
                        Log.e("ChatAppViewModel", "Error fetching users: " + exception.getMessage());
                        return;
                    }
                    List<Users> usersList = new ArrayList<>();
                    if (snapshot != null) {
                        for (DocumentSnapshot document : snapshot.getDocuments()) {
                            Users user = document.toObject(Users.class);
                            if (user != null) {
                                usersList.add(user);
                            }
                        }
                    }
                    usersLiveData.setValue(usersList);
                });

        return usersLiveData;
    }



    public void sendMessage(String sender, String receiver, String friendname, String friendimage) {
        executorService.execute(() -> {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("sender", sender);
            hashMap.put("receiver", receiver);
            hashMap.put("message", message.getValue());
            hashMap.put("time", Utils.getTime());

            // Sorting the string by converting it into a char array, sorting, and joining back into a string
            String uniqueId = getSortedString(sender + receiver);

            firestore.collection("Messages")
                    .document(uniqueId)
                    .collection("chats")
                    .document(Utils.getTime())
                    .set(hashMap)
                    .addOnCompleteListener(task -> {
                        // Handle message addition result if necessary
                    });
        });
    }

    public LiveData<List<Messages>> getMessages(String friendid) {
        MutableLiveData<List<Messages>> messages = new MutableLiveData<>();

        // Sorting the string by converting it into a char array, sorting, and joining back into a string
        String uniqueId = getSortedString(Utils.getUidLoggedIn() + friendid);

        firestore.collection("Messages")
                .document(uniqueId)
                .collection("chats")
                .orderBy("time", Query.Direction.ASCENDING)
                .addSnapshotListener((snapshot, exception) -> {
                    if (exception != null) {
                        return;
                    }

                    List<Messages> messagesList = new ArrayList<>();
                    if (snapshot != null && !snapshot.isEmpty()) {
                        for (DocumentSnapshot document : snapshot.getDocuments()) {
                            Messages messageModel = document.toObject(Messages.class);
                            if (messageModel != null &&
                                    ((messageModel.getSender().equals(Utils.getUidLoggedIn()) && messageModel.getReceiver().equals(friendid)) ||
                                            (messageModel.getSender().equals(friendid) && messageModel.getReceiver().equals(Utils.getUidLoggedIn())))) {
                                messagesList.add(messageModel);
                            }
                        }
                        messages.setValue(messagesList);
                    }
                });

        return messages;
    }

    // Helper method to sort the string by characters
    private String getSortedString(String input) {
        char[] charArray = input.toCharArray();
        Arrays.sort(charArray);
        return new String(charArray);
    }



    // Method to get current user info
    public void getCurrentUser() {
        executorService.execute(() -> {
            firestore.collection("Users")
                    .document(Utils.getUidLoggedIn())
                    .addSnapshotListener((value, error) -> {
                        if (value != null && value.exists()) {
                            Users users = value.toObject(Users.class);
                            if (users != null) {
                                name.setValue(users.getUsername());
                            }
                        }
                    });
        });
    }

    // Method to update the user profile
    public void updateProfile() {
        executorService.execute(() -> {
            HashMap<String, Object> hashMapUser = new HashMap<>();
            hashMapUser.put("username", name.getValue());

            firestore.collection("Users")
                    .document(Utils.getUidLoggedIn())
                    .update(hashMapUser)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(Utils.context, "Updated", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    // Method to delete a user
    public void deleteUser(String userId, OnSuccessListener onSuccess, OnFailureListener onFailure) {
        firestore.collection("Users")
                .document(userId)
                .delete()
                .addOnSuccessListener((com.google.android.gms.tasks.OnSuccessListener<? super Void>) onSuccess)
                .addOnFailureListener((com.google.android.gms.tasks.OnFailureListener) onFailure);
    }

    public MutableLiveData<String> getMessage() {
        return message;
    }

    public MutableLiveData<String> getName() {
        return name;
    }

    public MutableLiveData<String> getImageUrl() {
        return imageUrl;
    }

    public void setMessage(String message) {
        this.message.setValue(message);
    }

    public interface OnSuccessListener {
        void onSuccess();
    }

    public interface OnFailureListener {
        void onFailure(Exception exception);
    }
}
