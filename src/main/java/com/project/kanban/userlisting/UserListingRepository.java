package com.project.kanban.userlisting;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserListingRepository extends JpaRepository<UserListing, Long> {
    Optional<UserListing> findUserListingByUserIdAndListingId(long userId, long listingId);

}
