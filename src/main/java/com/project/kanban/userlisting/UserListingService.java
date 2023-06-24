package com.project.kanban.userlisting;

import java.util.Optional;

public interface UserListingService {
    Optional<UserListing> getUserListingById(long listingId);

    Optional<UserListing> getUserListingByUserAndListingIds(long userId, long listingId);

    void createUserListing(long userId, long listingId, String role);

    String getRoleFromUserListing(long userId, long listingId);

    void setRoleUserListing(UserListing listingAssignee, String role);
}
