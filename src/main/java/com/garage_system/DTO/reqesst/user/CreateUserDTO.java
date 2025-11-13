package com.garage_system.DTO.reqesst.user;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;


@Setter 
@Getter
public class CreateUserDTO {
    private int id ;
    
    @NotBlank(message = "empty names are not allowed")
    private String name ;

    @Email(message="Email should be valid")
    private String email;


    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotEmpty
    private String password ;

   
    public CreateUserDTO(){

    } 

    public CreateUserDTO(String name , String email , String password ){
            this.name = name ;this.email = email ; this.password = password ;
    } 



  }
