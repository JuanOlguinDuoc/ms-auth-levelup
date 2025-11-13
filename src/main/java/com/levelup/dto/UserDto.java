package com.levelup.dto;

import com.levelup.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String run;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String role;

    public static UserDto fromEntity(User u){
        if ( u == null ) {
            return null;
        }
        return UserDto.builder()
                .id( u.getId() )
                .run (u.getRun())
                .firstName( u.getFirstName() )
                .lastName( u.getLastName() )
                .email( u.getEmail() )
                .password( u.getPassword() )
                .role( u.getRole() )
                .build();
    }

    public static User toEntity(UserDto dto){
        if ( dto == null ) {
            return null;
        }
        return User.builder()
                .id( dto.getId() )
                .firstName( dto.getFirstName() )
                .lastName( dto.getLastName() )
                .email( dto.getEmail() )
                .password( dto.getPassword() )
                .role( dto.getRole() )
                .build();
    }

}
