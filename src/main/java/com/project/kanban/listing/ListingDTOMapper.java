package com.project.kanban.listing;

import org.springframework.stereotype.Service;

import java.util.function.Function;
@Service
public class ListingDTOMapper implements Function<Listing, ListingDTO> {
    @Override
    public ListingDTO apply(Listing listing){
        return new ListingDTO(
                listing.getId(),
                listing.getTitle(),
                listing.getType().toString(),
                listing.isArchived(),
                listing.getUpdatedAt(),
                listing.getCreatedAt(),
                listing.getBoard().getId()
        );
    }
}
