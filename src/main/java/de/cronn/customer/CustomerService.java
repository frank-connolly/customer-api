package de.cronn.customer;

import de.cronn.customer.exception.ServiceException;
import lombok.AllArgsConstructor;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@ApplicationScoped
public class CustomerService {

    private CustomerRepository customerRepository;
    private CustomerMapper customerMapper;

    public List<Customer> findAll() {
        return customerRepository.findAll().stream()
                .map(customerMapper::toDomain)
                .collect(Collectors.toList());
    }

    public Optional<Customer> findById(Integer customerId) {
        return customerRepository.findByIdOptional(customerId)
                .map(customerMapper::toDomain);
    }

    @Transactional
    public Customer save(Customer customer) {
        CustomerEntity customerEntity = customerMapper.toEntity(customer);
        customerRepository.persist(customerEntity);
        return customerMapper.toDomain(customerEntity);
    }

    @Transactional
    public Customer update(Customer customer) {
        if(customer.getCustomerId() == null) {
            throw new ServiceException("Customer does not have a customerId");
        }
        Optional<CustomerEntity> optionalCustomer = customerRepository.findByIdOptional(customer.getCustomerId());
        if (optionalCustomer.isEmpty()) {
            throw new ServiceException(String.format("No Customer found for customerId[%s]", customer.getCustomerId()));
        }
        CustomerEntity entity = optionalCustomer.get();
        entity.setFirstName(customer.getFirstName());
        entity.setMiddleName(customer.getMiddleName());
        entity.setLastName(customer.getLastName());
        entity.setSuffix(customer.getSuffix());
        entity.setEmail(customer.getEmail());
        entity.setPhone(customer.getPhone());
        customerRepository.persist(entity);
        return customerMapper.toDomain(entity);
    }
}
