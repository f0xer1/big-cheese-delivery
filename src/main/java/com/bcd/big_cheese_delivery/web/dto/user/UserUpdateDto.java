package com.bcd.big_cheese_delivery.web.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDto {
  @NotBlank(message = "Specify username")
  @Pattern(regexp = "^\\w+$", message = "You can use a-z, 0-9 and underscores")
  @Size(min = 4, max = 32, message = "Enter at least 4 and less than 32 characters")
  private String username;
  @NotBlank(message = "Specify first name")
  private String firstName;
  @NotBlank(message = "Specify last name")
  private String lastName;
  @NotBlank(message = "Specify email")
  @Email(message = "Enter correct email")
  private String email;
}
