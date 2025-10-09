package listeners.events;

import com.slack.api.app_backend.events.payload.EventsApiPayload;
import com.slack.api.bolt.App;
import com.slack.api.bolt.context.builtin.EventContext;
import com.slack.api.bolt.handler.BoltEventHandler;
import com.slack.api.bolt.response.Response;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.entity.EntityPresentDetailsRequest;
import com.slack.api.model.event.EntityDetailsRequestedEvent;
import java.io.IOException;

public class EntityDetailsRequestedListener implements BoltEventHandler<EntityDetailsRequestedEvent> {

    private final App app;

    public EntityDetailsRequestedListener(App app) {
        this.app = app;
    }

    @Override
    public Response apply(EventsApiPayload<EntityDetailsRequestedEvent> payload, EventContext ctx)
            throws IOException, SlackApiException {
        this.app.executorService().submit(() -> {

            // Prepare some work object metadata

            String iconURL =
                    "https://cdn.shopify.com/s/files/1/0156/3796/files/shutterstock_54266797_large.jpg?v=1549042211";
            String jsonString =
                    "{\"entity_type\":\"slack#/entities/task\",\"external_ref\":{\"id\":\"94\"},\"url\":\"https://tmpdomain.com/admin/slack/workobject/94/change/\",\"entity_payload\":{\"attributes\":{\"title\":{\"text\":\"Default title\"},\"display_type\":\"Assignment\",\"product_icon\":{\"url\":\"https://play-lh.googleusercontent.com/DG-zbXPr8LItYD8F2nD4aR_SK_jpkipLBK77YWY-F0cdJt67VFgCHZtRtjsakzTw3EM=w480-h960-rw\",\"alt_text\":\"The product's icon\"},\"product_name\":\"My Product\"},\"fields\":{\"description\":{\"value\":\"Description of the task\",\"format\":\"markdown\"},\"date_created\":{\"value\":1741164235},\"date_updated\":{\"value\":1741164235},\"assignee\":{\"type\":\"slack#/types/user_id\",\"value\":\""
                            + "U12345"
                            + "\"},\"status\":{\"value\":\"open\",\"tag_color\":\"blue\",\"link\":\"https://example.com/tasks?status=open\"},\"due_date\":{\"value\":\"2025-06-10\",\"type\":\"slack#/types/date\"},\"priority\":{\"value\":\"high\",\"icon\":{\"alt_text\":\"Icon to indicate a high priority item\",\"url\":\""
                            + iconURL
                            + "\"},\"link\":\"https://example.com/tasks?priority=high\"}},\"custom_fields\":[{\"key\":\"channels\",\"label\":\"channels\",\"item_type\":\"slack#/types/channel_id\",\"type\":\"array\",\"value\":[{\"value\":\""
                            + "C12345" + "\",\"type\":\"slack#/types/channel_id\"},{\"value\":\""
                            + "C12345"
                            + "\",\"type\":\"slack#/types/channel_id\"}]},{\"key\":\"channel\",\"label\":\"Channel\",\"value\":\""
                            + "C12345"
                            + "\",\"type\":\"slack#/types/channel_id\"},{\"key\":\"img\",\"label\":\"image\",\"type\":\"slack#/types/image\",\"image_url\":\"https://previews.us-east-1.widencdn.net/preview/48045879/assets/asset-view/8588de84-f8ed-4488-a456-45ba986280ee/thumbnail/eyJ3IjoyMDQ4LCJoIjoyMDQ4LCJzY29wZSI6ImFwcCJ9?sig.ver=1&sig.keyId=us-east-1.20240821&sig.expires=1759892400&sig=UgMe4SFiG6i3OP7mWd-ZX61Kx4uobjTmOuZqHjCV2QY\"},{\"type\":\"string\",\"key\":\"item_static_sel\",\"label\":\"Static Select\",\"value\":\"Red\"},{\"type\":\"array\",\"key\":\"multi_static_select_key\",\"label\":\"Multi Static Select\",\"value\":[{\"value\":\"Green\",\"type\":\"string\",\"tag_color\":\"gray\"}],\"item_type\":\"string\"},{\"type\":\"slack#/types/user_id\",\"key\":\"user_select_key\",\"label\":\"User select\",\"value\":\"USLACKBOT\",\"edit\":{\"enabled\":true}},{\"type\":\"array\",\"key\":\"string_array_key\",\"label\":\"Array of markdown strings\",\"item_type\":\"string\",\"value\":[{\"value\":\"__Thing 1__\",\"type\":\"string\",\"format\":\"markdown\"},{\"value\":\"*Thing 2*\",\"type\":\"string\",\"format\":\"markdown\"}],\"edit\":{\"enabled\":true}},{\"type\":\"array\",\"key\":\"string_array_2_key\",\"label\":\"Array of plain strings\",\"item_type\":\"string\",\"value\":[{\"value\":\"Plain string 1\",\"type\":\"string\"},{\"value\":\"Plain string 2\",\"type\":\"string\"}]},{\"type\":\"array\",\"key\":\"multi_user_select_key\",\"label\":\"Multi-user select\",\"item_type\":\"slack#/types/user_id\",\"value\":[{\"value\":\"USLACKBOT\",\"type\":\"slack#/types/user_id\"},{\"value\":\"U014KLZE350\",\"type\":\"slack#/types/user_id\"}]},{\"type\":\"array\",\"key\":\"multi_external_select_key\",\"label\":\"Multi External Select\",\"item_type\":\"string\",\"value\":[{\"value\":\"Jose Allen\",\"type\":\"string\",\"tag_color\":\"gray\"},{\"value\":\"Cristian Santos\",\"type\":\"string\",\"tag_color\":\"gray\"},{\"value\":\"Wayne Morgan\",\"type\":\"string\",\"tag_color\":\"gray\"}],\"edit\":{\"enabled\":true,\"select\":{\"fetch_options_dynamically\":true,\"current_values\":[\"helen-jones\",\"cristian-santos\",\"wayne-morgan\"]}}},{\"type\":\"string\",\"key\":\"external_select_key\",\"label\":\"External Select\",\"value\":\"Kevin Walters\",\"tag_color\":\"gray\",\"edit\":{\"enabled\":true,\"select\":{\"current_value\":\"kevin-walters\",\"fetch_options_dynamically\":true}}},{\"type\":\"slack#/types/timestamp\",\"key\":\"timestamp_key\",\"label\":\"Timestamp\",\"value\":\"1747496700\",\"edit\":{\"enabled\":true}}],\"actions\":{\"primary_actions\":[{\"text\":\"URL action\",\"action_id\":\"url_button_action_id\",\"value\":\"some_val\",\"style\":\"primary\",\"url\":\"https://example.com\",\"accessibility_label\":\"Goes to example.com\"},{\"text\":\"Block action\",\"action_id\":\"block_action_id\",\"value\":\"some_val\",\"style\":\"danger\",\"accessibility_label\":\"No URL so this should be invoked via blocks.actions\"}],\"overflow_actions\":[{\"text\":\"First overflow action\",\"action_id\":\"overflow_block_action_id\",\"value\":\"overflow\",\"style\":\"primary\",\"accessibility_label\":\"No URL so this should be invoked via blocks.actions\"},{\"text\":\"Second overflow action\",\"action_id\":\"second_block_action_id\",\"value\":\"overflow\",\"style\":\"danger\",\"url\":\"https://example.com\",\"accessibility_label\":\" URL \"}]}},\"app_unfurl_url\":\"https://tmpdomain.com/id/123?url-posted-by=user\"}";

            EntityPresentDetailsRequest request = EntityPresentDetailsRequest.builder()
                    .token(ctx.getBotToken())
                    .triggerId(payload.getEvent().getTriggerId())
                    .rawMetadata(jsonString)
                    .build();
            try {
                var response = ctx.client().entityPresentDetails(request);
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
