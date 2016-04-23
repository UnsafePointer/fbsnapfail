package com.ruenzuo.fbsnapfail;

import hudson.model.AbstractBuild;
import hudson.model.Action;

import java.util.HashMap;

/**
 * Created by ruenzuo on 23/04/16.
 */
public class SnapFailBuildAction implements Action {

    private AbstractBuild<?, ?> build;
    private HashMap<String, String> detected;

    @Override
    public String getIconFileName() {
        return "/plugin/fbsnapfail/img/snapfail.png";
    }

    @Override
    public String getDisplayName() {
        return "Snapfail results";
    }

    @Override
    public String getUrlName() {
        return "snap-fail";
    }

    public int getBuildNumber() {
        return this.build.number;
    }

    public HashMap<String, String> getDetected() {
        return this.detected;
    }

    public AbstractBuild<?, ?> getBuild() {
        return build;
    }

    SnapFailBuildAction(final AbstractBuild<?, ?> build, final HashMap<String, String> detected) {
        this.build = build;
        this.detected = detected;
    }

}
