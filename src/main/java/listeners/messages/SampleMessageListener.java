package listeners.messages;

import com.slack.api.app_backend.events.payload.EventsApiPayload;
import com.slack.api.bolt.context.builtin.EventContext;
import com.slack.api.bolt.handler.BoltEventHandler;
import com.slack.api.bolt.response.Response;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.model.event.MessageEvent;
import java.io.IOException;

public class SampleMessageListener implements BoltEventHandler<MessageEvent> {

    @Override
    public Response apply(EventsApiPayload<MessageEvent> pl, EventContext ctx) throws IOException, SlackApiException {

        String message = pl.getEvent().getText() + " :wave:";
        ChatPostMessageResponse chatPostMessageResponse = ctx.say(message);

        if (!chatPostMessageResponse.isOk()) {
            ctx.logger.error(chatPostMessageResponse.toString());
        }

        return Response.ok();
    }
}
