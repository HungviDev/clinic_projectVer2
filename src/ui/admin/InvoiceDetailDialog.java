package ui.admin;

import controller.admin.PaymentController;
import model.admin.PaymentModel;
import model.admin.PaymentSummaryModel;
import ui.admin.form.PaymentEditForm;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileOutputStream;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class InvoiceDetailDialog extends JDialog {

    private final Color BACKGROUND_COLOR = new Color(240, 245, 250);
    private final Color PRIMARY_COLOR = new Color(0, 102, 204);
    private final Color SUCCESS_COLOR = new Color(46, 204, 113);
    private final Color DANGER_COLOR = new Color(231, 76, 60);

    private JTable table;
    private DefaultTableModel model;
    private PaymentSummaryModel summary;
    private PaymentController paymentController = new PaymentController();

    public InvoiceDetailDialog(JFrame parent, PaymentSummaryModel summary) {
        super(parent, "Chi tiết hóa đơn bệnh nhân: " + summary.getPatientName(), true);
        this.summary = summary;

        setSize(900, 600);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BACKGROUND_COLOR);

        // TOP PANEL: Summary Info
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(BACKGROUND_COLOR);
        topPanel.setBorder(new EmptyBorder(20, 20, 10, 20));

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        JPanel infoPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        infoPanel.setBackground(BACKGROUND_COLOR);
        infoPanel.add(new JLabel("Bệnh nhân: " + summary.getPatientName()));
        infoPanel.add(new JLabel("Tổng tiền: " + currencyFormat.format(summary.getTotalAmount())));
        infoPanel.add(new JLabel("Đã thanh toán: " + currencyFormat.format(summary.getPaidAmount())));
        
        JLabel lblUnpaid = new JLabel("Còn nợ: " + currencyFormat.format(summary.getUnpaidAmount()));
        lblUnpaid.setForeground(summary.getUnpaidAmount() > 0 ? DANGER_COLOR : new Color(46, 204, 113));
        infoPanel.add(lblUnpaid);

        topPanel.add(infoPanel, BorderLayout.WEST);

        // BUTTONS
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        JButton btnExport = createButton("Tải hóa đơn", SUCCESS_COLOR);
        btnExport.setPreferredSize(new Dimension(130, 40));
        JButton btnUpdate = createButton("Sửa", PRIMARY_COLOR);
        JButton btnDelete = createButton("Xóa", DANGER_COLOR);

        buttonPanel.add(btnExport);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);

        topPanel.add(buttonPanel, BorderLayout.EAST);

        // TABLE
        String[] columns = {
                "ID",
                "Số tiền (VND)",
                "Phương thức",
                "Trạng thái",
                "Ngày tạo",
                "Tên giai đoạn"
        };

        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        table.setRowHeight(38);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new EmptyBorder(10, 20, 20, 20));

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // EVENTS
        btnExport.addActionListener(e -> {
            exportToPDF();
        });

        btnUpdate.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn cần sửa");
                return;
            }
            int id = Integer.parseInt(table.getValueAt(row, 0).toString());
            PaymentEditForm form = new PaymentEditForm(parent, id);
            form.setVisible(true);
            if (form.isSaved()) {
                loadPayments();
            }
        });

        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn cần xóa");
                return;
            }
            int id = Integer.parseInt(table.getValueAt(row, 0).toString());
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Bạn có chắc chắn muốn xóa hóa đơn này?",
                    "Xác nhận xóa",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                boolean result = paymentController.deletePayment(id);
                if (result) {
                    JOptionPane.showMessageDialog(this, "Xóa thành công");
                    loadPayments();
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa thất bại");
                }
            }
        });

        loadPayments();
    }

    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(100, 40));
        return button;
    }

    private void exportToPDF() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn nơi lưu hóa đơn");
        fileChooser.setFileFilter(new FileNameExtensionFilter("PDF Documents", "pdf"));
        fileChooser.setSelectedFile(new File("HoaDon_" + summary.getPatientName().replaceAll(" ", "") + ".pdf"));

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();
            if (!filePath.toLowerCase().endsWith(".pdf")) {
                filePath += ".pdf";
            }

            try {
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(filePath));
                document.open();

                // Font for Vietnamese
                String fontPath = "C:\\Windows\\Fonts\\arial.ttf";
                File fontFile = new File(fontPath);
                BaseFont bf;
                if (fontFile.exists()) {
                    bf = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                } else {
                    bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
                }
                
                com.itextpdf.text.Font titleFont = new com.itextpdf.text.Font(bf, 20, com.itextpdf.text.Font.BOLD);
                com.itextpdf.text.Font headerFont = new com.itextpdf.text.Font(bf, 12, com.itextpdf.text.Font.BOLD);
                com.itextpdf.text.Font normalFont = new com.itextpdf.text.Font(bf, 12, com.itextpdf.text.Font.NORMAL);

                // Title
                Paragraph title = new Paragraph("HÓA ĐƠN DỊCH VỤ NHA KHOA", titleFont);
                title.setAlignment(Element.ALIGN_CENTER);
                title.setSpacingAfter(20);
                document.add(title);

                // Info
                NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                document.add(new Paragraph("Bệnh nhân: " + summary.getPatientName(), normalFont));
                document.add(new Paragraph("Tổng tiền: " + currencyFormat.format(summary.getTotalAmount()), normalFont));
                document.add(new Paragraph("Đã thanh toán: " + currencyFormat.format(summary.getPaidAmount()), normalFont));
                document.add(new Paragraph("Còn nợ: " + currencyFormat.format(summary.getUnpaidAmount()), normalFont));
                document.add(new Paragraph(" "));
                document.add(new Paragraph("Chi tiết các đợt thanh toán:", headerFont));
                document.add(new Paragraph(" "));

                // Table
                PdfPTable pdfTable = new PdfPTable(table.getColumnCount());
                pdfTable.setWidthPercentage(100);

                // Headers
                for (int i = 0; i < table.getColumnCount(); i++) {
                    PdfPCell cell = new PdfPCell(new Phrase(table.getColumnName(i), headerFont));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfTable.addCell(cell);
                }

                // Data
                for (int i = 0; i < table.getRowCount(); i++) {
                    for (int j = 0; j < table.getColumnCount(); j++) {
                        String value = table.getValueAt(i, j) != null ? table.getValueAt(i, j).toString() : "";
                        PdfPCell cell = new PdfPCell(new Phrase(value, normalFont));
                        pdfTable.addCell(cell);
                    }
                }

                document.add(pdfTable);
                document.close();

                JOptionPane.showMessageDialog(this, "Xuất hóa đơn thành công!");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi xuất hóa đơn: " + ex.getMessage());
            }
        }
    }

    private void loadPayments() {
        model.setRowCount(0);
        List<PaymentModel> list = paymentController.getPaymentsByUserId(summary.getUserId());
        list.forEach(payment -> {
            model.addRow(new Object[]{
                    payment.getId(),
                    payment.getAmount(),
                    payment.getMethod(),
                    payment.getStatus(),
                    payment.getCreatedAt(),
                    payment.getTreatmentStageName() == null ? "Không có" : payment.getTreatmentStageName()
            });
        });
    }
}
