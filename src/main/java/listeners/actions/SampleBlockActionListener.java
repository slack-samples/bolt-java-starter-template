package listeners.actions;

import static com.slack.api.model.block.Blocks.*;
import static com.slack.api.model.block.composition.BlockCompositions.markdownText;
import static com.slack.api.model.block.composition.BlockCompositions.plainText;
import static com.slack.api.model.block.element.BlockElements.plainTextInput;
import static com.slack.api.model.view.Views.*;

import com.google.gson.Gson;
import com.slack.api.bolt.App;
import com.slack.api.bolt.context.builtin.ActionContext;
import com.slack.api.bolt.handler.builtin.BlockActionHandler;
import com.slack.api.bolt.request.builtin.BlockActionRequest;
import com.slack.api.bolt.response.Response;
import com.slack.api.methods.SlackApiException;
import com.slack.api.model.view.View;
import com.slack.api.util.json.GsonFactory;
import java.io.IOException;
import utils.Constants;
import utils.MeetingArrangement;

public class SampleBlockActionListener implements BlockActionHandler {
    private final App app;
    private final Gson gson;

    public SampleBlockActionListener(App app) {
        this.app = app;
        this.gson = GsonFactory.createSnakeCase(app.getSlack().getConfig());
    }

    @Override
    public Response apply(BlockActionRequest req, ActionContext ctx) throws IOException, SlackApiException {
        this.app.executorService().submit(() -> {
            // run the main logic asynchronously
            var payload = req.getPayload();
            var categoryId = payload.getActions().get(0).getSelectedOption().getValue();
            var currentView = payload.getView();

            var privateMetadata = currentView.getPrivateMetadata();
            var viewForTheCategory = buildViewByCategory(categoryId, privateMetadata);

            try {
                var viewsUpdateResponse = ctx.client().viewsUpdate(r -> r.viewId(currentView.getId())
                        .hash(currentView.getHash())
                        .view(viewForTheCategory));
                if (!viewsUpdateResponse.isOk()) {
                    ctx.logger.error(viewsUpdateResponse.toString());
                }
            } catch (Exception e) {
                ctx.logger.error("Failed to call views.update API (error: {})", e.getMessage(), e);
            }
        });
        // immediately acknowledge the request
        return ctx.ack();
    }

    private View buildViewByCategory(String categoryId, String privateMetadata) {
        var metadata = gson.fromJson(privateMetadata, MeetingArrangement.class);
        metadata.setCategoryId(categoryId);
        var updatedPrivateMetadata = gson.toJson(metadata);
        return view(view -> view.callbackId(Constants.CallbackIds.MEETING_ARRANGEMENT)
                .type("modal")
                .notifyOnClose(true)
                .title(viewTitle(title ->
                        title.type("plain_text").text("Meeting Arrangement").emoji(true)))
                .submit(viewSubmit(
                        submit -> submit.type("plain_text").text("Submit").emoji(true)))
                .close(viewClose(
                        close -> close.type("plain_text").text("Cancel").emoji(true)))
                .privateMetadata(updatedPrivateMetadata)
                .blocks(asBlocks(
                        section(section -> section.blockId(Constants.BlockIds.CATEGORY)
                                .text(markdownText("You've selected \"" + categoryId + "\""))),
                        input(input -> input.blockId(Constants.BlockIds.AGENDA)
                                .element(plainTextInput(pti ->
                                        pti.actionId(Constants.ActionIds.AGENDA).multiline(true)))
                                .label(plainText(
                                        pt -> pt.text("Detailed Agenda").emoji(true)))))));
    }
}
