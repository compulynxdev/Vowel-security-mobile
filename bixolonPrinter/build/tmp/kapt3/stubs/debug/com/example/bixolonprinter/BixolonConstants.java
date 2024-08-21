package com.example.bixolonprinter;

import java.lang.System;

@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b|\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001a\u0010\t\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u0006\"\u0004\b\u000b\u0010\bR\u001a\u0010\f\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u0006\"\u0004\b\u000e\u0010\bR\u001a\u0010\u000f\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\u0006\"\u0004\b\u0011\u0010\bR\u001a\u0010\u0012\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u0006\"\u0004\b\u0014\u0010\bR\u001a\u0010\u0015\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u0006\"\u0004\b\u0017\u0010\bR\u001a\u0010\u0018\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\u0006\"\u0004\b\u001a\u0010\bR\u001a\u0010\u001b\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001c\u0010\u0006\"\u0004\b\u001d\u0010\bR\u001a\u0010\u001e\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001f\u0010\u0006\"\u0004\b \u0010\bR\u001a\u0010!\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\"\u0010\u0006\"\u0004\b#\u0010\bR\u001a\u0010$\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b%\u0010\u0006\"\u0004\b&\u0010\bR\u001a\u0010\'\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b(\u0010\u0006\"\u0004\b)\u0010\bR\u001a\u0010*\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b+\u0010\u0006\"\u0004\b,\u0010\bR\u001a\u0010-\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b.\u0010\u0006\"\u0004\b/\u0010\bR\u001a\u00100\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b1\u0010\u0006\"\u0004\b2\u0010\bR\u001a\u00103\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b4\u0010\u0006\"\u0004\b5\u0010\bR\u001a\u00106\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b7\u0010\u0006\"\u0004\b8\u0010\bR\u001a\u00109\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b:\u0010\u0006\"\u0004\b;\u0010\bR\u001a\u0010<\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b=\u0010\u0006\"\u0004\b>\u0010\bR\u001a\u0010?\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b@\u0010\u0006\"\u0004\bA\u0010\bR\u001a\u0010B\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bC\u0010\u0006\"\u0004\bD\u0010\bR\u001a\u0010E\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bF\u0010\u0006\"\u0004\bG\u0010\bR\u001a\u0010H\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bI\u0010\u0006\"\u0004\bJ\u0010\bR\u001a\u0010K\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bL\u0010\u0006\"\u0004\bM\u0010\bR\u001a\u0010N\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bO\u0010\u0006\"\u0004\bP\u0010\bR\u001a\u0010Q\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bR\u0010\u0006\"\u0004\bS\u0010\bR\u001a\u0010T\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bU\u0010\u0006\"\u0004\bV\u0010\bR\u001a\u0010W\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bX\u0010\u0006\"\u0004\bY\u0010\bR\u0014\u0010Z\u001a\u00020\u0004X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b[\u0010\u0006R\u0014\u0010\\\u001a\u00020\u0004X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b]\u0010\u0006R\u0014\u0010^\u001a\u00020\u0004X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b_\u0010\u0006R\u0014\u0010`\u001a\u00020\u0004X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\ba\u0010\u0006R\u0014\u0010b\u001a\u00020\u0004X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\bc\u0010\u0006R\u0014\u0010d\u001a\u00020\u0004X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\be\u0010\u0006R\u0014\u0010f\u001a\u00020\u0004X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\bg\u0010\u0006R\u0014\u0010h\u001a\u00020\u0004X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\bi\u0010\u0006R\u0014\u0010j\u001a\u00020\u0004X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\bk\u0010\u0006R\u0014\u0010l\u001a\u00020\u0004X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\bm\u0010\u0006R\u0014\u0010n\u001a\u00020\u0004X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\bo\u0010\u0006R\u0014\u0010p\u001a\u00020\u0004X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\bq\u0010\u0006R\u0014\u0010r\u001a\u00020\u0004X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\bs\u0010\u0006R\u0014\u0010t\u001a\u00020\u0004X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\bu\u0010\u0006R\u0014\u0010v\u001a\u00020\u0004X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\bw\u0010\u0006R\u0014\u0010x\u001a\u00020\u0004X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\by\u0010\u0006R\u0014\u0010z\u001a\u00020\u0004X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b{\u0010\u0006R\u0014\u0010|\u001a\u00020\u0004X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b}\u0010\u0006R\u0014\u0010~\u001a\u00020\u0004X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u007f\u0010\u0006\u00a8\u0006\u0080\u0001"}, d2 = {"Lcom/example/bixolonprinter/BixolonConstants;", "", "()V", "ALIGNMENT_CENTER", "", "getALIGNMENT_CENTER", "()I", "setALIGNMENT_CENTER", "(I)V", "ALIGNMENT_LEFT", "getALIGNMENT_LEFT", "setALIGNMENT_LEFT", "ALIGNMENT_RIGHT", "getALIGNMENT_RIGHT", "setALIGNMENT_RIGHT", "ATTRIBUTE_BOLD", "getATTRIBUTE_BOLD", "setATTRIBUTE_BOLD", "ATTRIBUTE_FONT_A", "getATTRIBUTE_FONT_A", "setATTRIBUTE_FONT_A", "ATTRIBUTE_FONT_B", "getATTRIBUTE_FONT_B", "setATTRIBUTE_FONT_B", "ATTRIBUTE_FONT_C", "getATTRIBUTE_FONT_C", "setATTRIBUTE_FONT_C", "ATTRIBUTE_FONT_D", "getATTRIBUTE_FONT_D", "setATTRIBUTE_FONT_D", "ATTRIBUTE_NORMAL", "getATTRIBUTE_NORMAL", "setATTRIBUTE_NORMAL", "ATTRIBUTE_REVERSE", "getATTRIBUTE_REVERSE", "setATTRIBUTE_REVERSE", "ATTRIBUTE_UNDERLINE", "getATTRIBUTE_UNDERLINE", "setATTRIBUTE_UNDERLINE", "BARCODE_HRI_ABOVE", "getBARCODE_HRI_ABOVE", "setBARCODE_HRI_ABOVE", "BARCODE_HRI_BELOW", "getBARCODE_HRI_BELOW", "setBARCODE_HRI_BELOW", "BARCODE_HRI_NONE", "getBARCODE_HRI_NONE", "setBARCODE_HRI_NONE", "BARCODE_TYPE_Codabar", "getBARCODE_TYPE_Codabar", "setBARCODE_TYPE_Codabar", "BARCODE_TYPE_Code128", "getBARCODE_TYPE_Code128", "setBARCODE_TYPE_Code128", "BARCODE_TYPE_Code39", "getBARCODE_TYPE_Code39", "setBARCODE_TYPE_Code39", "BARCODE_TYPE_Code93", "getBARCODE_TYPE_Code93", "setBARCODE_TYPE_Code93", "BARCODE_TYPE_DATAMATRIX", "getBARCODE_TYPE_DATAMATRIX", "setBARCODE_TYPE_DATAMATRIX", "BARCODE_TYPE_EAN128", "getBARCODE_TYPE_EAN128", "setBARCODE_TYPE_EAN128", "BARCODE_TYPE_EAN13", "getBARCODE_TYPE_EAN13", "setBARCODE_TYPE_EAN13", "BARCODE_TYPE_EAN8", "getBARCODE_TYPE_EAN8", "setBARCODE_TYPE_EAN8", "BARCODE_TYPE_ITF", "getBARCODE_TYPE_ITF", "setBARCODE_TYPE_ITF", "BARCODE_TYPE_MAXICODE", "getBARCODE_TYPE_MAXICODE", "setBARCODE_TYPE_MAXICODE", "BARCODE_TYPE_PDF417", "getBARCODE_TYPE_PDF417", "setBARCODE_TYPE_PDF417", "BARCODE_TYPE_QRCODE", "getBARCODE_TYPE_QRCODE", "setBARCODE_TYPE_QRCODE", "BARCODE_TYPE_UPCA", "getBARCODE_TYPE_UPCA", "setBARCODE_TYPE_UPCA", "BARCODE_TYPE_UPCE", "getBARCODE_TYPE_UPCE", "setBARCODE_TYPE_UPCE", "DONE_PRINTING", "getDONE_PRINTING", "ERROR_PRINTER_COVER_OPEN", "getERROR_PRINTER_COVER_OPEN", "ERROR_PRINTER_EMPTY", "getERROR_PRINTER_EMPTY", "ERROR_PRINTER_OFFLINE", "getERROR_PRINTER_OFFLINE", "PRINTER_BATTERY_LOW", "getPRINTER_BATTERY_LOW", "PRINTER_BATTERY_NORMAL", "getPRINTER_BATTERY_NORMAL", "PRINTER_CONNECTED", "getPRINTER_CONNECTED", "PRINTER_CONNECTING", "getPRINTER_CONNECTING", "PRINTER_CONNECTION_FAILED", "getPRINTER_CONNECTION_FAILED", "PRINTER_COVER_OK", "getPRINTER_COVER_OK", "PRINTER_COVER_OPEN", "getPRINTER_COVER_OPEN", "PRINTER_IDLE", "getPRINTER_IDLE", "PRINTER_OFFLINE", "getPRINTER_OFFLINE", "PRINTER_ONLINE", "getPRINTER_ONLINE", "PRINTER_POWER_OFF", "getPRINTER_POWER_OFF", "PRINTER_POWER_ON", "getPRINTER_POWER_ON", "PRINTER_RECEIPT_PAPER_EMPTY", "getPRINTER_RECEIPT_PAPER_EMPTY", "PRINTER_RECEIPT_PAPER_NEARLY_EMPTY", "getPRINTER_RECEIPT_PAPER_NEARLY_EMPTY", "PRINTER_RECEIPT_PAPER_OK", "getPRINTER_RECEIPT_PAPER_OK", "bixolonPrinter_debug"})
public final class BixolonConstants {
    @org.jetbrains.annotations.NotNull
    public static final com.example.bixolonprinter.BixolonConstants INSTANCE = null;
    private static final int PRINTER_CONNECTED = 0;
    private static final int PRINTER_CONNECTION_FAILED = 1;
    private static final int PRINTER_CONNECTING = 2;
    private static final int DONE_PRINTING = 3;
    private static final int PRINTER_POWER_OFF = 2004;
    private static final int PRINTER_POWER_ON = 2001;
    private static final int PRINTER_COVER_OPEN = 11;
    private static final int PRINTER_COVER_OK = 12;
    private static final int PRINTER_RECEIPT_PAPER_EMPTY = 24;
    private static final int PRINTER_RECEIPT_PAPER_NEARLY_EMPTY = 25;
    private static final int PRINTER_RECEIPT_PAPER_OK = 26;
    private static final int PRINTER_IDLE = 1001;
    private static final int PRINTER_OFFLINE = 53;
    private static final int PRINTER_ONLINE = 54;
    private static final int PRINTER_BATTERY_NORMAL = 55;
    private static final int PRINTER_BATTERY_LOW = 56;
    private static final int ERROR_PRINTER_OFFLINE = 217;
    private static final int ERROR_PRINTER_EMPTY = 203;
    private static final int ERROR_PRINTER_COVER_OPEN = 201;
    private static int ALIGNMENT_LEFT = 1;
    private static int ALIGNMENT_CENTER = 2;
    private static int ALIGNMENT_RIGHT = 4;
    private static int ATTRIBUTE_NORMAL = 0;
    private static int ATTRIBUTE_FONT_A = 1;
    private static int ATTRIBUTE_FONT_B = 2;
    private static int ATTRIBUTE_FONT_C = 4;
    private static int ATTRIBUTE_BOLD = 8;
    private static int ATTRIBUTE_UNDERLINE = 16;
    private static int ATTRIBUTE_REVERSE = 32;
    private static int ATTRIBUTE_FONT_D = 64;
    private static int BARCODE_TYPE_UPCA = jpos.POSPrinterConst.PTR_BCS_UPCA;
    private static int BARCODE_TYPE_UPCE = jpos.POSPrinterConst.PTR_BCS_UPCE;
    private static int BARCODE_TYPE_EAN8 = jpos.POSPrinterConst.PTR_BCS_EAN8;
    private static int BARCODE_TYPE_EAN13 = jpos.POSPrinterConst.PTR_BCS_EAN13;
    private static int BARCODE_TYPE_ITF = jpos.POSPrinterConst.PTR_BCS_ITF;
    private static int BARCODE_TYPE_Codabar = jpos.POSPrinterConst.PTR_BCS_Codabar;
    private static int BARCODE_TYPE_Code39 = jpos.POSPrinterConst.PTR_BCS_Code39;
    private static int BARCODE_TYPE_Code93 = jpos.POSPrinterConst.PTR_BCS_Code93;
    private static int BARCODE_TYPE_Code128 = jpos.POSPrinterConst.PTR_BCS_Code128;
    private static int BARCODE_TYPE_PDF417 = jpos.POSPrinterConst.PTR_BCS_PDF417;
    private static int BARCODE_TYPE_MAXICODE = jpos.POSPrinterConst.PTR_BCS_MAXICODE;
    private static int BARCODE_TYPE_DATAMATRIX = jpos.POSPrinterConst.PTR_BCS_DATAMATRIX;
    private static int BARCODE_TYPE_QRCODE = jpos.POSPrinterConst.PTR_BCS_QRCODE;
    private static int BARCODE_TYPE_EAN128 = jpos.POSPrinterConst.PTR_BCS_EAN128;
    private static int BARCODE_HRI_NONE = jpos.POSPrinterConst.PTR_BC_TEXT_NONE;
    private static int BARCODE_HRI_ABOVE = jpos.POSPrinterConst.PTR_BC_TEXT_ABOVE;
    private static int BARCODE_HRI_BELOW = jpos.POSPrinterConst.PTR_BC_TEXT_BELOW;
    
    private BixolonConstants() {
        super();
    }
    
    public final int getPRINTER_CONNECTED() {
        return 0;
    }
    
    public final int getPRINTER_CONNECTION_FAILED() {
        return 0;
    }
    
    public final int getPRINTER_CONNECTING() {
        return 0;
    }
    
    public final int getDONE_PRINTING() {
        return 0;
    }
    
    public final int getPRINTER_POWER_OFF() {
        return 0;
    }
    
    public final int getPRINTER_POWER_ON() {
        return 0;
    }
    
    public final int getPRINTER_COVER_OPEN() {
        return 0;
    }
    
    public final int getPRINTER_COVER_OK() {
        return 0;
    }
    
    public final int getPRINTER_RECEIPT_PAPER_EMPTY() {
        return 0;
    }
    
    public final int getPRINTER_RECEIPT_PAPER_NEARLY_EMPTY() {
        return 0;
    }
    
    public final int getPRINTER_RECEIPT_PAPER_OK() {
        return 0;
    }
    
    public final int getPRINTER_IDLE() {
        return 0;
    }
    
    public final int getPRINTER_OFFLINE() {
        return 0;
    }
    
    public final int getPRINTER_ONLINE() {
        return 0;
    }
    
    public final int getPRINTER_BATTERY_NORMAL() {
        return 0;
    }
    
    public final int getPRINTER_BATTERY_LOW() {
        return 0;
    }
    
    public final int getERROR_PRINTER_OFFLINE() {
        return 0;
    }
    
    public final int getERROR_PRINTER_EMPTY() {
        return 0;
    }
    
    public final int getERROR_PRINTER_COVER_OPEN() {
        return 0;
    }
    
    public final int getALIGNMENT_LEFT() {
        return 0;
    }
    
    public final void setALIGNMENT_LEFT(int p0) {
    }
    
    public final int getALIGNMENT_CENTER() {
        return 0;
    }
    
    public final void setALIGNMENT_CENTER(int p0) {
    }
    
    public final int getALIGNMENT_RIGHT() {
        return 0;
    }
    
    public final void setALIGNMENT_RIGHT(int p0) {
    }
    
    public final int getATTRIBUTE_NORMAL() {
        return 0;
    }
    
    public final void setATTRIBUTE_NORMAL(int p0) {
    }
    
    public final int getATTRIBUTE_FONT_A() {
        return 0;
    }
    
    public final void setATTRIBUTE_FONT_A(int p0) {
    }
    
    public final int getATTRIBUTE_FONT_B() {
        return 0;
    }
    
    public final void setATTRIBUTE_FONT_B(int p0) {
    }
    
    public final int getATTRIBUTE_FONT_C() {
        return 0;
    }
    
    public final void setATTRIBUTE_FONT_C(int p0) {
    }
    
    public final int getATTRIBUTE_BOLD() {
        return 0;
    }
    
    public final void setATTRIBUTE_BOLD(int p0) {
    }
    
    public final int getATTRIBUTE_UNDERLINE() {
        return 0;
    }
    
    public final void setATTRIBUTE_UNDERLINE(int p0) {
    }
    
    public final int getATTRIBUTE_REVERSE() {
        return 0;
    }
    
    public final void setATTRIBUTE_REVERSE(int p0) {
    }
    
    public final int getATTRIBUTE_FONT_D() {
        return 0;
    }
    
    public final void setATTRIBUTE_FONT_D(int p0) {
    }
    
    public final int getBARCODE_TYPE_UPCA() {
        return 0;
    }
    
    public final void setBARCODE_TYPE_UPCA(int p0) {
    }
    
    public final int getBARCODE_TYPE_UPCE() {
        return 0;
    }
    
    public final void setBARCODE_TYPE_UPCE(int p0) {
    }
    
    public final int getBARCODE_TYPE_EAN8() {
        return 0;
    }
    
    public final void setBARCODE_TYPE_EAN8(int p0) {
    }
    
    public final int getBARCODE_TYPE_EAN13() {
        return 0;
    }
    
    public final void setBARCODE_TYPE_EAN13(int p0) {
    }
    
    public final int getBARCODE_TYPE_ITF() {
        return 0;
    }
    
    public final void setBARCODE_TYPE_ITF(int p0) {
    }
    
    public final int getBARCODE_TYPE_Codabar() {
        return 0;
    }
    
    public final void setBARCODE_TYPE_Codabar(int p0) {
    }
    
    public final int getBARCODE_TYPE_Code39() {
        return 0;
    }
    
    public final void setBARCODE_TYPE_Code39(int p0) {
    }
    
    public final int getBARCODE_TYPE_Code93() {
        return 0;
    }
    
    public final void setBARCODE_TYPE_Code93(int p0) {
    }
    
    public final int getBARCODE_TYPE_Code128() {
        return 0;
    }
    
    public final void setBARCODE_TYPE_Code128(int p0) {
    }
    
    public final int getBARCODE_TYPE_PDF417() {
        return 0;
    }
    
    public final void setBARCODE_TYPE_PDF417(int p0) {
    }
    
    public final int getBARCODE_TYPE_MAXICODE() {
        return 0;
    }
    
    public final void setBARCODE_TYPE_MAXICODE(int p0) {
    }
    
    public final int getBARCODE_TYPE_DATAMATRIX() {
        return 0;
    }
    
    public final void setBARCODE_TYPE_DATAMATRIX(int p0) {
    }
    
    public final int getBARCODE_TYPE_QRCODE() {
        return 0;
    }
    
    public final void setBARCODE_TYPE_QRCODE(int p0) {
    }
    
    public final int getBARCODE_TYPE_EAN128() {
        return 0;
    }
    
    public final void setBARCODE_TYPE_EAN128(int p0) {
    }
    
    public final int getBARCODE_HRI_NONE() {
        return 0;
    }
    
    public final void setBARCODE_HRI_NONE(int p0) {
    }
    
    public final int getBARCODE_HRI_ABOVE() {
        return 0;
    }
    
    public final void setBARCODE_HRI_ABOVE(int p0) {
    }
    
    public final int getBARCODE_HRI_BELOW() {
        return 0;
    }
    
    public final void setBARCODE_HRI_BELOW(int p0) {
    }
}