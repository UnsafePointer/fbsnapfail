package com.ruenzuo.fbsnapfail;

import hudson.model.AbstractBuild;
import hudson.model.Action;

/**
 * Created by ruenzuo on 23/04/16.
 */
public class SnapFailBuildAction implements Action {

    private AbstractBuild<?, ?> build;

    @Override
    public String getIconFileName() {
        return "/plugin/testExample/img/snapfail.png";
    }

    @Override
    public String getDisplayName() {
        return "Snapfail";
    }

    @Override
    public String getUrlName() {
        return "Snapfail";
    }

    SnapFailBuildAction(final AbstractBuild<?, ?> build) {
        this.build = build;
    }

}
