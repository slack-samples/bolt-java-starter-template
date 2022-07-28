package listeners.shortcuts;

import static com.slack.api.model.block.Blocks.*;
import static com.slack.api.model.block.composition.BlockCompositions.*;
import static com.slack.api.model.block.element.BlockElements.plainTextInput;
import static com.slack.api.model.block.element.BlockElements.staticSelect;
import static com.slack.api.model.view.Views.*;

import com.slack.api.bolt.context.builtin.GlobalShortcutContext;
import com.slack.api.bolt.handler.builtin.GlobalShortcutHandler;
import com.slack.api.bolt.request.builtin.GlobalShortcutRequest;
import com.slack.api.bolt.response.Response;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.response.views.ViewsOpenResponse;
import com.slack.api.model.view.View;
import java.io.IOException;

public class SampleGlobalShortcutListener implements GlobalShortcutHandler {

    @Override
    public Response apply(GlobalShortcutRequest req, GlobalShortcutContext ctx) throws IOException, SlackApiException {

        ViewsOpenResponse viewsOpenResponse =
                ctx.client().viewsOpen(r -> r.triggerId(ctx.getTriggerId()).view(buildView()));

        if (!viewsOpenResponse.isOk()) {
            ctx.logger.error(viewsOpenResponse.toString());
        }

        return ctx.ack();
    }

    View buildView() {
        return view(view -> view.callbackId("meeting-arrangement")
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
                        section(section -> section.blockId("category-block")
                                .text(markdownText("Select a category of the meeting!"))
                                .accessory(staticSelect(staticSelect -> staticSelect
                                        .actionId("category-selection-action")
                                        .placeholder(plainText("Select a category"))
                                        .options(asOptions(
                                                option(plainText("Customer"), "customer"),
                                                option(plainText("Partner"), "partner"),
                                                option(plainText("Internal"), "internal")))))),
                        input(input -> input.blockId("agenda-block")
                                .element(plainTextInput(
                                        pti -> pti.actionId("agenda-action").multiline(true)))
                                .label(plainText(
                                        pt -> pt.text("Detailed Agenda").emoji(true)))))));
    }
}
