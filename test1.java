package com.example.maven;

import org.codehaus.mojo.versions.api.DefaultVersionHelper;
import org.codehaus.mojo.versions.api.PomHelper;
import org.apache.maven.project.MavenProject;
import org.codehaus.mojo.versions.ordering.VersionComparator;
import org.codehaus.mojo.versions.api.ArtifactVersions;
import org.apache.maven.artifact.Artifact;

import java.util.Optional;

public class CustomVersionHelper extends DefaultVersionHelper {

    public CustomVersionHelper(MavenProject project) {
        super(project);
    }

    @Override
    public ArtifactVersions lookupArtifactVersions(Artifact artifact, boolean usePluginRepositories) throws Exception {
        // Custom logic can be implemented here
        System.out.println("Looking up versions for artifact: " + artifact);
        
        // Call the parent method to maintain original behavior
        ArtifactVersions versions = super.lookupArtifactVersions(artifact, usePluginRepositories);
        
        // Additional custom behavior
        Optional<String> latestVersion = getLatestVersion(versions);
        latestVersion.ifPresent(version -> System.out.println("Latest version found: " + version));
        
        return versions;
    }

    private Optional<String> getLatestVersion(ArtifactVersions versions) {
        // Implement custom logic to determine the latest version, e.g., based on a specific versioning scheme
        return Optional.ofNullable(versions.getNewestVersion(null, null, false, true));
    }
}