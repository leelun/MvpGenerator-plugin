package com.stfalcon.mvpgenerator;

import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.ide.fileTemplates.FileTemplateUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;

import java.util.Properties;

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
