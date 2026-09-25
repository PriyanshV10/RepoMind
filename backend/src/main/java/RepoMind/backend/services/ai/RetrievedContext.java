package RepoMind.backend.services.ai;

import RepoMind.backend.dto.CitationDto;

import java.util.List;

public record RetrievedContext(List<CitationDto> citations, String contextText) {}
