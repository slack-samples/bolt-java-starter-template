package listeners.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import com.slack.api.bolt.context.builtin.SlashCommandContext;
import com.slack.api.bolt.request.builtin.SlashCommandRequest;
import com.slack.api.bolt.response.Response;
import org.junit.jupiter.api.Test;

public class SampleMessageListenerTest {

    @Test
    public void testApply() {
        // Given
        SlashCommandRequest reqMock = mock(SlashCommandRequest.class);
        SlashCommandContext ctxMock = spy(SlashCommandContext.class);

        // When
        SampleCommandListener sampleCommandCallback = new SampleCommandListener();
        Response res = sampleCommandCallback.apply(reqMock, ctxMock);

        // Then
        assertEquals(res.getBody(), "{\"text\":\":wave: Hello!\"}");
    }
}
