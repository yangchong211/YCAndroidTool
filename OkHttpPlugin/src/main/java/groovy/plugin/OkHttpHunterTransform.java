package groovy.plugin;

import com.android.build.api.transform.Context;
import com.android.build.api.transform.TransformException;
import com.android.build.api.transform.TransformInput;
import com.android.build.api.transform.TransformOutputProvider;

import org.gradle.api.Project;

import java.io.IOException;
import java.util.Collection;

import groovy.hunter.HunterTransform;
import groovy.hunter.RunVariant;
import groovy.plugin.bytecode.OkHttpWeaver;


final class OkHttpHunterTransform extends HunterTransform {

    private Project project;
    private OkHttpHunterExtension okHttpHunterExtension;

    public OkHttpHunterTransform(Project project) {
        super(project);
        this.project = project;
        project.getExtensions().create("okHttpHunterExt", OkHttpHunterExtension.class);
        this.bytecodeWeaver = new OkHttpWeaver();
    }

    @Override
    public void transform(Context context, Collection<TransformInput> inputs,
                          Collection<TransformInput> referencedInputs,
                          TransformOutputProvider outputProvider, boolean isIncremental)
            throws IOException, TransformException, InterruptedException {
        okHttpHunterExtension = (OkHttpHunterExtension) project.getExtensions().getByName("okHttpHunterExt");
        this.bytecodeWeaver.setExtension(okHttpHunterExtension);
        super.transform(context, inputs, referencedInputs, outputProvider, isIncremental);
    }

    @Override
    protected RunVariant getRunVariant() {
        return okHttpHunterExtension.runVariant;
    }

    @Override
    protected boolean inDuplcatedClassSafeMode() {
        return okHttpHunterExtension.duplcatedClassSafeMode;
    }
}
