package com.stfalcon.mvpgenerator;

import com.intellij.openapi.fileTypes.*;
import com.intellij.ide.fileTemplates.*;

public class FileTemplateProvider implements FileTemplateGroupDescriptorFactory
{
    static final String MVP_ACTIVITY = "Activity.kt";
    static final String MVP_ACTIVITY_CONTRACT = "ActivityContract.kt";
    static final String MVP_ACTIVITY_MODULE = "ActivityModule.kt";
    static final String MVP_ACTIVITY_PRESENTER = "ActivityPresenter.kt";
    static final String MVP_ACTIVITY_SUB_COMPONENT = "ActivitySubComponent.kt";
    static final String MVP_ACTIVITY_LAYOUT = "layout_activity.xml";
    static final String MVP_FRAGMENT = "Fragment.kt";
    static final String MVP_FRAGMENT_CONTRACT = "FragmentContract.kt";
    static final String MVP_FRAGMENT_MODULE = "FragmentModule.kt";
    static final String MVP_FRAGMENT_PRESENTER = "FragmentPresenter.kt";
    static final String MVP_FRAGMENT_SUB_COMPONENT = "FragmentSubComponent.kt";
    static final String MVP_FRAGMENT_LAYOUT = "layout_fragment.xml";
    static final String DI_BASE_APP = "App.kt";
    static final String DI_BASE_APP_COMPONENT = "AppComponent.kt";
    static final String DI_BASE_APP_MODULE = "AppModule.kt";
    static final String DI_BASE_ACTIVITIES_INJECTOR_FACTORIES = "ActivitiesInjectorFactories.kt";
    static final String DI_BASE_FRAGMENTS_INJECTOR_FACTORIES = "FragmentsInjectorFactories.kt";
    
    public FileTemplateGroupDescriptor getFileTemplatesDescriptor() {
        final FileTemplateGroupDescriptor group = new FileTemplateGroupDescriptor("MVP", StdFileTypes.JAVA.getIcon());
//        group.addTemplate(new FileTemplateDescriptor(MVP_ACTIVITY, StdFileTypes.JAVA.getIcon()));
//        group.addTemplate(new FileTemplateDescriptor(MVP_ACTIVITY_CONTRACT, StdFileTypes.JAVA.getIcon()));
//        group.addTemplate(new FileTemplateDescriptor(MVP_ACTIVITY_MODULE, StdFileTypes.JAVA.getIcon()));
//        group.addTemplate(new FileTemplateDescriptor(MVP_ACTIVITY_PRESENTER, StdFileTypes.JAVA.getIcon()));
//        group.addTemplate(new FileTemplateDescriptor(MVP_ACTIVITY_SUB_COMPONENT, StdFileTypes.JAVA.getIcon()));
//        group.addTemplate(new FileTemplateDescriptor(MVP_ACTIVITY_LAYOUT, StdFileTypes.XML.getIcon()));
//        group.addTemplate(new FileTemplateDescriptor(MVP_FRAGMENT, StdFileTypes.JAVA.getIcon()));
//        group.addTemplate(new FileTemplateDescriptor(MVP_FRAGMENT_CONTRACT, StdFileTypes.JAVA.getIcon()));
//        group.addTemplate(new FileTemplateDescriptor(MVP_FRAGMENT_MODULE, StdFileTypes.JAVA.getIcon()));
//        group.addTemplate(new FileTemplateDescriptor(MVP_FRAGMENT_PRESENTER, StdFileTypes.JAVA.getIcon()));
//        group.addTemplate(new FileTemplateDescriptor(MVP_FRAGMENT_SUB_COMPONENT, StdFileTypes.JAVA.getIcon()));
//        group.addTemplate(new FileTemplateDescriptor(MVP_FRAGMENT_LAYOUT, StdFileTypes.XML.getIcon()));
//        group.addTemplate(new FileTemplateDescriptor(DI_BASE_APP, StdFileTypes.JAVA.getIcon()));
//        group.addTemplate(new FileTemplateDescriptor(DI_BASE_APP_COMPONENT, StdFileTypes.JAVA.getIcon()));
//        group.addTemplate(new FileTemplateDescriptor(DI_BASE_APP_MODULE, StdFileTypes.JAVA.getIcon()));
//        group.addTemplate(new FileTemplateDescriptor(DI_BASE_ACTIVITIES_INJECTOR_FACTORIES, StdFileTypes.JAVA.getIcon()));
//        group.addTemplate(new FileTemplateDescriptor(DI_BASE_FRAGMENTS_INJECTOR_FACTORIES, StdFileTypes.JAVA.getIcon()));
        return group;
    }
}
