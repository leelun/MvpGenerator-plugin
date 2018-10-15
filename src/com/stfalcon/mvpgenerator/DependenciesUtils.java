package com.stfalcon.mvpgenerator;

import com.android.tools.idea.gradle.parser.BuildFileKey;
import com.android.tools.idea.gradle.parser.Dependency;
import com.android.tools.idea.gradle.parser.GradleBuildFile;
import com.android.tools.idea.gradle.parser.GradleSettingsFile;
import com.android.tools.idea.gradle.project.importing.GradleProjectImporter;
import com.android.tools.idea.gradle.project.sync.GradleSyncListener;
import com.intellij.openapi.application.Result;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootModificationUtil;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

class DependenciesUtils {
    private static final String LIB_GROUP = "com.github.stfalcon";
    private static final String LIB_NAME = "mvphelper";
    private static final String LIB_VERSION = "0.2.1";

    static void addDependenciesIfNeeded1(final Module module) {
        ModuleRootModificationUtil.updateModel(module, model -> {
            final GradleSettingsFile gradleSettingsFile = GradleSettingsFile.get(model.getProject());
            final String moduleGradlePath = GradleSettingsFile.getModuleGradlePath(module);
            final GradleBuildFile buildFile = gradleSettingsFile.getModuleBuildFile(moduleGradlePath);
            final Dependency newDependency = new Dependency(Dependency.Scope.COMPILE, Dependency.Type.EXTERNAL, String.format("%s:%s:%s", LIB_GROUP, LIB_NAME, LIB_VERSION));
            final List<Dependency> value = (List<Dependency>) buildFile.getValue(BuildFileKey.DEPENDENCIES);
            final List<Dependency> dependencies = (value != null) ? value : new ArrayList<Dependency>();
            if (!dependencies.contains(newDependency)) {
                dependencies.add(newDependency);
                new WriteCommandAction<Void>(module.getProject(), "Add dependency", new PsiFile[]{buildFile.getPsiFile()}) {

                    protected void run(@NotNull final Result<Void> result) throws Throwable {
                        if (result == null) {
                            reportNull(0);
                        }
                        buildFile.setValue(BuildFileKey.DEPENDENCIES, dependencies);
                        GradleProjectImporter.getInstance().importProject(module.getProject().getProjectFile().getName(), new File(module.getProject().getProjectFile().getPath()), new GradleSyncListener.Adapter() {
                            public void syncSucceeded(@NotNull final Project project) {
                                if (project == null) {
                                    reportNull(0);
                                }
                                super.syncSucceeded(project);
                                VirtualFileManager.getInstance().syncRefresh();
                            }

                            private void reportNull(final int n) {
                                throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", "project", "com/stfalcon/mvpgenerator/DependenciesUtils", "syncSucceeded"));
                            }
                        });
                    }

                    private void reportNull(final int n) {
                        throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", "result", "com/stfalcon/mvpgenerator/DependenciesUtils", "run"));
                    }
                }.execute();
            }
        });
    }
}
