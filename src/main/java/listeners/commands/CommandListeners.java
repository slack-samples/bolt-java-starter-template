package listeners.commands;

import com.slack.api.bolt.App;
import listeners.ListenerProvider;

public class CommandListeners implements ListenerProvider {
    @Override
    public void register(App app) {
        app.command("/sample-command", new SampleCommandListener());
    }
}
