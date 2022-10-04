package listeners.messages;

import com.slack.api.bolt.App;
import java.util.regex.Pattern;
import listeners.ListenerProvider;

public class MessageListeners implements ListenerProvider {
    @Override
    public void register(App app) {
        Pattern greetingsPattern = Pattern.compile("(?i)(hi|hello|hey)");
        app.message(greetingsPattern, new SampleMessageListener(app));
    }
}
