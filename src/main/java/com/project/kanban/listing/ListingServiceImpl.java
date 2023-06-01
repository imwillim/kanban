package com.project.kanban.listing;

import com.project.kanban.board.Board;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ListingServiceImpl implements ListingService {
    private final ListingRepository listingRepository;

    public ListingServiceImpl(ListingRepository listingRepository){
        this.listingRepository = listingRepository;
    }

    @Override
    public Optional<Listing> getListing(long listingId){
        return listingRepository.findById(listingId);
    }

    @Override
    public List<Listing> getListings(Board board) {
        return listingRepository.getListingsByBoard(board);
    }

    @Override
    public Listing createListing(ListingDTO requestBody, Board board) {
        Listing listing = new Listing(requestBody.getTitle(), requestBody.getType(), board);
        return listingRepository.save(listing);
    }

    @Override
    public Listing updateListing(long listingId, ListingDTO requestBody) {
        Listing updatedListing = getListing(listingId).orElse(null);
        if (updatedListing != null){
            updatedListing.setTitle(requestBody.getTitle());
            updatedListing.setType(requestBody.getType());
            updatedListing.setUpdatedAt(LocalDateTime.now());
            return listingRepository.save(updatedListing);
        }
        return null;

    }

    @Override
    public void deleteListing(long listingId) {
        listingRepository.deleteById(listingId);
    }

    @Override
    public Listing saveListing(Listing listing) {
        return listingRepository.save(listing);
    }
}
