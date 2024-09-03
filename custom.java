package com.example;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.codehaus.mojo.versions.api.VersionsHelper;
import org.codehaus.mojo.versions.api.ArtifactVersions;
import org.apache.maven.artifact.Artifact;

import java.util.List;

@Mojo(name = "check-updates", defaultPhase = LifecyclePhase.VALIDATE)
public class CustomMavenPlugin extends AbstractMojo {

    @Component
    private MavenProject project;

    @Component
    private VersionsHelper versionsHelper;

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject mavenProject;

    public void execute() throws MojoExecutionException {
        // List dependencies
        getLog().info("Listing project dependencies:");
        List<Artifact> artifacts = (List<Artifact>) project.getArtifacts();
        for (Artifact artifact : artifacts) {
            getLog().info(artifact.getGroupId() + ":" + artifact.getArtifactId() + ":" + artifact.getVersion());
        }

        // Check for updates
        getLog().info("Checking for updates:");
        for (Artifact artifact : artifacts) {
            try {
                ArtifactVersions versions = versionsHelper.lookupArtifactVersions(artifact, false);
                String latestVersion = versions.getNewestVersion(null, false).toString();
                if (!artifact.getVersion().equals(latestVersion)) {
                    getLog().info(artifact.getArtifactId() + " has an update available: " + latestVersion);
                }
            } catch (Exception e) {
                getLog().error("Failed to check updates for " + artifact.getArtifactId(), e);
            }
        }
    }
}
