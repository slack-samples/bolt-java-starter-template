package listeners.events;

import static com.slack.api.model.block.Blocks.asBlocks;
import static com.slack.api.model.block.Blocks.section;
import static com.slack.api.model.block.composition.BlockCompositions.markdownText;
import static com.slack.api.model.view.Views.view;

import com.slack.api.app_backend.events.payload.EventsApiPayload;
import com.slack.api.bolt.App;
import com.slack.api.bolt.context.builtin.EventContext;
import com.slack.api.bolt.handler.BoltEventHandler;
import com.slack.api.bolt.response.Response;
import com.slack.api.methods.SlackApiException;
import com.slack.api.model.event.AppHomeOpenedEvent;
import java.io.IOException;
import java.time.ZonedDateTime;

public class AppHomeOpenedListener implements BoltEventHandler<AppHomeOpenedEvent> {

    private final App app;

    public AppHomeOpenedListener(App app) {
        this.app = app;
    }

    @Override
    public Response apply(EventsApiPayload<AppHomeOpenedEvent> payload, EventContext ctx)
            throws IOException, SlackApiException {
        this.app.executorService().submit(() -> {
            // run the main logic asynchronously
            var now = ZonedDateTime.now();
            var appHomeView = view(view -> view.type("home")
                    .blocks(asBlocks(section(section -> section.text(
                            markdownText(mt -> mt.text(":wave: Hello, App Home! (Last updated: " + now + ")")))))));
            try {
                var viewsPublishResponse = ctx.client()
                        .viewsPublish(
                                r -> r.userId(payload.getEvent().getUser()).view(appHomeView));
                if (!viewsPublishResponse.isOk()) {
                    ctx.logger.error(viewsPublishResponse.toString());
                }
            } catch (Exception e) {
                ctx.logger.error("Failed to call views.publish API (error: {})", e.getMessage(), e);
            }
        });
        // immediately acknowledge the request
        return ctx.ack();
    }
}
