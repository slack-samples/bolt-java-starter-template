package listeners.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import com.slack.api.bolt.context.builtin.SlashCommandContext;
import com.slack.api.bolt.request.builtin.SlashCommandRequest;
import org.junit.jupiter.api.Test;

public class SampleCommandListenerTest {

    @Test
    public void testApply() {
        // Given
        var reqMock = mock(SlashCommandRequest.class);
        var ctxMock = spy(SlashCommandContext.class);

        // When
        var sampleCommandCallback = new SampleCommandListener();
        var res = sampleCommandCallback.apply(reqMock, ctxMock);

        // Then
        assertEquals(res.getBody(), "{\"text\":\":wave: Hello!\"}");
    }
}
