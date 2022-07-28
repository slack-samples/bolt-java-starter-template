package listeners.messages;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.slack.api.app_backend.events.payload.EventsApiPayload;
import com.slack.api.bolt.context.builtin.EventContext;
import com.slack.api.bolt.response.Response;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.model.event.MessageEvent;
import java.io.IOException;
import org.junit.jupiter.api.Test;

public class SampleMessageListenerTest {

    final String greeting = "hi";

    @Test
    public void testApply() throws SlackApiException, IOException {
        // Given
        MessageEvent msgEventMock = mock(MessageEvent.class);
        when(msgEventMock.getText()).thenReturn(greeting);

        ChatPostMessageResponse resMock = new ChatPostMessageResponse();
        resMock.setOk(true);

        EventsApiPayload<MessageEvent> plMock = (EventsApiPayload<MessageEvent>) mock(EventsApiPayload.class);
        EventContext ctxMock = mock(EventContext.class);

        when(plMock.getEvent()).thenReturn(msgEventMock);
        when(ctxMock.say(any(String.class))).thenReturn(resMock);

        // When
        SampleMessageListener sampleMessageCallback = new SampleMessageListener();
        Response res = sampleMessageCallback.apply(plMock, ctxMock);

        // Then
        verify(ctxMock).say(greeting + " :wave:");
        assertEquals(res.getStatusCode(), 200);
    }
}
