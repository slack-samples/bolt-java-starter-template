import com.slack.api.bolt.App;
import com.slack.api.bolt.jetty.SlackAppServer;
import com.slack.api.bolt.socket_mode.SocketModeApp;
import listeners.Listeners;

public class OAuthMain {

    public static void main(String[] args) throws Exception {

        // App expects an env variable: SLACK_SIGNING_SECRET
        var apiApp = new App();
        Listeners.register(apiApp);

        // OAuth Flow Handler App
        //  expected env variables:
        //   SLACK_CLIENT_ID, SLACK_CLIENT_SECRET, SLACK_REDIRECT_URI, SLACK_SCOPES,
        //   SLACK_INSTALL_PATH, SLACK_REDIRECT_URI_PATH
        //   SLACK_OAUTH_COMPLETION_URL, SLACK_OAUTH_CANCELLATION_URL
        var oauthApp = new App().asOAuthApp(true);

        var socketModeApp = new SocketModeApp(apiApp);
        var oauthServer = new SlackAppServer(oauthApp);

        // SocketModeApp expects an env variable: SLACK_APP_TOKEN
        socketModeApp.startAsync();
        oauthServer.start();
    }
}
