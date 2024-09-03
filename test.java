package com.example;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.execution.MavenSession;

@Mojo(name = "check-custom-updates", defaultPhase = LifecyclePhase.VALIDATE)
public class CustomMavenPlugin extends AbstractMojo {

    @Component
    private MavenProject project;

    @Component
    private MavenSession session;

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject mavenProject;

    public void execute() throws MojoExecutionException {
        // Create an instance of the custom VersionsHelper
        CustomVersionsHelper customHelper = new CustomVersionsHelper(session, session.getUserProperties());

        // Use the custom helper to check for updates
        getLog().info("Checking for updates using custom helper:");
        try {
            customHelper.lookupVersions("com.example", "your-artifact", project)
                .forEach(version -> getLog().info("Available version: " + version));
        } catch (Exception e) {
            getLog().error("Failed to check updates", e);
        }
    }
}
