package com.stfalcon.mvpgenerator;

import com.intellij.ide.actions.*;
import com.intellij.icons.*;
import com.intellij.openapi.module.*;
import com.intellij.openapi.vfs.*;
import com.intellij.*;
import com.intellij.openapi.project.*;
import com.intellij.openapi.actionSystem.*;
import org.jetbrains.android.facet.*;
import com.intellij.psi.*;
import com.intellij.ide.fileTemplates.*;
import java.util.*;
import com.intellij.openapi.ui.popup.*;

public class CreateKotlinMvpFragmentAction extends AnAction
{
    private ElementCreator myCreator;
    
    public CreateKotlinMvpFragmentAction() {
        super(AllIcons.FileTypes.Any_type);
    }
    
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
        final PsiDirectory psiDirectory = FileUtils.validateSelectedDirectory(project, targetFile);
        if (psiDirectory == null) {
            return;
        }
        this.myCreator = new ElementCreator(module.getProject(), CommonBundle.getErrorTitle()) {
            protected PsiElement[] create(final String s) throws Exception {
                CreateKotlinMvpFragmentAction.this.create(psiDirectory, s, module);
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
        MvpGeneratorManager.GeneratorProperties mvpProperties=MvpGeneratorManager.getInstance().getProperties(module);
        Map<String,String> map=new HashMap<>();
        map.put("COMMON_PACKAGE",mvpProperties.getCommonPackage());
        map.put("MVP_FRAGMENT_PACKAGE",mvpProperties.getMvpActivityPackage());
        this.createPsiClass(directory, fragmentName, fileTemplateManager, "Fragment.kt", new HashMap<String, String>() {
            {
                this.put("FRAGMENT_NAME", name);
                this.put("LAYOUT_NAME", layoutName);
                this.put("PROJECT_PACKAGE", projectPackage);
                this.putAll(map);
            }
        });
        this.createPsiClass(directory, fragmentContractName, fileTemplateManager, "FragmentContract.kt", new HashMap<String, String>() {
            {
                this.put("FRAGMENT_NAME", name);
                this.putAll(map);
            }
        });
        this.createPsiClass(directory, fragmentModuleName, fileTemplateManager, "FragmentModule.kt", new HashMap<String, String>() {
            {
                this.put("FRAGMENT_NAME", name);
                this.putAll(map);
            }
        });
        this.createPsiClass(directory, fragmentPresenterName, fileTemplateManager, "FragmentPresenter.kt", new HashMap<String, String>() {
            {
                this.put("FRAGMENT_NAME", name);
                this.putAll(map);
            }
        });
        this.createPsiClass(directory, fragmentSubComponentName, fileTemplateManager, "FragmentSubComponent.kt", new HashMap<String, String>() {
            {
                this.put("FRAGMENT_NAME", name);
                this.putAll(map);
            }
        });
        this.createLayoutFile(name, layoutName, androidFacet, psiManager, fileTemplateManager);
    }
    
    private void createPsiClass(final PsiDirectory directory, final String name, final FileTemplateManager fileTemplateManager, final String templateName, final Map<String, String> properties) {
        final FileTemplate template = fileTemplateManager.getJ2eeTemplate(templateName);
        final Properties props = fileTemplateManager.getDefaultProperties();
        props.putAll(properties);
        try {
            FileTemplateUtil.createFromTemplate(template, name, props, directory);
        }
        catch (Exception e) {
            throw new RuntimeException("Unable to create template for " + name + "Fragment", e);
        }
    }
    
    private void createLayoutFile(final String name, final String layoutName, final AndroidFacet androidFacet, final PsiManager psiManager, final FileTemplateManager fileTemplateManager) {
        final VirtualFile resFolder = androidFacet.getAllResourceDirectories().get(0);
        if (resFolder != null) {
            try {
                VirtualFile layoutFolder = resFolder.findChild("layout");
                if (layoutFolder == null) {
                    layoutFolder = resFolder.createChildDirectory((Object)this, "layout");
                }
                final FileTemplate template = fileTemplateManager.getJ2eeTemplate("layout_fragment.xml");
                final Properties props = fileTemplateManager.getDefaultProperties();
                FileTemplateUtil.createFromTemplate(template, layoutName, props, psiManager.findDirectory(layoutFolder));
            }
            catch (Exception e) {
                throw new RuntimeException("Unable to create layout for " + name + "Fragment", e);
            }
        }
    }
    
    private void showError(final String text, final Project project) {
        JBPopupFactory.getInstance().createMessage(text).showCenteredInCurrentWindow(project);
    }
}
