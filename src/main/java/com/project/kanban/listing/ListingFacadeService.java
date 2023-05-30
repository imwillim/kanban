package com.project.kanban.listing;

import java.util.List;
import java.util.Optional;

public interface ListingFacadeService {
    List<ListingDTO> getListingsProcess(long workspaceId, long boardId);
    Optional<ListingDTO> getListingProcess(long workspaceId, long boardId, long listingId);

    Optional<ListingDTO> createListingProcess(long workspaceId, long boardId, ListingDTO listingDTO);

    Optional<ListingDTO> updateListingProcess(long workspaceId, long boardId, long listingId, ListingDTO listingDTO);

    void deleteListingProcess(long workspaceId, long boardId, long listingId);
}
