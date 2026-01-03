package com.parking_reservation_system.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bills")
@PreAuthorize("hasRole('ADMIN')")
public class BillController {
    /* 
    private final BillService billService;

    @Autowired
    public BillController(BillService billService) {
        this.billService = billService;
    }

    @PostMapping
    public ResponseEntity<Bill> createBill(@RequestBody Bill bill) {
        Bill created = billService.createBill(bill);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public List<Bill> getAllBills() {
        return billService.getAllBills();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bill> getBillById(@PathVariable int id) {
        return billService.getBillById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Bill> updateBill(@PathVariable int id, @RequestBody Bill bill) {
        Bill updated = billService.updateBill(id, bill);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBill(@PathVariable int id) {
        billService.deleteBill(id);
        return ResponseEntity.noContent().build();
    }*/
}
