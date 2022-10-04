package listeners.messages;

import com.slack.api.app_backend.events.payload.EventsApiPayload;
import com.slack.api.bolt.App;
import com.slack.api.bolt.context.builtin.EventContext;
import com.slack.api.bolt.handler.BoltEventHandler;
import com.slack.api.bolt.response.Response;
import com.slack.api.methods.SlackApiException;
import com.slack.api.model.event.MessageEvent;
import java.io.IOException;

public class SampleMessageListener implements BoltEventHandler<MessageEvent> {

    private final App app;

    public SampleMessageListener(App app) {
        this.app = app;
    }

    @Override
    public Response apply(EventsApiPayload<MessageEvent> payload, EventContext ctx)
            throws IOException, SlackApiException {
        this.app.executorService().submit(() -> {
            // run the main logic asynchronously
            var message = payload.getEvent().getText() + " :wave:";
            try {
                var postMessageResponse = ctx.say(message);
                if (!postMessageResponse.isOk()) {
                    ctx.logger.error(postMessageResponse.toString());
                }
            } catch (Exception e) {
                ctx.logger.error("Failed to call chat.postMessage API (error: {})", e.getMessage(), e);
            }
        });
        return Response.ok();
    }
}
