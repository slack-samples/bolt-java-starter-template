package listeners.views;

import com.slack.api.bolt.context.builtin.ViewSubmissionContext;
import com.slack.api.bolt.handler.builtin.ViewSubmissionHandler;
import com.slack.api.bolt.request.builtin.ViewSubmissionRequest;
import com.slack.api.bolt.response.Response;
import java.util.HashMap;
import utils.Constants;

public class SampleViewListener implements ViewSubmissionHandler {

    @Override
    public Response apply(ViewSubmissionRequest req, ViewSubmissionContext ctx) {
        // For view submission handling, calling ack() within 3 seconds is recommended
        // It's still possible to use app.executorService() for async code execution,
        // but with that, you cannot respond with errors in the case of validation errors.

        var view = req.getPayload().getView();
        var privateMetadata = view.getPrivateMetadata();
        ctx.logger.info(privateMetadata);

        var stateValues = view.getState().getValues();
        var agenda = stateValues
                .get(Constants.BlockIds.AGENDA)
                .get(Constants.ActionIds.AGENDA)
                .getValue();

        var errors = new HashMap<String, String>();
        if (agenda.length() <= 10) {
            errors.put(Constants.BlockIds.AGENDA, "Agenda needs to be longer than 10 characters.");
        }
        if (!errors.isEmpty()) {
            return ctx.ack(r -> r.responseAction("errors").errors(errors));
        }

        // Responding with an empty body means closing the modal now.
        // If your app has next steps, respond with other response_action and a modal view.
        return ctx.ack();
    }
}
