package io.jenkins.plugins.sample;

import hudson.model.Action;
import hudson.model.Run;

public class HelloWorldAction implements Action {

    private transient Run run;

    private String name;

    public HelloWorldAction (String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    /**
     * This is the icon used for the side panel item.
     * document.png is one of the predefined icons bundled with Jenkins
     */
    @Override
    public String getIconFileName(){
        return "document.png";
    }

    /**
     * This is the label used for the side panel item.
     */
    @Override
    public String getDisplayName(){
        return "Greeting";
    }

    /**
     * This is the URL fragment used for this action
     */
    @Override
    public String getUrlName(){
        return "greeting";
    }


}
