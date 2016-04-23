package com.ruenzuo.fbsnapfail;

import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import hudson.tasks.Recorder;
import hudson.util.FormValidation;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import javax.servlet.ServletException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;

/**
 * Created by ruenzuo on 23/04/16.
 */
public class SnapFailPublisher extends Recorder {

    private final String logpath;
    private final HashMap<String, String> detected = new HashMap<>();

    @DataBoundConstructor
    public SnapFailPublisher(String logpath) {
        this.logpath = logpath;
    }

    public String getLogpath() {
        return logpath;
    }

    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) throws InterruptedException, IOException {
        FilePath workspacePath = build.getWorkspace();
        URI uri = workspacePath.toURI();
        File file = new File(uri.getPath(), logpath);
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("ksdiff")) {
                    String[] lines = line.split(" ");
                    detected.put(lines[1].replace("\"", ""), lines[2].replace("\"", ""));
                }
            }
        }


        SnapFailBuildAction buildAction = new SnapFailBuildAction(build, detected);
        build.addAction(buildAction);

        return true;
    }

    @Override
    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.NONE;
    }

    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl) super.getDescriptor();
    }

    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Publisher> {

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            return true;
        }

        @Override
        public String getDisplayName() {
            return "Publish results from FBSnapshotTestCase";
        }

        public FormValidation doCheckName(@QueryParameter String value)
                throws IOException, ServletException {
            if (value.length() == 0) {
                return FormValidation.error("Please set a path");
            }
            return FormValidation.ok();
        }

    }

}
