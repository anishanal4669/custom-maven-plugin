package com.example;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.repository.metadata.Metadata;
import org.apache.maven.artifact.repository.metadata.RepositoryMetadataManager;
import org.apache.maven.artifact.repository.metadata.RepositoryMetadataResolutionException;
import org.apache.maven.artifact.resolver.ArtifactResolutionRequest;
import org.apache.maven.artifact.resolver.ArtifactResolutionResult;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.util.List;

@Mojo(name = "fetch-dependency-metadata", defaultPhase = LifecyclePhase.VALIDATE)
public class FetchDependencyMetadataMojo extends AbstractMojo {

    @Component
    private MavenProject project;

    @Component
    private ArtifactResolver artifactResolver;

    @Component
    private RepositoryMetadataManager repositoryMetadataManager;

    @Parameter(defaultValue = "${project.remoteArtifactRepositories}", readonly = true, required = true)
    private List<ArtifactRepository> remoteRepositories;

    @Parameter(defaultValue = "${localRepository}", readonly = true, required = true)
    private ArtifactRepository localRepository;

    public void execute() throws MojoExecutionException {
        for (Artifact artifact : project.getArtifacts()) {
            try {
                // Resolving the artifact metadata
                Metadata metadata = fetchArtifactMetadata(artifact);
                if (metadata != null) {
                    getLog().info("Versions for " + artifact.getArtifactId() + ": " + metadata.getVersioning().getVersions());
                } else {
                    getLog().warn("No metadata found for " + artifact.getArtifactId());
                }
            } catch (Exception e) {
                getLog().error("Failed to fetch metadata for " + artifact.getArtifactId(), e);
            }
        }
    }

    private Metadata fetchArtifactMetadata(Artifact artifact) throws RepositoryMetadataResolutionException {
        ArtifactResolutionRequest request = new ArtifactResolutionRequest();
        request.setArtifact(artifact);
        request.setRemoteRepositories(remoteRepositories);
        request.setLocalRepository(localRepository);

        // Resolve the artifact to ensure it's available locally
        ArtifactResolutionResult result = artifactResolver.resolve(request);
        if (!result.isSuccess()) {
            getLog().warn("Artifact resolution failed for " + artifact.getArtifactId());
            return null;
        }

        // Now fetch the metadata
        Metadata metadata = repositoryMetadataManager.resolveArtifactMetadata(artifact, localRepository, remoteRepositories);
        return metadata;
    }
}
