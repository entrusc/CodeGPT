package ee.carlrobert.codegpt.completions.ollama;

import ee.carlrobert.codegpt.completions.ConversationType;
import ee.carlrobert.codegpt.conversations.message.Message;
import ee.carlrobert.llm.completion.CompletionRequest;

import java.util.List;

public class OllamaCompletionRequest implements CompletionRequest {

    private String model;
    private String systemPrompt;

    private String userPrompt;

    private List<Message> history;

    private int maxTokens;

    private double temperature;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSystemPrompt() {
        return systemPrompt;
    }

    public void setSystemPrompt(String systemPrompt) {
        this.systemPrompt = systemPrompt;
    }

    public String getUserPrompt() {
        return userPrompt;
    }

    public void setUserPrompt(String userPrompt) {
        this.userPrompt = userPrompt;
    }

    public List<Message> getHistory() {
        return history;
    }

    public void setHistory(List<Message> history) {
        this.history = history;
    }

    public int getMaxTokens() {
        return maxTokens;
    }

    public void setMaxTokens(int maxTokens) {
        this.maxTokens = maxTokens;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }
}
