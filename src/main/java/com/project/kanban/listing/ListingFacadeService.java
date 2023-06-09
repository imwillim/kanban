package com.project.kanban.listing;

import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

public interface ListingFacadeService {
    List<ListingDTO> getListingsProcess(long workspaceId, long boardId);
    Optional<ListingDTO> getListingProcess(Authentication authentication, long workspaceId, long boardId, long listingId);

    Optional<ListingDTO> createListingProcess(Authentication authentication, long workspaceId, long boardId, ListingDTO listingDTO);

    Optional<ListingDTO> updateListingProcess(Authentication authentication, long workspaceId, long boardId, long listingId, ListingDTO listingDTO);

    void deleteListingProcess(Authentication authentication, long workspaceId, long boardId, long listingId);

    void assignListing(Authentication authentication,long boardId, long listingId);
}
