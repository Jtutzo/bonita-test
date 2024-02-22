package com.example.bonitatest.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;

@Path("users")
public class UserResource {

    private static final Logger LOG = LoggerFactory.getLogger(UserResource.class);

    private final UserRepository userRepository;

    @Inject
    public UserResource(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @POST
    @Consumes({APPLICATION_JSON, APPLICATION_XML})
    public Response create(@Context UriInfo uriInfo, UserDto user) {
        LOG.info("create user {}", user);
        var id = userRepository.create(user.toUser());
        var uri = UriBuilder.fromUri(uriInfo.getAbsolutePath()).path("{id}").build(id);
        return Response
                .created(uri)
                .build();
    }

    @GET
    @Path("/{id}")
    @Produces({APPLICATION_JSON, APPLICATION_XML})
    public UserDto findById(@PathParam("id") long id) {
        LOG.info("find user {}", id);
        return userRepository.findById(id)
                .map(UserDto::from)
                .orElseThrow(() -> new NotFoundException("user not found"));
    }

    @XmlRootElement(name = "user")
    public static class UserDto {

        @XmlElement(name = "id") private long id;
        @XmlElement(name = "username") private String username;
        @XmlElement(name = "firstName") private String firstName;
        @XmlElement(name = "lastName") private String lastName;

        public static UserDto from(User user) {
            var userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setUsername(user.getUsername());
            userDto.setFirstName(user.getFirstName());
            userDto.setLastName(user.getLastName());
            return userDto;
        }

        @JsonIgnore
        public User toUser() {
            var user = new User();
            user.setId(id);
            user.setUsername(username);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            return user;
        }

        public void setId(long id) {
            this.id = id;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

    }

}