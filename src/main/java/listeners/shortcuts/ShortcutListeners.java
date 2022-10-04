package listeners.shortcuts;

import com.slack.api.bolt.App;
import listeners.ListenerProvider;

public class ShortcutListeners implements ListenerProvider {
    @Override
    public void register(App app) {
        app.globalShortcut("sample-shortcut-id", new SampleGlobalShortcutListener(app));
    }
}
