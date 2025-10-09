package listeners.events;

import com.slack.api.bolt.App;
import com.slack.api.model.event.AppHomeOpenedEvent;
import com.slack.api.model.event.EntityDetailsRequestedEvent;
import com.slack.api.model.event.LinkSharedEvent;
import listeners.ListenerProvider;

public class EventListeners implements ListenerProvider {
    @Override
    public void register(App app) {
        app.event(AppHomeOpenedEvent.class, new AppHomeOpenedListener(app));
        app.event(EntityDetailsRequestedEvent.class, new EntityDetailsRequestedListener(app));
        app.event(LinkSharedEvent.class, new LinkSharedListener(app));
    }
}
