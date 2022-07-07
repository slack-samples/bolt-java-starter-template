import com.slack.api.bolt.App;
import com.slack.api.bolt.socket_mode.SocketModeApp;
import listeners.Listeners;

public class Main {

    public static void main(String[] args) throws Exception {
        // App expects an env variable: SLACK_BOT_TOKEN
        App app = new App();
        Listeners.register(app);
        // SocketModeApp expects an env variable: SLACK_APP_TOKEN
        new SocketModeApp(app).start();
    }
}
