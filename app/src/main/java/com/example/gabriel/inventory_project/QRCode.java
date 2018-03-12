package com.example.gabriel.inventory_project;

/**
 * Created by gabriel on 12.03.18.
 */

public class QRCode {
    String qrcode;
    String id;

    public QRCode() {
    }

    public QRCode(String id, String qrcode) {
        this.id = id;
        this.qrcode = qrcode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }
}
