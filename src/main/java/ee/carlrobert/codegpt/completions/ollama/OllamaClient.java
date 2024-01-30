package ee.carlrobert.codegpt.completions.ollama;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ee.carlrobert.llm.client.llama.completion.LlamaCompletionRequest;
import ee.carlrobert.llm.client.llama.completion.LlamaCompletionResponse;
import ee.carlrobert.llm.client.openai.completion.ErrorDetails;
import ee.carlrobert.llm.client.openai.completion.OpenAIChatCompletionEventSourceListener;
import ee.carlrobert.llm.client.openai.completion.OpenAICompletionRequest;
import ee.carlrobert.llm.completion.CompletionEventListener;
import ee.carlrobert.llm.completion.CompletionEventSourceListener;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSources;

public class OllamaClient {

    private final OkHttpClient httpClient;

    public OllamaClient(OkHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public EventSource getChatCompletionAsync(OllamaCompletionRequest request, CompletionEventListener eventListener) {
        return EventSources.createFactory(httpClient).newEventSource(buildCompletionRequest(request), getEventSourceListener(eventListener));
    }

    private Request buildCompletionRequest(OllamaCompletionRequest request) {
        try {
            String baseHost = this.port == null ? BASE_URL : String.format("http://localhost:%d", this.port);
            return (new Request.Builder()).url(this.host == null ? baseHost + "/completion" : this.host).header("Cache-Control", "no-cache").header("Content-Type", "application/json").header("Accept", request.isStream() ? "text/event-stream" : "text/json").post(RequestBody.create((new ObjectMapper()).writeValueAsString(request), MediaType.parse("application/json"))).build();
        } catch (JsonProcessingException var3) {
            throw new RuntimeException(var3);
        }
    }

    private CompletionEventSourceListener getEventSourceListener(CompletionEventListener eventListener) {
        return new CompletionEventSourceListener(eventListener) {
            protected String getMessage(String data) {
                try {
                    //TODO: adjust to use Ollama

                    LlamaCompletionResponse response = (LlamaCompletionResponse)(new ObjectMapper()).readValue(data, LlamaCompletionResponse.class);
                    return response.getContent();
                } catch (JacksonException var3) {
                    return "";
                }
            }

            protected ErrorDetails getErrorDetails(String error) {
                return new ErrorDetails(error);
            }
        };
    }
}
