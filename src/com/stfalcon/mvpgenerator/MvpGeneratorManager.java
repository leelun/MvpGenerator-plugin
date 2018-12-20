package com.stfalcon.mvpgenerator;

import com.intellij.openapi.module.Module;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

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
        } catch (IOException e) {
            throw new RuntimeException("Can not find mvpgenerator.properties in project or current module", e);
        }
        return properties;
    }


    public static class GeneratorProperties extends Properties {
        public String getCommonPackage() {
            return getProperty("common.package");
        }

        public String getMvpActivityPackage() {
            return getProperty("base.activity.package");
        }

        public String getMvpFragmentPackage() {
            return getProperty("base.fragment.package");
        }

        public String getActivitiesInjectorFactory() {
            return getProperty("activity.injector.factory.file");
        }

        public String getFragmentsInjectorFactory() {
            return getProperty("fragment.injector.factory.file");
        }
    }

}
