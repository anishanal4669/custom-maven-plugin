package com.example;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.artifact.repository.ArtifactRepository;

import java.util.List;

@Mojo(name = "fetch-metadata", defaultPhase = LifecyclePhase.VALIDATE)
public class FetchMetadataMojo extends AbstractMojo {

    @Component
    private MavenProject project;

    @Component
    private CustomMavenMetadataSource metadataSource;

    @Parameter(defaultValue = "${project.remoteArtifactRepositories}", readonly = true, required = true)
    private List<ArtifactRepository> remoteRepositories;

    public void execute() throws MojoExecutionException {
        for (Artifact artifact : project.getArtifacts()) {
            try {
                List<String> versions = metadataSource.fetchArtifactVersions(artifact, remoteRepositories);
                getLog().info("Versions for " + artifact.getArtifactId() + ": " + versions);
            } catch (Exception e) {
                getLog().error("Failed to fetch versions for " + artifact.getArtifactId(), e);
            }
        }
    }
}
