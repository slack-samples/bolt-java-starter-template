package listeners.events;

import com.slack.api.bolt.App;
import com.slack.api.model.event.AppHomeOpenedEvent;
import listeners.ListenerProvider;

public class EventListeners implements ListenerProvider {
    @Override
    public void register(App app) {
        app.event(AppHomeOpenedEvent.class, new AppHomeOpenedListener(app));
    }
}
