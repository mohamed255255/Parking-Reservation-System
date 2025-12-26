package com.garage_system.Controller ;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.garage_system.Service.QRCodeService;
import com.google.zxing.WriterException;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;
import java.io.IOException;

@RestController
public class QRCodeController {

    private QRCodeService qrCodeService ;

    public QRCodeController(QRCodeService qrCodeService) {
        this.qrCodeService = qrCodeService;
    }
    

    /// this method Confrim the reservation after scaning the code
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/readQRCode")
    public ResponseEntity<String> readQRCode(@RequestParam("file") byte[] file) throws IOException {
        String result = qrCodeService.readQRCode(file);
        // front end should redirect to the payment page if the result 
        // is sucess and send reservation info in the body 
        // so front end use it for payment
        return ResponseEntity.ok(result);
    }
}