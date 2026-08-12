package com.binarybrains.slms.customer.dto;

import com.binarybrains.slms.customer.model.Customer;

import java.time.LocalDateTime;

public class CustomerResponse {
    private String id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String customerType;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static CustomerResponse fromCustomer(Customer c) {
        CustomerResponse r = new CustomerResponse();
        r.id = c.getId(); r.name = c.getName(); r.email = c.getEmail();
        r.phone = c.getPhone(); r.address = c.getAddress();
        r.customerType = c.getCustomerType(); r.active = c.isActive();
        r.createdAt = c.getCreatedAt(); r.updatedAt = c.getUpdatedAt();
        return r;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getCustomerType() { return customerType; }
    public void setCustomerType(String customerType) { this.customerType = customerType; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
