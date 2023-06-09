package com.project.kanban.userlisting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserListingServiceImpl implements UserListingService {
    private final UserListingRepository listingAssigneeRepository;
    @Autowired
    public UserListingServiceImpl(UserListingRepository listingAssigneeRepository){
        this.listingAssigneeRepository = listingAssigneeRepository;
    }

    @Override
    public Optional<UserListing> getUserListingById(long listingId) {
        return listingAssigneeRepository.findById(listingId);
    }

    @Override
    public Optional<UserListing> getUserListingByUserAndListingIds(long userId, long listingId) {
        return listingAssigneeRepository.findUserListingByUserIdAndListingId(userId, listingId);
    }

    @Override
    public void createUserListing(long userId, long listingId, String role) {
        UserListing listingAssignee = new UserListing(userId, listingId, role);
        listingAssigneeRepository.save(listingAssignee);
    }

    @Override
    public String getRoleFromUserListing(long userId, long listingId){
        Optional<UserListing> userListing = getUserListingByUserAndListingIds(userId, listingId);
        if (userListing.isPresent()){
            return userListing.get().getRole().toString();
        }
        return "";
    }

    @Override
    public void setRoleUserListing(UserListing listingAssignee, String role) {
        listingAssignee.setRole(UserListingRole.valueOf(role.toUpperCase()));
        listingAssigneeRepository.save(listingAssignee);
    }
}
