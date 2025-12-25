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

    private final QRCodeService qrCodeService;

    public QRCodeController(QRCodeService qrCodeService) {
        this.qrCodeService = qrCodeService;
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/generateQRCode")
    public ResponseEntity<byte[]> generateQRCode(@RequestParam String text) throws IOException, WriterException {
        byte[] qrCode = qrCodeService.generateQRCode(text);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return ResponseEntity.ok().headers(headers).body(qrCode);
    }
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/readQRCode")
    public ResponseEntity<String> readQRCode(@RequestParam("file") byte[] file) throws IOException {
        String result = qrCodeService.readQRCode(file);
        return ResponseEntity.ok(result);
    }
}