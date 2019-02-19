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
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import org.jetbrains.android.facet.AndroidFacet;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class CreateJavaMvpFragmentAction extends AnAction {
    private ElementCreator myCreator;

    public CreateJavaMvpFragmentAction() {
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
                CreateJavaMvpFragmentAction.this.create(psiDirectory, s, module);
                return PsiElement.EMPTY_ARRAY;
            }

            protected String getActionName(final String s) {
                return "MVP Kotlin Fragment";
            }
        };
        this.showCreationDialog(psiDirectory);
    }

    private void showCreationDialog(final PsiDirectory psiDirectory) {
        final DialogComponentName dialog = new DialogComponentName("Create Kotlin Fragment (MVP based)", "Name of fragment:");
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
        final String fragmentName = name + "Fragment";
        final String fragmentContractName = name + "FragmentContract";
        final String fragmentModuleName = name + "FragmentModule";
        final String fragmentPresenterName = name + "FragmentPresenter";
        final String fragmentSubComponentName = name + "FragmentSubComponent";
        final String layoutName = "fragment_" + StringUtils.camelCaseToSnakeCase(name);
        final AndroidFacet androidFacet = AndroidFacet.getInstance(module);
        final PsiManager psiManager = PsiManager.getInstance(module.getProject());
        final String projectPackage = androidFacet.getManifest().getPackage().getXmlAttributeValue().getValue();
        final FileTemplateManager fileTemplateManager = FileTemplateManager.getDefaultInstance();
        MvpGeneratorManager.GeneratorProperties mvpProperties = MvpGeneratorManager.getInstance().getProperties(module);
        Map<String, String> map = new HashMap<>();
        map.put("COMMON_PACKAGE", mvpProperties.getCommonPackage());
        map.put("MVP_FRAGMENT_PACKAGE", mvpProperties.getMvpFragmentPackage());
        this.createPsiClass(directory, fragmentName, fileTemplateManager, "Fragment.java", new HashMap<String, String>() {
            {
                this.put("FRAGMENT_NAME", name);
                this.put("LAYOUT_NAME", layoutName);
                this.put("PROJECT_PACKAGE", projectPackage);
                this.putAll(map);
            }
        });
        this.createPsiClass(directory, fragmentContractName, fileTemplateManager, "FragmentContract.java", new HashMap<String, String>() {
            {
                this.put("FRAGMENT_NAME", name);
                this.putAll(map);
            }
        });
        this.createPsiClass(directory, fragmentModuleName, fileTemplateManager, "FragmentModule.java", new HashMap<String, String>() {
            {
                this.put("FRAGMENT_NAME", name);
                this.putAll(map);
            }
        });
        this.createPsiClass(directory, fragmentPresenterName, fileTemplateManager, "FragmentPresenter.java", new HashMap<String, String>() {
            {
                this.put("FRAGMENT_NAME", name);
                this.putAll(map);
            }
        });
        this.createPsiClass(directory, fragmentSubComponentName, fileTemplateManager, "FragmentSubComponent.java", new HashMap<String, String>() {
            {
                this.put("FRAGMENT_NAME", name);
                this.putAll(map);

                Properties properties = fileTemplateManager.getDefaultProperties();
                properties.put("FRAGMENT_NAME", name);
                String bindMethodStr = "";
                try {
                    bindMethodStr = fileTemplateManager.getJ2eeTemplate("FragmentBindInjectMethod.java").getText(properties);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String filepath = mvpProperties.getFragmentsInjectorFactory();
                if (StringUtils.isEmpty(filepath) || !new File(filepath).exists()) {
                    this.put("BIND_METHOD", bindMethodStr);
                } else {
                    String packageName = ProjectConfigurationManager.getInstance().getModuleConfigurable(module).getClassFilePackageName(directory.getVirtualFile().getPath());
                    this.put("BIND_METHOD", "");
                    InjectFactoryUtils.appendSubComponent(filepath, new String[]{packageName + "." + name + "Fragment", packageName + "." + name + "FragmentSubComponent"}, name + "FragmentSubComponent.class", bindMethodStr);
                }
            }
        });
        this.createLayoutFile(name, layoutName, androidFacet, psiManager, fileTemplateManager, mvpProperties);
    }

    private void createPsiClass(final PsiDirectory directory, final String name, final FileTemplateManager fileTemplateManager, final String templateName, final Map<String, String> properties) {
        final FileTemplate template = fileTemplateManager.getJ2eeTemplate(templateName);
        final Properties props = fileTemplateManager.getDefaultProperties();
        props.putAll(properties);
        try {
            FileTemplateUtil.createFromTemplate(template, name, props, directory);
        } catch (Exception e) {
            throw new RuntimeException("Unable to create template for " + name + "Fragment", e);
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
                    final FileTemplate template = fileTemplateManager.getJ2eeTemplate("layout_fragment.xml");
                    final Properties props = fileTemplateManager.getDefaultProperties();
                    FileTemplateUtil.createFromTemplate(template, layoutName, props, psiManager.findDirectory(layoutFolder));
                } catch (Exception e) {
                    throw new RuntimeException("Unable to create layout for " + name + "Fragment", e);
                }
            }
        }
    }

    private void showError(final String text, final Project project) {
        JBPopupFactory.getInstance().createMessage(text).showCenteredInCurrentWindow(project);
    }
}
