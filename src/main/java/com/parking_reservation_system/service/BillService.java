package com.parking_reservation_system.service;

import org.springframework.stereotype.Service;

@Service
public class BillService {
    /*
    private final BillRepository billRepository;

    @Autowired
    public BillService(BillRepository billRepository) {
        this.billRepository = billRepository;
    }

    public Bill createBill(Bill bill) {
        return billRepository.save(bill);
    }

    public List<Bill> getAllBills() {
        return billRepository.getAllBills();
    }

    public Optional<Bill> getBillById(int id) {
        return billRepository.findById(id);
    }

   public Bill updateBill(int id, Bill updatedBill) {
        return billRepository.findById(id)
            .map(existing -> {
                existing.setPrice(updatedBill.getPrice());
                existing.setVehicleNumber(updatedBill.getVehicleNumber());
                existing.setStartingTime(updatedBill.getStartingTime());
                existing.setEndingTime(updatedBill.getEndingTime());
                existing.setDate(updatedBill.getDate());
                existing.setUsers(updatedBill.getUsers());
                return billRepository.save(existing);
            })
            .orElseThrow(() -> new IllegalArgumentException("Bill with id " + id + " not found"));
    }

    public void deleteBill(int id) {
        billRepository.deleteById(id);
    }*/
} 