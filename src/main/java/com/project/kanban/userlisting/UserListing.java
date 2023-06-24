package com.project.kanban.userlisting;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "user_listing")
@Data
@NoArgsConstructor
@Table(uniqueConstraints={@UniqueConstraint(columnNames = {"user_id" , "listing_id"})})

public class UserListing {
    @Id
    @SequenceGenerator(name = "userlisting_sequence",
            sequenceName = "userlisting_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userlisting_sequence")
    @Column(name = "id")
    private long id;


    @Column(name = "user_id")
    private long userId;

    @Column(name = "listing_id")
    private long listingId;
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private UserListingRole role;

    public UserListing(long userId, long listingId, String role){
        this.userId = userId;
        this.listingId = listingId;
        this.role = UserListingRole.valueOf(role);
    }
}
