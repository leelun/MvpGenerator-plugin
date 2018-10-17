package com.stfalcon.mvpgenerator;

import com.intellij.CommonBundle;
import com.intellij.icons.AllIcons;
import com.intellij.ide.actions.ElementCreator;
import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.ide.fileTemplates.FileTemplateUtil;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import org.jetbrains.android.dom.manifest.Activity;
import org.jetbrains.android.dom.manifest.Application;
import org.jetbrains.android.facet.AndroidFacet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class CreateKotlinMvpActivityAction extends AnAction {
    private ElementCreator myCreator;

    public CreateKotlinMvpActivityAction() {
        super(AllIcons.FileTypes.Any_type);
    }

    public void actionPerformed(final AnActionEvent e) {
        final Project project = e.getProject();
        if (project == null) {
            return;
        }
        final DataContext dataContext = e.getDataContext();
        final Module module = (Module) LangDataKeys.MODULE.getData(dataContext);
        if (module == null) {
            return;
        }
        final VirtualFile targetFile = (VirtualFile) CommonDataKeys.VIRTUAL_FILE.getData(dataContext);
        final PsiDirectory psiDirectory = FileUtils.validateSelectedDirectory(project, targetFile);
        if (psiDirectory == null) {
            return;
        }
        this.myCreator = new ElementCreator(module.getProject(), CommonBundle.getErrorTitle()) {
            protected PsiElement[] create(final String s) throws Exception {
                CreateKotlinMvpActivityAction.this.create(psiDirectory, s, module);
                return PsiElement.EMPTY_ARRAY;
            }

            protected String getActionName(final String s) {
                return "MVP Kotlin Activity";
            }
        };
        this.showCreationDialog(psiDirectory);
    }

    private void showCreationDialog(final PsiDirectory psiDirectory) {
        final DialogComponentName dialog = new DialogComponentName("Create Kotlin Activity (MVP based)", "Name of activity:");
        dialog.show();
        switch (dialog.getExitCode()) {
            case 0: {
                if (psiDirectory != null) {
                    this.myCreator.tryCreate(dialog.getComponentName());
                    break;
                }
                break;
            }
        }
    }

    private void create(final PsiDirectory directory, final String name, final Module module) {
        final String activityName = name + "Activity";
        final String activityContractName = name + "ActivityContract";
        final String activityModuleName = name + "ActivityModule";
        final String activityPresenterName = name + "ActivityPresenter";
        final String activitySubComponentName = name + "ActivitySubComponent";
        final String layoutName = "activity_" + StringUtils.camelCaseToSnakeCase(name);
        final FileTemplateManager fileTemplateManager = FileTemplateManager.getDefaultInstance();
        final AndroidFacet androidFacet = AndroidFacet.getInstance(module);
        final PsiManager psiManager = PsiManager.getInstance(module.getProject());
        final String projectPackage = androidFacet.getManifest().getPackage().getXmlAttributeValue().getValue();

        Properties mvppPoperties=new Properties();
        try {
            File mvpgeneratorSetting=new File(module.getProject().getBasePath()+"/mvpgenerator.properties");
            if(mvpgeneratorSetting.exists()){
                mvppPoperties.load(new FileInputStream(mvpgeneratorSetting));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.createPsiClass(directory, activityName, fileTemplateManager, "Activity.kt", new HashMap<String, String>() {
            {
                this.put("ACTIVITY_NAME", name);
                this.put("LAYOUT_NAME", layoutName);
                this.put("PROJECT_PACKAGE", projectPackage);
            }
        });
        this.createPsiClass(directory, activityContractName, fileTemplateManager, "ActivityContract.kt", new HashMap<String, String>() {
            {
                this.put("ACTIVITY_NAME", name);
            }
        });
        this.createPsiClass(directory, activityModuleName, fileTemplateManager, "ActivityModule.kt", new HashMap<String, String>() {
            {
                this.put("ACTIVITY_NAME", name);
            }
        });
        this.createPsiClass(directory, activityPresenterName, fileTemplateManager, "ActivityPresenter.kt", new HashMap<String, String>() {
            {
                this.put("ACTIVITY_NAME", name);
            }
        });
        this.createPsiClass(directory, activitySubComponentName, fileTemplateManager, "ActivitySubComponent.kt", new HashMap<String, String>() {
            {
                this.put("ACTIVITY_NAME", name);
            }
        });
        this.createLayoutFile(name, layoutName, androidFacet, psiManager, fileTemplateManager);
        final Properties properties = fileTemplateManager.getDefaultProperties();
        FileTemplateUtil.fillDefaultProperties(properties, directory);
        final String activityClass = properties.getProperty("PACKAGE_NAME") + "." + activityName;
        this.registerActivity(androidFacet, activityClass);
    }

    private void createPsiClass(final PsiDirectory directory, final String name, final FileTemplateManager fileTemplateManager, final String templateName, final Map<String, String> properties) {
        final FileTemplate template = fileTemplateManager.getJ2eeTemplate(templateName);
        final Properties props = fileTemplateManager.getDefaultProperties();
        props.putAll(properties);
        try {
            FileTemplateUtil.createFromTemplate(template, name, props, directory);
        } catch (Exception e) {
            throw new RuntimeException("Unable to create template for " + name + "Activity", e);
        }
    }

    private void createLayoutFile(final String name, final String layoutName, final AndroidFacet androidFacet, final PsiManager psiManager, final FileTemplateManager fileTemplateManager) {
        final VirtualFile resFolder = androidFacet.getAllResourceDirectories().get(0);
        if (resFolder != null) {
            try {
                VirtualFile layoutFolder = resFolder.findChild("layout");
                if (layoutFolder == null) {
                    layoutFolder = resFolder.createChildDirectory((Object) this, "layout");
                }
                final FileTemplate template = fileTemplateManager.getJ2eeTemplate("layout_activity.xml");
                final Properties props = fileTemplateManager.getDefaultProperties();
                FileTemplateUtil.createFromTemplate(template, layoutName, props, psiManager.findDirectory(layoutFolder));
            } catch (Exception e) {
                throw new RuntimeException("Unable to create layout for " + name + "Activity", e);
            }
        }
    }

    private void registerActivity(final AndroidFacet androidFacet, final String activityClass) {
        final Application application = androidFacet.getManifest().getApplication();
        final Activity activity = application.addActivity();
        activity.getActivityClass().setStringValue(activityClass);
        activity.getExported().setStringValue("false");
    }
}
