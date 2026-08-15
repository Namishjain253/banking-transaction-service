package org.hsbc.banking.customer.dto;

public class CustomerResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String mobileNumber;

    public CustomerResponse() {
        super();
    }

    public CustomerResponse(Long id, String firstName, String lastName,
                            String email, String mobileNumber) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.mobileNumber = mobileNumber;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }
}