package listeners.shortcuts;

import static com.slack.api.model.block.Blocks.*;
import static com.slack.api.model.block.composition.BlockCompositions.*;
import static com.slack.api.model.block.element.BlockElements.plainTextInput;
import static com.slack.api.model.block.element.BlockElements.staticSelect;
import static com.slack.api.model.view.Views.*;

import com.slack.api.bolt.App;
import com.slack.api.bolt.context.builtin.GlobalShortcutContext;
import com.slack.api.bolt.handler.builtin.GlobalShortcutHandler;
import com.slack.api.bolt.request.builtin.GlobalShortcutRequest;
import com.slack.api.bolt.response.Response;
import com.slack.api.methods.SlackApiException;
import com.slack.api.model.view.View;
import java.io.IOException;
import utils.Constants;

public class SampleGlobalShortcutListener implements GlobalShortcutHandler {

    private final App app;

    public SampleGlobalShortcutListener(App app) {
        this.app = app;
    }

    @Override
    public Response apply(GlobalShortcutRequest req, GlobalShortcutContext ctx) throws IOException, SlackApiException {
        this.app.executorService().submit(() -> {
            // run the main logic asynchronously
            try {
                var viewsOpenResponse = ctx.client()
                        .viewsOpen(r -> r.triggerId(ctx.getTriggerId()).view(buildView()));
                if (!viewsOpenResponse.isOk()) {
                    ctx.logger.error(viewsOpenResponse.toString());
                }
            } catch (Exception e) {
                ctx.logger.error("Failed to call views.open API (error: {})", e.getMessage(), e);
            }
        });
        // immediately acknowledge the request
        return ctx.ack();
    }

    View buildView() {
        return view(view -> view.callbackId(Constants.CallbackIds.MEETING_ARRANGEMENT)
                .type("modal")
                .notifyOnClose(true)
                .title(viewTitle(title ->
                        title.type("plain_text").text("Meeting Arrangement").emoji(true)))
                .submit(viewSubmit(
                        submit -> submit.type("plain_text").text("Submit").emoji(true)))
                .close(viewClose(
                        close -> close.type("plain_text").text("Cancel").emoji(true)))
                .privateMetadata("{}")
                .blocks(asBlocks(
                        section(section -> section.blockId(Constants.BlockIds.CATEGORY)
                                .text(markdownText("Select a category of the meeting!"))
                                .accessory(staticSelect(staticSelect -> staticSelect
                                        .actionId(Constants.ActionIds.CATEGORY)
                                        .placeholder(plainText("Select a category"))
                                        .options(asOptions(
                                                option(plainText("Customer"), "customer"),
                                                option(plainText("Partner"), "partner"),
                                                option(plainText("Internal"), "internal")))))),
                        input(input -> input.blockId(Constants.BlockIds.AGENDA)
                                .element(plainTextInput(pti ->
                                        pti.actionId(Constants.ActionIds.AGENDA).multiline(true)))
                                .label(plainText(
                                        pt -> pt.text("Detailed Agenda").emoji(true)))))));
    }
}
