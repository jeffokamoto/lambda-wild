package com.gaminggrunts;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.ArgumentMatchers.anyString;


@ExtendWith(MockitoExtension.class)//without this runner - mocks would be "null"
@MockitoSettings(strictness = Strictness.LENIENT)
public class LambdaRequestHandlerTest {
    private HandlerStream handler;
    @Mock
    Context context;
    @Mock
    LambdaLogger loggerMock;

    @BeforeEach
    public void setUp() throws Exception {
        when(context.getLogger()).thenReturn(loggerMock);

        doAnswer(call -> {
            System.out.println((String)call.getArgument(0));//print to the console
            return null;
        }).when(loggerMock).log(anyString());

        handler = new HandlerStream();
    }

    @Test
    public void handleRequest() throws IOException {
        String respond = handler.handleRequest("3D6", context);
        assertTrue(respond.startsWith("Die Roll for 3D6:"));
    }
}