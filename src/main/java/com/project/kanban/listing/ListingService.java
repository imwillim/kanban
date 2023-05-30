package com.project.kanban.listing;


import com.project.kanban.board.Board;

import java.util.List;
import java.util.Optional;

public interface ListingService {
    List<Listing> getListings(Board board);

    Optional<Listing> getListing(long listingId);

    Listing createListing(ListingDTO listingDTO, Board board);

    Listing updateListing(long listingId, ListingDTO listingDTO);

    void deleteListing(long listingId);

    Listing saveListing(Listing listing);
}
