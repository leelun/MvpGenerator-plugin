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
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import org.jetbrains.android.dom.manifest.Activity;
import org.jetbrains.android.dom.manifest.Application;
import org.jetbrains.android.facet.AndroidFacet;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class CreateJavaMvpActivityAction extends AnAction {
    private ElementCreator myCreator;

    public CreateJavaMvpActivityAction() {
        super(AllIcons.FileTypes.Any_type);
    }

    private void test(Project project, PsiDirectory psiDirectory) {
        Map<String, String> map = new HashMap<>();
        map.put("ACTIVITY_NAME", "Main");
        map.put("LAYOUT_NAME", "main");
        map.put("PROJECT_PACKAGE", "com.common");
        map.put("COMMON_PACKAGE", "com.package");
        map.put("MVP_ACTIVITY_PACKAGE", "activity.package");
        map.put("NAME", "Main");
        Properties props = FileTemplateManager.getDefaultInstance().getDefaultProperties();
        for (String key : props.stringPropertyNames()) {
            System.out.println(key + " = " + props.get(key));
        }
        System.out.println("===========" + project.getBasePath());
        props.putAll(map);

        try {
            FileTemplate template1 = FileTemplateManager.getDefaultInstance().getJ2eeTemplate("ActivityContract.java");
            System.out.println(template1.getText(props));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(final AnActionEvent e) {
        final Project project = e.getProject();
        if (project == null) {
            return;
        }
        final DataContext dataContext = e.getDataContext();
        final Module module = LangDataKeys.MODULE.getData(dataContext);
        if (module == null) {
            return;
        }
        final VirtualFile targetFile = CommonDataKeys.VIRTUAL_FILE.getData(dataContext);
        final PsiDirectory psiDirectory = FileUtils.validateSelectedDirectory(project, targetFile);
        if (psiDirectory == null) {
            return;
        }
        test(project, psiDirectory);
        this.myCreator = new ElementCreator(module.getProject(), CommonBundle.getErrorTitle()) {
            protected PsiElement[] create(final String s) throws Exception {
                CreateJavaMvpActivityAction.this.create(psiDirectory, s, module);
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
        MvpGeneratorManager.GeneratorProperties mvpProperties = MvpGeneratorManager.getInstance().getProperties(module);
        Map<String, String> map = new HashMap<>();
        map.put("COMMON_PACKAGE", mvpProperties.getCommonPackage());
        map.put("MVP_ACTIVITY_PACKAGE", mvpProperties.getMvpActivityPackage());
        this.createPsiClass(directory, activityName, fileTemplateManager, "Activity.java", new HashMap<String, String>() {
            {
                this.put("ACTIVITY_NAME", name);
                this.put("LAYOUT_NAME", layoutName);
                this.put("PROJECT_PACKAGE", projectPackage);
                this.putAll(map);
            }
        });
        this.createPsiClass(directory, activityContractName, fileTemplateManager, "ActivityContract.java", new HashMap<String, String>() {
            {
                this.put("ACTIVITY_NAME", name);
                this.putAll(map);
            }
        });
        this.createPsiClass(directory, activityModuleName, fileTemplateManager, "ActivityModule.java", new HashMap<String, String>() {
            {
                this.put("ACTIVITY_NAME", name);
                this.putAll(map);
            }
        });
        this.createPsiClass(directory, activityPresenterName, fileTemplateManager, "ActivityPresenter.java", new HashMap<String, String>() {
            {
                this.put("ACTIVITY_NAME", name);
                this.putAll(map);
            }
        });
        this.createPsiClass(directory, activitySubComponentName, fileTemplateManager, "ActivitySubComponent.java", new HashMap<String, String>() {
            {
                this.put("ACTIVITY_NAME", name);
                this.putAll(map);
                Properties properties = fileTemplateManager.getDefaultProperties();
                properties.put("ACTIVITY_NAME", name);
                String bindMethodStr = "";
                try {
                    bindMethodStr = fileTemplateManager.getJ2eeTemplate("ActivityBindInjectMethod.java").getText(properties);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String filepath = mvpProperties.getActivitiesInjectorFactory();
                if (StringUtils.isEmpty(filepath) || !new File(filepath).exists()) {
                    this.put("BIND_METHOD", bindMethodStr);
                } else {
                    String packageName = ProjectConfigurationManager.getInstance().getModuleConfigurable(module).getClassFilePackageName(directory.getVirtualFile().getPath());
                    this.put("BIND_METHOD", "");
                    InjectFactoryUtils.appendSubComponent(filepath, new String[]{packageName + "." + name + "Activity", packageName + "." + name + "ActivitySubComponent"}, name + "ActivitySubComponent.class", bindMethodStr);
                }
            }
        });
        this.createLayoutFile(name, layoutName, androidFacet, psiManager, fileTemplateManager, mvpProperties);
        final Properties properties = fileTemplateManager.getDefaultProperties();
        FileTemplateUtil.fillDefaultProperties(properties, directory);
        final String activityClass = properties.getProperty("PACKAGE_NAME") + "." + activityName;
        this.registerActivity(androidFacet, activityClass, mvpProperties);
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

    private void createLayoutFile(final String name, final String layoutName, final AndroidFacet androidFacet, final PsiManager psiManager, final FileTemplateManager fileTemplateManager, MvpGeneratorManager.GeneratorProperties mvpProperties) {
        final VirtualFile resFolder = androidFacet.getAllResourceDirectories().get(0);
        if (resFolder != null) {
            String[] layouts = mvpProperties.getLayoutFolders();
            for (String layout : layouts) {
                try {
                    VirtualFile layoutFolder = resFolder.findChild(layout);
                    if (layoutFolder == null) {
                        layoutFolder = resFolder.createChildDirectory((Object) this, layout);
                    }
                    final FileTemplate template = fileTemplateManager.getJ2eeTemplate("layout_activity.xml");
                    final Properties props = fileTemplateManager.getDefaultProperties();
                    FileTemplateUtil.createFromTemplate(template, layoutName, props, psiManager.findDirectory(layoutFolder));
                } catch (Exception e) {
                    throw new RuntimeException("Unable to create " + layout + " for " + name + "Activity " + e.getMessage(), e);
                }
            }

        }
    }

    private void registerActivity(final AndroidFacet androidFacet, final String activityClass, MvpGeneratorManager.GeneratorProperties mvpProperties) {
        final Application application = androidFacet.getManifest().getApplication();
        final Activity activity = application.addActivity();
        activity.getActivityClass().setStringValue(activityClass);
        activity.getExported().setStringValue("false");
        Map<String, String> attrs = mvpProperties.getManifestActivityTagAttrs();
        for (String key : attrs.keySet()) {
            activity.getXmlTag().setAttribute(key, attrs.get(key));
        }
    }
}
