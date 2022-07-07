package listeners.actions;

import com.slack.api.bolt.App;
import listeners.ListenerProvider;

public class ActionListeners implements ListenerProvider {
    @Override
    public void register(App app) {
        app.blockAction("category-selection-action", new SampleBlockActionListener());
    }
}
