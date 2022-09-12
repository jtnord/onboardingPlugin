package io.jenkins.plugins.sample;

import hudson.model.Action;
import hudson.model.Run;
import jenkins.model.RunAction2;


//RunAction2 is the interface to implement so that actions added to Run properly get references to the Run.
public class HelloWorldAction implements RunAction2 {

    private transient Run run;
    private String name;
    private String description;

    public HelloWorldAction (String name, String description){
        this.name = name;
        this.description = description;
    }

    public String getName(){
        return name;
    }
    public String getDescription(){ return description;}

    @Override
    public void onAttached(Run <?, ?> run){
        //Setting the field when first attaching this action to the Run.
        this.run = run;
    }

    @Override
    public void onLoad(Run <?, ?> run){
        //	Setting the run field when loading this action from disk
        this.run = run;
    }

    //This will make the Run available for use in the Jelly view—it cannot access private fields.
    public Run getRun(){
        return run;
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
