package io.jenkins.plugins.sample;

import hudson.model.FreeStyleBuild;
import hudson.model.FreeStyleProject;
import hudson.model.Label;
import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition;
import org.jenkinsci.plugins.workflow.job.WorkflowJob;
import org.jenkinsci.plugins.workflow.job.WorkflowRun;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

public class HelloWorldBuilderTest {

    @Rule
    public JenkinsRule jenkinsRule = new JenkinsRule();

    final String name = "Julie";
    final String myDescription = "Is awesome";

    @Test
    public void testConfigRoundtrip() throws Exception {
        FreeStyleProject project = jenkinsRule.createFreeStyleProject();
        project.getBuildersList().add(new HelloWorldBuilder(name, myDescription));
        project = jenkinsRule.configRoundtrip(project);
        jenkinsRule.assertEqualDataBoundBeans(new HelloWorldBuilder(name, myDescription), project.getBuildersList().get(0));
    }

    @Test
    public void testConfigRoundtripFrench() throws Exception {
        FreeStyleProject project = jenkinsRule.createFreeStyleProject();
        HelloWorldBuilder builder = new HelloWorldBuilder(name, myDescription);
        builder.setUseFrench(true);
        project.getBuildersList().add(builder);
        project = jenkinsRule.configRoundtrip(project);

        HelloWorldBuilder lhs = new HelloWorldBuilder(name, myDescription);
        lhs.setUseFrench(true);
        jenkinsRule.assertEqualDataBoundBeans(lhs, project.getBuildersList().get(0));
    }

    @Test
    public void testBuild() throws Exception {
        FreeStyleProject project = jenkinsRule.createFreeStyleProject();
        HelloWorldBuilder builder = new HelloWorldBuilder(name, myDescription);
        project.getBuildersList().add(builder);

        FreeStyleBuild build = jenkinsRule.buildAndAssertSuccess(project);
        jenkinsRule.assertLogContains("Hello, " + name, build);
    }

    @Test
    public void testBuildFrench() throws Exception {

        FreeStyleProject project = jenkinsRule.createFreeStyleProject();
        HelloWorldBuilder builder = new HelloWorldBuilder(name, myDescription);
        builder.setUseFrench(true);
        project.getBuildersList().add(builder);

        FreeStyleBuild build = jenkinsRule.buildAndAssertSuccess(project);
        jenkinsRule.assertLogContains("Bonjour, " + name, build);
    }

    @Test
    public void testScriptedPipeline() throws Exception {
        String agentLabel = "my-agent";
        jenkinsRule.createOnlineSlave(Label.get(agentLabel));
        WorkflowJob job = jenkinsRule.createProject(WorkflowJob.class, "test-scripted-pipeline");
        String pipelineScript
                = "node {\n"
                + "  greet name:'" + name + "', myDescription:'"+ myDescription +"'\n"
                + "}";
        job.setDefinition(new CpsFlowDefinition(pipelineScript, true));
        WorkflowRun completedBuild = jenkinsRule.assertBuildStatusSuccess(job.scheduleBuild2(0));
        String expectedString = "Hello, " + name + "!";
        jenkinsRule.assertLogContains(expectedString, completedBuild);
    }

}