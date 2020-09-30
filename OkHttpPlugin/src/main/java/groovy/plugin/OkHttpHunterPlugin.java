package groovy.plugin;

import com.android.build.gradle.AppExtension;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

import java.util.Collections;

/**
 * 插件类必须实现Plugin接口，并重写apply方法
 */
public final class OkHttpHunterPlugin implements Plugin<Project> {


    @SuppressWarnings("NullableProblems")
    @Override
    public void apply(Project project) {
        System.out.println("========================");
        System.out.println("apply MyPlugin succeed!");
        System.out.println("========================");
        AppExtension appExtension = (AppExtension)project.getProperties().get("android");
        if (appExtension != null) {
            appExtension.registerTransform(new OkHttpHunterTransform(project), Collections.EMPTY_LIST);
        }
    }

}
