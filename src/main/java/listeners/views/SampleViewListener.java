package listeners.views;

import com.slack.api.bolt.context.builtin.ViewSubmissionContext;
import com.slack.api.bolt.handler.builtin.ViewSubmissionHandler;
import com.slack.api.bolt.request.builtin.ViewSubmissionRequest;
import com.slack.api.bolt.response.Response;
import com.slack.api.model.view.View;
import com.slack.api.model.view.ViewState;
import java.util.HashMap;
import java.util.Map;

public class SampleViewListener implements ViewSubmissionHandler {

    @Override
    public Response apply(ViewSubmissionRequest req, ViewSubmissionContext ctx) {

        View view = req.getPayload().getView();
        String privateMetadata = view.getPrivateMetadata();
        ctx.logger.info(privateMetadata);

        Map<String, Map<String, ViewState.Value>> stateValues = view.getState().getValues();
        String agenda = stateValues.get("agenda-block").get("agenda-action").getValue();

        Map<String, String> errors = new HashMap<>();
        if (agenda.length() <= 10) {
            errors.put("agenda-block", "Agenda needs to be longer than 10 characters.");
        }
        if (!errors.isEmpty()) {
            return ctx.ack(r -> r.responseAction("errors").errors(errors));
        }
        // TODO: may store the stateValues and privateMetadata
        // Responding with an empty body means closing the modal now.
        // If your app has next steps, respond with other response_action and a modal view.
        return ctx.ack();
    }
}
