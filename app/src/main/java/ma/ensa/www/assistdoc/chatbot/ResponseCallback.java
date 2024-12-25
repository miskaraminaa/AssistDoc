
package ma.ensa.www.assistdoc.chatbot;

public interface ResponseCallback {

    void onResponse(String response);

    void onError(Throwable throwable);
}
