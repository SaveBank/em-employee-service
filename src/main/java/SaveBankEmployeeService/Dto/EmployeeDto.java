package SaveBankEmployeeService.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto  {

    private Long id;

    @NotEmpty(message = "first name should not be null or empty")
    private String firstName;

    @NotEmpty(message = "last name should not be null or empty")
    private String lastName;

    @Email
    @NotEmpty(message = "email should not be null or empty")
    private String email;

    @NotEmpty(message = "department code should not be null or empty")
    private String departmentCode;

}
