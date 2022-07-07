package listeners.commands;

import com.slack.api.bolt.context.builtin.SlashCommandContext;
import com.slack.api.bolt.handler.builtin.SlashCommandHandler;
import com.slack.api.bolt.request.builtin.SlashCommandRequest;
import com.slack.api.bolt.response.Response;

public class SampleCommandListener implements SlashCommandHandler {

    @Override
    public Response apply(SlashCommandRequest req, SlashCommandContext ctx) {
        ctx.logger.info("Hello command responding");
        return ctx.ack(":wave: Hello!");
    }
}
