package com.parking_reservation_system.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.parking_reservation_system.dto.request.SlotRequest;
import com.parking_reservation_system.exception.QRCodeGenerationException;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.imageio.ImageIO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class QRCodeService {

    @Value("${file.qrcode-dir}")
    private String qrCodeDirectory;

    public byte[] generateQRCode(String text) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 200, 200);

        BufferedImage bufferedImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
        bufferedImage.createGraphics().fillRect(0, 0, 200, 200);
        Graphics2D graphics = (Graphics2D) bufferedImage.getGraphics();
        graphics.setColor(Color.BLACK);

        for (int x = 0; x < 200; x++) {
            for (int y = 0; y < 200; y++) {
                if (bitMatrix.get(x, y)) {
                    graphics.fillRect(x, y, 1, 1);
                }
            }
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write((RenderedImage) bufferedImage, "png", baos);
        return baos.toByteArray();
    }

    public String readQRCode(byte[] imageBytes) throws IOException {

        ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes);
        BufferedImage bufferedImage = ImageIO.read(bais);

        BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        HybridBinarizer binarizer = new HybridBinarizer(source);
        com.google.zxing.BinaryBitmap bitmap = new com.google.zxing.BinaryBitmap(binarizer);
        MultiFormatReader reader = new MultiFormatReader();
        try {
            Result result = reader.decode(bitmap);
            return result.getText();
        } catch (ReaderException e) {
            return "Error reading QR code";
        }
    }

    public String saveQRCodeImage(SlotRequest SlotRequest) {
    String toBeEncodedText = "G" + SlotRequest.garage_id() + "_S" + SlotRequest.slot_number();
    Path qrCodeDir = Paths.get(qrCodeDirectory).toAbsolutePath().normalize();
    String fileName = "G" + SlotRequest.garage_id() + "_S" + SlotRequest.slot_number() + ".png";
    Path targetLocation = qrCodeDir.resolve(fileName);

    try {
        Files.createDirectories(qrCodeDir);
        if (!Files.exists(targetLocation)) {
            byte[] qrCodeBytes = generateQRCode(toBeEncodedText);
            Files.write(targetLocation, qrCodeBytes);
        }
        return qrCodeDirectory + fileName;
    } catch (IOException | WriterException e) {
        throw new QRCodeGenerationException("Failed to create QR code for the slot", e);
    }
   }
   
}
