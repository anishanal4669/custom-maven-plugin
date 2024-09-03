package com.example;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.repository.metadata.Metadata;
import org.apache.maven.artifact.repository.metadata.RepositoryMetadataManager;
import org.apache.maven.artifact.repository.metadata.RepositoryMetadataResolutionException;
import org.apache.maven.repository.legacy.metadata.DefaultMetadataResolutionRequest;
import org.apache.maven.repository.legacy.metadata.MetadataResolutionRequest;
import org.apache.maven.repository.legacy.metadata.MetadataResolutionResult;
import org.apache.maven.repository.legacy.metadata.MetadataSource;
import org.apache.maven.repository.legacy.metadata.MavenMetadataSource;
import org.codehaus.plexus.component.annotations.Component;

import java.util.List;

@Component(role = MetadataSource.class, hint = "custom-metadata-source")
public class CustomMavenMetadataSource extends MavenMetadataSource {

    private final RepositoryMetadataManager repositoryMetadataManager;

    public CustomMavenMetadataSource(RepositoryMetadataManager repositoryMetadataManager) {
        super(repositoryMetadataManager);
        this.repositoryMetadataManager = repositoryMetadataManager;
    }

    public List<String> fetchArtifactVersions(Artifact artifact, List<ArtifactRepository> repositories) throws RepositoryMetadataResolutionException {
        MetadataResolutionRequest request = new DefaultMetadataResolutionRequest();
        request.setArtifact(artifact);
        request.setRemoteRepositories(repositories);
        request.setLocalRepository(getLocalRepository());

        MetadataResolutionResult result = repositoryMetadataManager.resolveMetadata(request);

        if (result.hasMetadata()) {
            Metadata metadata = result.getMetadata();
            return metadata.getVersioning().getVersions();
        }

        return null;
    }
}
