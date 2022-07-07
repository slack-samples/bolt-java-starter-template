package listeners;

import com.slack.api.bolt.App;

public interface ListenerProvider {
    void register(App app);
}
