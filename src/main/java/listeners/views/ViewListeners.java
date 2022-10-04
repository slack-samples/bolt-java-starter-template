package listeners.views;

import com.slack.api.bolt.App;
import listeners.ListenerProvider;
import utils.Constants;

public class ViewListeners implements ListenerProvider {
    @Override
    public void register(App app) {
        app.viewSubmission(Constants.CallbackIds.MEETING_ARRANGEMENT, new SampleViewListener());
    }
}
