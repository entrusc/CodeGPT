package ee.carlrobert.codegpt.settings.state;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import ee.carlrobert.codegpt.settings.service.ServiceSelectionForm;
import org.jetbrains.annotations.NotNull;

@State(name = "CodeGPT_OllamaSettings", storages = @Storage("CodeGPT_OllamaSettings.xml"))
public class OllamaSettingsState implements PersistentStateComponent<OllamaSettingsState>  {

    private String baseHost = "";


    public OllamaSettingsState() {
    }

    public void apply(ServiceSelectionForm serviceSelectionForm) {
        var ollamaForm = serviceSelectionForm.getOllamaServiceSelectionForm();
        setBaseHost(ollamaForm.getBaseHost());
    }

    public String getBaseHost() {
        return baseHost;
    }

    public void setBaseHost(String baseHost) {
        this.baseHost = baseHost;
    }

    @Override
    public OllamaSettingsState getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull OllamaSettingsState state) {
        XmlSerializerUtil.copyBean(state, this);
    }


    public static OllamaSettingsState getInstance() {
        return ApplicationManager.getApplication().getService(OllamaSettingsState.class);
    }
}
