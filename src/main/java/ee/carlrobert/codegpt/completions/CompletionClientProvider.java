package ee.carlrobert.codegpt.completions;

import static java.lang.String.format;

import ee.carlrobert.codegpt.CodeGPTPlugin;
import ee.carlrobert.codegpt.completions.ollama.OllamaClient;
import ee.carlrobert.codegpt.completions.you.YouUserManager;
import ee.carlrobert.codegpt.credentials.AzureCredentialsManager;
import ee.carlrobert.codegpt.credentials.OpenAICredentialsManager;
import ee.carlrobert.codegpt.settings.advanced.AdvancedSettingsState;
import ee.carlrobert.codegpt.settings.state.AzureSettingsState;
import ee.carlrobert.codegpt.settings.state.LlamaSettingsState;
import ee.carlrobert.codegpt.settings.state.OpenAISettingsState;
import ee.carlrobert.llm.client.azure.AzureClient;
import ee.carlrobert.llm.client.azure.AzureCompletionRequestParams;
import ee.carlrobert.llm.client.llama.LlamaClient;
import ee.carlrobert.llm.client.openai.OpenAIClient;
import ee.carlrobert.llm.client.you.UTMParameters;
import ee.carlrobert.llm.client.you.YouClient;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;

public class CompletionClientProvider {

  public static OpenAIClient getOpenAIClient() {
    var settings = OpenAISettingsState.getInstance();
    var builder = new OpenAIClient.Builder(OpenAICredentialsManager.getInstance().getApiKey())
        .setOrganization(settings.getOrganization());
    var baseHost = settings.getBaseHost();
    if (baseHost != null) {
      builder.setHost(baseHost);
    }
    return builder.build(getDefaultClientBuilder());
  }

  public static AzureClient getAzureClient() {
    var settings = AzureSettingsState.getInstance();
    var params = new AzureCompletionRequestParams(
        settings.getResourceName(),
        settings.getDeploymentId(),
        settings.getApiVersion());
    var builder = new AzureClient.Builder(AzureCredentialsManager.getInstance().getSecret(), params)
        .setActiveDirectoryAuthentication(settings.isUseAzureActiveDirectoryAuthentication());
    var baseHost = settings.getBaseHost();
    if (baseHost != null) {
      builder.setUrl(format(baseHost, params.getResourceName()));
    }
    return builder.build();
  }

  public static YouClient getYouClient() {
    var utmParameters = new UTMParameters();
    utmParameters.setSource("ide");
    utmParameters.setMedium("jetbrains");
    utmParameters.setCampaign(CodeGPTPlugin.getVersion());
    utmParameters.setContent("CodeGPT");

    var sessionId = "";
    var accessToken = "";
    var youUserManager = YouUserManager.getInstance();
    if (youUserManager.isAuthenticated()) {
      var authenticationResponse = youUserManager.getAuthenticationResponse().getData();
      sessionId = authenticationResponse.getSession().getSessionId();
      accessToken = authenticationResponse.getSessionJwt();
    }

    return new YouClient.Builder(sessionId, accessToken)
        .setUTMParameters(utmParameters)
        .build();
  }

  public static LlamaClient getLlamaClient() {
    return new LlamaClient.Builder()
        .setPort(LlamaSettingsState.getInstance().getServerPort())
        .build(getDefaultClientBuilder());
  }

  public static OllamaClient getOllamaClient() {

  }

  private static OkHttpClient.Builder getDefaultClientBuilder() {
    OkHttpClient.Builder builder = new OkHttpClient.Builder();
    var advancedSettings = AdvancedSettingsState.getInstance();
    var proxyHost = advancedSettings.getProxyHost();
    var proxyPort = advancedSettings.getProxyPort();
    if (!proxyHost.isEmpty() && proxyPort != 0) {
      builder.proxy(
          new Proxy(advancedSettings.getProxyType(), new InetSocketAddress(proxyHost, proxyPort)));
      if (advancedSettings.isProxyAuthSelected()) {
        builder.proxyAuthenticator((route, response) ->
            response.request()
                .newBuilder()
                .header("Proxy-Authorization", Credentials.basic(
                    advancedSettings.getProxyUsername(),
                    advancedSettings.getProxyPassword()))
                .build());
      }
    }

    return builder
        .connectTimeout(advancedSettings.getConnectTimeout(), TimeUnit.SECONDS)
        .readTimeout(advancedSettings.getReadTimeout(), TimeUnit.SECONDS);
  }
}


