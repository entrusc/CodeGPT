package ee.carlrobert.codegpt.settings.service;

import com.intellij.ui.TitledSeparator;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.JBUI;
import ee.carlrobert.codegpt.CodeGPTBundle;
import ee.carlrobert.codegpt.settings.state.OllamaSettingsState;

import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.BorderLayout;

public class OllamaServiceSelectionForm extends JPanel  {

    private final JBTextField baseHostTextField = new JBTextField();

    public OllamaServiceSelectionForm() {
        init();
    }
    
    private void init() {
        setLayout(new BorderLayout());

        add(FormBuilder.createFormBuilder()
                .addComponent(new TitledSeparator(
                        CodeGPTBundle.get("settingsConfigurable.service.ollama.serverPreferences.title")))
                .addComponent(withEmptyLeftBorder(createServerPreferencesForm()))
                .addComponentFillVertically(new JPanel(), 0)
                .getPanel());

        loadSettings();
    }

    private void loadSettings() {
        OllamaSettingsState settings = OllamaSettingsState.getInstance();
        baseHostTextField.setText(settings.getBaseHost());
    }

    private JPanel createServerPreferencesForm() {
        return FormBuilder.createFormBuilder()
                .addLabeledComponent(
                        CodeGPTBundle.get("settingsConfigurable.service.ollama.serverPreferences.apiUrl"),
                        baseHostTextField)
                .getPanel();
    }

    public String getBaseHost() {
        return baseHostTextField.getText();
    }

    private JComponent withEmptyLeftBorder(JComponent component) {
        component.setBorder(JBUI.Borders.emptyLeft(16));
        return component;
    }
}
