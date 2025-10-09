package listeners.events;

import com.slack.api.app_backend.events.payload.EventsApiPayload;
import com.slack.api.bolt.App;
import com.slack.api.bolt.context.builtin.EventContext;
import com.slack.api.bolt.handler.BoltEventHandler;
import com.slack.api.bolt.response.Response;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatUnfurlRequest;
import com.slack.api.methods.request.chat.ChatUnfurlRequest.UnfurlMetadata;
import com.slack.api.model.EntityMetadata;
import com.slack.api.model.EntityMetadata.EntityPayload;
import com.slack.api.model.EntityMetadata.EntityPayload.FileFields;
import com.slack.api.model.event.LinkSharedEvent;
import java.io.IOException;

public class LinkSharedListener implements BoltEventHandler<LinkSharedEvent> {

    private final App app;

    public LinkSharedListener(App app) {
        this.app = app;
    }

    @Override
    public Response apply(EventsApiPayload<LinkSharedEvent> payload, EventContext ctx)
            throws IOException, SlackApiException {
        this.app.executorService().submit(() -> {

            // Prepare some work object metadata

            EntityPayload.Attributes.Title title =
                    EntityPayload.Attributes.Title.builder().text("My File").build();
            EntityPayload.Attributes attributes =
                    EntityPayload.Attributes.builder().title(title).build();
            EntityPayload.Timestamp dateCreated =
                    EntityPayload.Timestamp.builder().value(1756166400).build();
            FileFields fields = FileFields.builder().dateCreated(dateCreated).build();
            EntityPayload entityPayload = EntityPayload.builder()
                    .attributes(attributes)
                    .fileFields(fields)
                    .build();
            EntityMetadata.ExternalRef externalRef =
                    EntityMetadata.ExternalRef.builder().id("F123456").build();
            EntityMetadata entity = EntityMetadata.builder()
                    .entityType("slack#/entities/file")
                    .appUnfurlUrl("https://tmpdomain.com")
                    .url("https://tmpdomain.com")
                    .externalRef(externalRef)
                    .entityPayload(entityPayload)
                    .build();
            EntityMetadata[] entities = {entity};
            UnfurlMetadata metadata =
                    UnfurlMetadata.builder().entities(entities).build();
            ChatUnfurlRequest request = ChatUnfurlRequest.builder()
                    .token(ctx.getBotToken())
                    .channel(ctx.getChannelId())
                    .ts(payload.getEvent().getMessageTs())
                    .metadata(metadata)
                    .build();
            try {
                var response = ctx.client().chatUnfurl(request);
                if (!response.isOk()) {
                    ctx.logger.error(response.toString());
                }
            } catch (Exception e) {
                ctx.logger.error("Failed to call API (error: {})", e.getMessage(), e);
            }
        });
        // immediately acknowledge the request
        return ctx.ack();
    }
}
