package com.project.kanban.listing;

import com.project.kanban.board.Board;

import java.util.List;
import java.util.Optional;

public interface ListingService {
    List<Listing> getListings(Board board);

    Optional<Listing> getListing(long boardId, long listingId);

    Listing createListing(ListingDTO listingDTO, Board board);

    Listing updateListing(Listing listing, ListingDTO listingDTO);

    void deleteListing(long listingId);
}
