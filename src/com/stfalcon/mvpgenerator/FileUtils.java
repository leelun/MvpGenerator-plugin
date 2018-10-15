package com.stfalcon.mvpgenerator;

import com.intellij.ide.fileTemplates.*;
import java.util.*;
import com.intellij.openapi.project.*;
import com.intellij.openapi.vfs.*;
import com.intellij.psi.*;
import com.intellij.openapi.ui.popup.*;

public final class FileUtils
{
    public static String getSelectedDirectoryPath(final FileTemplateManager fileTemplateManager, final PsiDirectory directory) {
        final Properties properties = fileTemplateManager.getDefaultProperties();
        FileTemplateUtil.fillDefaultProperties(properties, directory);
        return properties.getProperty("PACKAGE_NAME");
    }
    
    public static PsiDirectory validateSelectedDirectory(final Project project, final VirtualFile targetFile) {
        final PsiManager psiManager = PsiManager.getInstance(project);
        if (targetFile != null) {
            if (targetFile.isDirectory()) {
                return psiManager.findDirectory(targetFile);
            }
            final PsiFile psiFile = psiManager.findFile(targetFile);
            if (psiFile != null) {
                return psiFile.getParent();
            }
            showError("You must select file or directory!", project);
        }
        else {
            showError("You must select file or directory!", project);
        }
        return null;
    }
    
    private static void showError(final String text, final Project project) {
        JBPopupFactory.getInstance().createMessage(text).showCenteredInCurrentWindow(project);
    }
}
