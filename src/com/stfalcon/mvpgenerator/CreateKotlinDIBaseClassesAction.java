package com.stfalcon.mvpgenerator;

import com.intellij.openapi.module.*;
import com.intellij.openapi.vfs.*;
import com.intellij.openapi.project.*;
import com.intellij.openapi.actionSystem.*;
import com.intellij.psi.*;
import com.intellij.ide.fileTemplates.*;
import java.util.*;

public class CreateKotlinDIBaseClassesAction extends AnAction
{
    private static final String NAME_DIR_ROOT = "di";
    private static final String NAME_DIR_COMPONENTS = "components";
    private static final String NAME_DIR_FACTORIES = "factories";
    private static final String NAME_DIR_MODULES = "modules";
    private static final String NAME_FILE_APP = "App";
    private static final String NAME_FILE_APP_COMPONENT = "AppComponent";
    private static final String NAME_FILE_APP_MODULE = "AppModule";
    private static final String NAME_FILE_ACTIVITIES_INJECTOR_FACTORIES = "ActivitiesInjectorFactories";
    private static final String NAME_FILE_FRAGMENTS_INJECTOR_FACTORIES = "FragmentsInjectorFactories";
    
    public void actionPerformed(final AnActionEvent e) {
        final Project project = e.getProject();
        if (project == null) {
            return;
        }
        final DataContext dataContext = e.getDataContext();
        final Module module = (Module)LangDataKeys.MODULE.getData(dataContext);
        if (module == null) {
            return;
        }
        final VirtualFile targetFile = (VirtualFile)CommonDataKeys.VIRTUAL_FILE.getData(dataContext);
        PsiDirectory psiDirectory = FileUtils.validateSelectedDirectory(project, targetFile);
        if (psiDirectory == null) {
            return;
        }
        psiDirectory = psiDirectory.createSubdirectory("di");
        this.create(psiDirectory, module);
    }
    
    private void create(final PsiDirectory directory, final Module module) {
        final FileTemplateManager fileTemplateManager = FileTemplateManager.getDefaultInstance();
        final String rootPackage = FileUtils.getSelectedDirectoryPath(fileTemplateManager, directory);
        this.createFile(directory, "App", rootPackage, fileTemplateManager, "App.kt");
        this.createFile(directory.createSubdirectory("components"), "AppComponent", rootPackage, fileTemplateManager, "AppComponent.kt");
        this.createFile(directory.createSubdirectory("modules"), "AppModule", rootPackage, fileTemplateManager, "AppModule.kt");
        this.createFile(directory.createSubdirectory("factories"), "ActivitiesInjectorFactories", rootPackage, fileTemplateManager, "ActivitiesInjectorFactories.kt");
        this.createFile(directory.findSubdirectory("factories"), "FragmentsInjectorFactories", rootPackage, fileTemplateManager, "FragmentsInjectorFactories.kt");
    }
    
    private void createFile(final PsiDirectory directory, final String name, final String rootPackage, final FileTemplateManager fileTemplateManager, final String templateName) {
        final FileTemplate template = fileTemplateManager.getJ2eeTemplate(templateName);
        final Properties props = fileTemplateManager.getDefaultProperties();
        props.put("ROOT_NAME", rootPackage);
        try {
            FileTemplateUtil.createFromTemplate(template, name, props, directory);
        }
        catch (Exception e) {
            throw new RuntimeException("Unable to create template for injection classes :(", e);
        }
    }
}
