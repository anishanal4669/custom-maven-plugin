package com.example;

import org.codehaus.mojo.versions.api.DefaultVersionsHelper;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.project.MavenProject;

import java.util.List;
import java.util.Properties;

public class CustomVersionsHelper extends DefaultVersionsHelper {

    public CustomVersionsHelper(MavenSession session, Properties userProperties) {
        super(session, userProperties, null, null, null, null, null);
    }

    // Example of overriding a method
    @Override
    public List<ArtifactVersion> lookupVersions(String groupId, String artifactId, MavenProject project) {
        List<ArtifactVersion> versions = super.lookupVersions(groupId, artifactId, project);
        // Custom logic to filter or process the versions
        versions.removeIf(version -> version.toString().contains("SNAPSHOT"));
        return versions;
    }

    // You can add additional custom methods or override other methods from DefaultVersionsHelper
}
