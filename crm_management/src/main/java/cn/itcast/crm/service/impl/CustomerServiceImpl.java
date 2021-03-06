package cn.itcast.crm.service.impl;

import cn.itcast.crm.dao.CustomerRepository;
import cn.itcast.crm.domain.Customer;
import cn.itcast.crm.service.CustomerService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {


    // 注入DAO
    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public List<Customer> findNoAssociationCustomers() {
        // fixedAreaId is null
        return customerRepository.findByFixedAreaIdIsNull();
    }

    @Override
    public List<Customer> findHasAssociationFixedAreaCustomers(
            String fixedAreaId) {
        // fixedAreaId is ?
        return customerRepository.findByFixedAreaId(fixedAreaId);
    }

    @Override
    public void associationCustomersToFixedArea(String customerIdStr, String nocustomerIdStr,
                                                String fixedAreaId) {
        // 解除关联动作
        customerRepository.clearFixedAreaId(fixedAreaId);

        //解除关联
        if (StringUtils.isBlank(nocustomerIdStr)) {
            return;
        }
        String[] nocustomerIdArray = nocustomerIdStr.split(",");
        for (String idStr : nocustomerIdArray) {
            Integer id = Integer.parseInt(idStr);
            customerRepository.updateFixedAreaId(null, id);
        }
        // 切割字符串 1,2,3
        if (StringUtils.isBlank(customerIdStr)) {
            return;
        }
        String[] customerIdArray = customerIdStr.split(",");
        for (String idStr : customerIdArray) {
            Integer id = Integer.parseInt(idStr);
            customerRepository.updateFixedAreaId(fixedAreaId, id);
        }
    }

    @Override
    public void register(Customer customer) {
        customerRepository.save(customer);
    }

    @Override
    public void active(Customer customer) {
        customerRepository.updateType(customer.getTelephone());
    }

    @Override
    public Customer login(String telephone, String password) {
        return customerRepository.findByTelephoneAndPassword(telephone, password);
    }

}
