package listeners.views;

import com.slack.api.bolt.App;
import listeners.ListenerProvider;

public class ViewListeners implements ListenerProvider {
    @Override
    public void register(App app) {
        app.viewSubmission("meeting-arrangement", new SampleViewListener());
    }
}
