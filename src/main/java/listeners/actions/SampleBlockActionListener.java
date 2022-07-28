package listeners.actions;

import static com.slack.api.model.block.Blocks.*;
import static com.slack.api.model.block.composition.BlockCompositions.markdownText;
import static com.slack.api.model.block.composition.BlockCompositions.plainText;
import static com.slack.api.model.block.element.BlockElements.plainTextInput;
import static com.slack.api.model.view.Views.*;

import com.google.gson.Gson;
import com.slack.api.app_backend.interactive_components.payload.BlockActionPayload;
import com.slack.api.bolt.context.builtin.ActionContext;
import com.slack.api.bolt.handler.builtin.BlockActionHandler;
import com.slack.api.bolt.request.builtin.BlockActionRequest;
import com.slack.api.bolt.response.Response;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.response.views.ViewsUpdateResponse;
import com.slack.api.model.view.View;
import com.slack.api.util.json.GsonFactory;
import java.io.IOException;
import java.util.Map;

public class SampleBlockActionListener implements BlockActionHandler {

    @Override
    public Response apply(BlockActionRequest req, ActionContext ctx) throws IOException, SlackApiException {
        BlockActionPayload payload = req.getPayload();

        String categoryId = payload.getActions().get(0).getSelectedOption().getValue();
        View currentView = payload.getView();

        String privateMetadata = currentView.getPrivateMetadata();
        View viewForTheCategory = buildViewByCategory(categoryId, privateMetadata);

        ViewsUpdateResponse viewsUpdateResp = ctx.client().viewsUpdate(r -> r.viewId(currentView.getId())
                .hash(currentView.getHash())
                .view(viewForTheCategory));

        if (!viewsUpdateResp.isOk()) {
            ctx.logger.error(viewsUpdateResp.toString());
        }

        return ctx.ack();
    }

    View buildViewByCategory(String categoryId, String privateMetadata) {
        Gson gson = GsonFactory.createSnakeCase();
        Map<String, String> metadata = gson.fromJson(privateMetadata, Map.class);
        metadata.put("categoryId", categoryId);
        String updatedPrivateMetadata = gson.toJson(metadata);
        return view(view -> view.callbackId("meeting-arrangement")
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
                        section(section -> section.blockId("category-block")
                                .text(markdownText("You've selected \"" + categoryId + "\""))),
                        input(input -> input.blockId("agenda-block")
                                .element(plainTextInput(
                                        pti -> pti.actionId("agenda-action").multiline(true)))
                                .label(plainText(
                                        pt -> pt.text("Detailed Agenda").emoji(true)))))));
    }
}
