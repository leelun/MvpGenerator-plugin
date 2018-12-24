package com.stfalcon.mvpgenerator;

import com.intellij.openapi.module.Module;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class MvpGeneratorManager {
    private static volatile MvpGeneratorManager instance;

    public static MvpGeneratorManager getInstance() {
        if (instance == null) {
            synchronized (MvpGeneratorManager.class) {
                if (instance == null) {
                    instance = new MvpGeneratorManager();
                }
            }
        }
        return instance;
    }

    private MvpGeneratorManager() {
    }

    public synchronized GeneratorProperties getProperties(Module module) {
        String modelBasePath = module.getModuleFile().getParent().getPath();
        GeneratorProperties properties = new GeneratorProperties();
        try {
            File file = new File(modelBasePath + File.separator + "mvpgenerator.properties");
            if (!file.exists()) {
                file = new File(module.getProject().getBasePath() + File.separator + "mvpgenerator.properties");
            }
            properties.load(new FileInputStream(file));
            properties.put("PROJECT_PATH", module.getProject().getBasePath());
            properties.put("MODULE_PATH", modelBasePath);
        } catch (IOException e) {
            throw new RuntimeException("Can not find mvpgenerator.properties in project or current module", e);
        }
        return properties;
    }


    public static class GeneratorProperties extends Properties {
        private static final String MVP_HELPER_PACKAGE = "mvp.helper.package";
        private static final String MVP_ACTIVITY_PACKAGE = "mvp.activity.package";
        private static final String MVP_FRAGMENT_PACKAGE = "mvp.fragment.package";
        private static final String ACTIVITY_INJECTOR_FACTORY_FILE = "activity.injector.factory.file";
        private static final String FRAGMENT_INJECTOR_FACTORY_FILE = "fragment.injector.factory.file";

        public String getCommonPackage() {
            return getProperty(MVP_HELPER_PACKAGE);
        }

        public String getMvpActivityPackage() {
            return getProperty(MVP_ACTIVITY_PACKAGE);
        }

        public String getMvpFragmentPackage() {
            return getProperty(MVP_FRAGMENT_PACKAGE);
        }

        public String getProjectPath() {
            return getProperty("PROJECT_PATH");
        }

        public String getModulePath() {
            return getProperty("MODULE_PATH");
        }

        public String getActivitiesInjectorFactory() {
            String filepath = getProperty(ACTIVITY_INJECTOR_FACTORY_FILE);
            if (filepath != null) {
                filepath = filepath.trim();
                if (filepath.startsWith("$PROJECT_DIR$")) {
                    filepath = filepath.replace("$PROJECT_DIR$", getProjectPath());
                } else if (filepath.startsWith("$MODULE_DIR$")) {
                    filepath = filepath.replace("$MODULE_DIR$", getModulePath());
                }
                return filepath;
            } else {
                return null;
            }
        }

        public String getFragmentsInjectorFactory() {
            String filepath = getProperty(FRAGMENT_INJECTOR_FACTORY_FILE);
            if (filepath != null) {
                filepath = filepath.trim();
                if (filepath.startsWith("$PROJECT_DIR$")) {
                    filepath = filepath.replace("$PROJECT_DIR$", getProjectPath());
                } else if (filepath.startsWith("$MODULE_DIR$")) {
                    filepath = filepath.replace("$MODULE_DIR$", getModulePath());
                }
                return filepath;
            } else {
                return null;
            }
        }
    }

}
