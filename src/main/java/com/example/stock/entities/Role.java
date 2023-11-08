package com.example.stock.entities;


import lombok.Data;

import javax.persistence.*;


/**
 * Simple domain object that represents application user's role - ADMIN, USER, etc.
 *
 * @author Eugene Suleimanov
 * @version 1.0
 */

@Entity
@Table(name = "stock_roles")
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;


    //    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(name = "user_roles3",
//            joinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")},
//            inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")})
//    private List<User> users;

}
