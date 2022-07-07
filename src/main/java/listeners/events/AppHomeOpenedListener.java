package listeners.events;

import static com.slack.api.model.block.Blocks.asBlocks;
import static com.slack.api.model.block.Blocks.section;
import static com.slack.api.model.block.composition.BlockCompositions.markdownText;
import static com.slack.api.model.view.Views.view;

import com.slack.api.app_backend.events.payload.EventsApiPayload;
import com.slack.api.bolt.context.builtin.EventContext;
import com.slack.api.bolt.handler.BoltEventHandler;
import com.slack.api.bolt.response.Response;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.response.views.ViewsPublishResponse;
import com.slack.api.model.event.AppHomeOpenedEvent;
import com.slack.api.model.view.View;
import java.io.IOException;
import java.time.ZonedDateTime;

public class AppHomeOpenedListener implements BoltEventHandler<AppHomeOpenedEvent> {

    @Override
    public Response apply(EventsApiPayload<AppHomeOpenedEvent> pl, EventContext ctx)
            throws IOException, SlackApiException {
        ZonedDateTime now = ZonedDateTime.now();
        View appHomeView = view(view -> view.type("home")
                .blocks(asBlocks(section(section -> section.text(
                        markdownText(mt -> mt.text(":wave: Hello, App Home! (Last updated: " + now + ")")))))));

        ViewsPublishResponse viewsPubRes =
                ctx.client().viewsPublish(r -> r.userId(pl.getEvent().getUser()).view(appHomeView));

        if (!viewsPubRes.isOk()) {
            ctx.logger.error(viewsPubRes.toString());
        }

        return ctx.ack();
    }
}
