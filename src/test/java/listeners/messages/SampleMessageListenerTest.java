package listeners.messages;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.slack.api.app_backend.events.payload.EventsApiPayload;
import com.slack.api.bolt.App;
import com.slack.api.bolt.context.builtin.EventContext;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.model.event.MessageEvent;
import org.junit.jupiter.api.Test;

public class SampleMessageListenerTest {

    final String greeting = "hi";

    @Test
    public void testApply() throws Exception {
        // Given
        var msgEventMock = mock(MessageEvent.class);
        when(msgEventMock.getText()).thenReturn(greeting);

        var resMock = new ChatPostMessageResponse();
        resMock.setOk(true);

        var plMock = (EventsApiPayload<MessageEvent>) mock(EventsApiPayload.class);
        var ctxMock = mock(EventContext.class);

        when(plMock.getEvent()).thenReturn(msgEventMock);
        when(ctxMock.say(any(String.class))).thenReturn(resMock);

        // When
        var app = new App();
        var sampleMessageCallback = new SampleMessageListener(app);
        var res = sampleMessageCallback.apply(plMock, ctxMock);

        Thread.sleep(300L); // for test stability

        // Then
        verify(ctxMock).say(greeting + " :wave:");
        assertEquals(res.getStatusCode(), 200);
    }
}
