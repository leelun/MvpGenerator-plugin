package com.stfalcon.mvpgenerator;

import com.intellij.openapi.ui.*;
import javax.swing.*;
import com.intellij.ui.components.*;
import java.awt.*;
import org.jetbrains.annotations.*;

public class DialogComponentName extends DialogWrapper
{
    private JBTextField textArea;
    private String labelString;
    
    DialogComponentName(final String title, final String label) {
        super(false);
        this.labelString = label;
        this.init();
        this.setTitle(title);
    }
    
    @Nullable
    protected JComponent createCenterPanel() {
        final JBPanel panel = new JBPanel();
        panel.setLayout((LayoutManager)new GridLayout(2, 1));
        final JBLabel label = new JBLabel(this.labelString);
        this.textArea = new JBTextField();
        panel.add((Component)label, 0);
        panel.add((Component)this.textArea, 1);
        return (JComponent)panel;
    }
    
    @Nullable
    public JComponent getPreferredFocusedComponent() {
        return (JComponent)this.textArea;
    }
    
    String getComponentName() {
        return this.textArea.getText();
    }
}
