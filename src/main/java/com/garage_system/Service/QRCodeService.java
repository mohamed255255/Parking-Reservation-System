package com.garage_system.Service;

import com.garage_system.DTO.request.SlotDto;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.RenderedImage;
import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.zxing.MultiFormatReader;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import java.io.ByteArrayInputStream;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;

@Service
public class QRCodeService {
   
    @Value("${file.qrcode-dir}")
    private String qrCodeDirectory ;

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
            
            /// check that QR code is valid :
            /// 1. decode extract garage_id , slot_id from garage##,slot##.png
           

            try {
                Result result = reader.decode(bitmap);
                return result.getText();
            } catch (ReaderException e) {
                return "Error reading QR code";
            }
    }


    public String saveQRCodeImage(SlotDto slotDto) throws IOException, WriterException {
        
        String toBeEncodedText = "G" + slotDto.getGarage_id() + "_S" + slotDto.getSlot_number();
        
        // Create QR code directory if it doesn't exist
        Path qrCodeDir = Paths.get(qrCodeDirectory).toAbsolutePath().normalize();
        Files.createDirectories(qrCodeDir);
        
        // Generate
        String fileName = "qrcode_garage" + slotDto.getGarage_id() + 
                        "_slot" + slotDto.getSlot_number() + ".png";
        
        Path targetLocation = qrCodeDir.resolve(fileName);

        if (!Files.exists(targetLocation)) {
            byte[] qrCodeBytes = generateQRCode(toBeEncodedText);
            Files.write(targetLocation, qrCodeBytes);
        }
        return qrCodeDirectory + fileName;
    
    }
}