package ma.ensa.www.assistdoc.model;
import android.os.Parcel;
import android.os.Parcelable;

public class Users implements Parcelable {
    String userId;
    String status;
    String userName;
    String email;
    String type;
    String age;
    String gender;
    String profilepic ;
    String lastMessage;
    String password ;

    public Users() {
        // Firebase nécessite un constructeur par défaut
    }

    public Users(String id, String userName, String email, String password, String profilepic, String status) {
        this.userId= id ;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.profilepic = profilepic;
        this.status = status;

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    // Constructeur avec tous les paramètres
    public Users(String userId, String status, String username, String email, String type, String age, String gender) {
        this.userId = userId;
        this.status = status;
        this.userName = username;
        this.email = email;
        this.type = type;
        this.age = age;
        this.gender = gender;
    }

    // Constructeur secondaire pour créer un objet Users à partir d'un Parcel
    protected Users(Parcel in) {
        userId = in.readString();  // Utilisez readLong() pour récupérer le userId comme long
        status = in.readString();
        userName = in.readString();
        email = in.readString();
        type = in.readString();
        age = in.readString();
        gender = in.readString();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUsername() {
        return userName;
    }

    public void setUsername(String username) {
        this.userName = username;
    }

    public String getUseremail() {
        return email;
    }

    public void setUseremail(String email) {
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    // Méthode writeToParcel pour écrire dans le Parcel
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(status);
        dest.writeString(userName);
        dest.writeString(email);
        dest.writeString(type);
        dest.writeString(age);
        dest.writeString(gender);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Users> CREATOR = new Parcelable.Creator<Users>() {
        @Override
        public Users createFromParcel(Parcel in) {
            return new Users(in);
        }

        @Override
        public Users[] newArray(int size) {
            return new Users[size];
        }
    };
}
