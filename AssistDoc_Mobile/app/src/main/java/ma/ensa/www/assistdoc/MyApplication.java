package ma.ensa.www.assistdoc;
import android.app.Application;

public class MyApplication extends Application {

    // Static variable to hold the application instance
    private static MyApplication instance;

    // Getter method to access the application instance
    public static MyApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize the instance of MyApplication
        instance = this;
    }
}
