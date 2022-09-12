package io.jenkins.plugins.sample;

import hudson.Launcher;
import hudson.Extension;
import hudson.FilePath;
import hudson.util.FormValidation;
import hudson.model.AbstractProject;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.Builder;
import hudson.tasks.BuildStepDescriptor;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jenkins.tasks.SimpleBuildStep;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundSetter;

public class HelloWorldBuilder extends Builder implements SimpleBuildStep{

    private final String name;
    private final String description;
    private boolean useFrench;

    @DataBoundConstructor
    public HelloWorldBuilder(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }
    public String getDescription(){ return description; }

    public boolean isUseFrench() {
        return useFrench;
    }

    @DataBoundSetter
    public void setUseFrench(boolean useFrench) {
        this.useFrench = useFrench;
    }

    @Override
    public void perform(Run<?, ?> run, FilePath workspace, Launcher launcher, TaskListener listener) throws InterruptedException, IOException {
        run.addAction(new HelloWorldAction(name, description));
        if (useFrench) {
            listener.getLogger().println("Bonjour, " + name + "!");
            listener.getLogger().println("description: " + description);
        } else {
            listener.getLogger().println("Hello, " + name + "!");
            listener.getLogger().println("description: " + description);
        }
    }

    @Symbol("greet")
    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {

        public FormValidation doCheckName(@QueryParameter String value, @QueryParameter boolean useFrench)
                throws IOException, ServletException {

            //Check for format AND value length otherwise error will appear when form is first open
            if(!NameFormatMatcher(value) && value.length() != 0){
                return FormValidation.error(Messages.HelloWorldBuilder_DescriptorImpl_errors_NameFormat());
            }
            if (value.length() == 0)
                return FormValidation.error(Messages.HelloWorldBuilder_DescriptorImpl_errors_missingName());
            if (value.length() < 4)
                return FormValidation.warning(Messages.HelloWorldBuilder_DescriptorImpl_warnings_tooShort());
            if (!useFrench && value.matches(".*[éáàç].*")) {
                return FormValidation.warning(Messages.HelloWorldBuilder_DescriptorImpl_warnings_reallyFrench());
            }
            return FormValidation.ok();
        }

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            return true;
        }

        @Override
        public String getDisplayName() {
            return Messages.HelloWorldBuilder_DescriptorImpl_DisplayName();
        }


        /**
         * Used as regular expression check for name field on the configure job screen
         * @param value = represents a name of a user that the user has typed in
         * @return if the name matches the format, it is a valid name (only letters and hyphens)
         */
        private boolean NameFormatMatcher(String value){
            Pattern pattern = Pattern.compile("^[A-Za-z\\s-]+$");
            Matcher matcher = pattern.matcher(value);
            return matcher.matches();
        }


    }

}
