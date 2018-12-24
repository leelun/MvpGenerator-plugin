package com.stfalcon.mvpgenerator;


import com.intellij.openapi.module.Module;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.ArrayList;
import java.util.List;

public class ProjectConfigurationManager {
    private static volatile ProjectConfigurationManager instance;

    public static ProjectConfigurationManager getInstance() {
        if (instance == null) {
            synchronized (ProjectConfigurationManager.class) {
                if (instance == null) {
                    instance = new ProjectConfigurationManager();
                }
            }
        }
        return instance;
    }

    private ProjectConfigurationManager() {
    }

    public ModuleConfigurable getModuleConfigurable(Module module) {
        return new ModuleConfigurable(module);
    }

    public static class ModuleConfigurable {
        private List<String> moduleInventories;
        private Module module;

        private ModuleConfigurable(Module module) {
            try {
                this.module = module;
                SAXParserFactory factory = SAXParserFactory.newInstance();
                SAXParser parser = factory.newSAXParser();
                ModuleInventoryHandler handler = new ModuleInventoryHandler();
                parser.parse(module.getModuleFilePath(), handler);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public String getClassFilePackageName(String classFilePath) {
            classFilePath = classFilePath.replaceAll("\\\\", "/").replaceAll("//", "/");
            for (String str : moduleInventories) {
                str = str.trim().replaceAll("file://", "").replaceAll("\\$MODULE_DIR\\$", module.getModuleFile().getParent().getPath()).replaceAll("\\\\", "/").replace("//", "/");
                if (classFilePath.startsWith(str)) {
                    if (classFilePath.contains(".")) {
                        classFilePath = classFilePath.substring(str.length(), classFilePath.lastIndexOf('/'));
                    } else {
                        classFilePath = classFilePath.substring(str.length());
                    }
                    if (classFilePath.startsWith("/")) {
                        classFilePath = classFilePath.substring(1, classFilePath.length());
                    }
                    String packageName = classFilePath.replaceAll("/", ".");
                    return packageName;
                }
            }
            return null;
        }

        class ModuleInventoryHandler extends DefaultHandler {
            public void startDocument() {
                moduleInventories = new ArrayList<>();
            }

            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                if ("sourceFolder".equals(qName)) {
                    moduleInventories.add(attributes.getValue("url"));
                }
            }
        }
    }
}
