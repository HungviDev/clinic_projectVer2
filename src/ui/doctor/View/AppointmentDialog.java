package ui.doctor.View;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;
import ui.doctor.Controller.AppointmentController;
import ui.doctor.Model.DentalAppointmentModel;

public class AppointmentDialog {

    private final AppointmentController controller;
    private final DentalAppointmentModel appointment;
    private final AppointmentView viewComponent;
    private final int doctorId;

    public AppointmentDialog(AppointmentView viewComponent, DentalAppointmentModel appointment, int doctorId) {
        this.viewComponent = viewComponent;
        this.appointment = appointment;
        this.doctorId = doctorId;
        this.controller = new AppointmentController();

        if (appointment != null) {
            showQuickOptions();
        } else {
            showAddAppointmentForm();
        }
    }

    // ==========================================
    // 1. FORM THÊM MỚI LỊCH HẸN (NHẬP TỰ DO)
    // ==========================================
 private void showAddAppointmentForm() {
        // 1. Danh sách bệnh nhân
        java.util.List<String> patientNames = controller.getPatientNamesByDoctor(this.doctorId);
        JComboBox<String> comboPatient = new JComboBox<>(patientNames.toArray(new String[0]));
        comboPatient.setEditable(true);

        // 2. Danh sách dịch vụ (Vấn đề khám)
        java.util.List<String> serviceNames = controller.getAllServiceNames();
        JComboBox<String> comboProblem = new JComboBox<>(serviceNames.toArray(new String[0]));

        JTextField txtDate = new JTextField(); 
        JTextField txtTime = new JTextField(); 

        Object[] message = {
            "Chọn bệnh nhân:", comboPatient,
            "Ngày (yyyy-mm-dd):", txtDate,
            "Giờ (hh:mm):", txtTime,
            "Vấn đề khám:", comboProblem
        };

        int option = JOptionPane.showConfirmDialog(viewComponent, message, "Thêm lịch hẹn mới", JOptionPane.OK_CANCEL_OPTION);
        
        if (option == JOptionPane.OK_OPTION) {
            DentalAppointmentModel newAp = new DentalAppointmentModel();
            newAp.setPatientName((String) comboPatient.getSelectedItem());
            newAp.setAppointmentDate(txtDate.getText().trim());
            newAp.setAppointmentTime(txtTime.getText().trim());
            newAp.setProblem((String) comboProblem.getSelectedItem());
            newAp.setStatus("pending");

            // Thêm vào DB
            if (controller.addAppointment(newAp)) {
                // Tự động tìm ID vừa thêm để duyệt ngay
                int patientId = controller.findUserIdByName(newAp.getPatientName());
                int newId = getLastAppointmentId(patientId);

                if (newId != -1) {
                    // Cập nhật trạng thái thành 'approved' -> tự động kích hoạt tạo bệnh án
                    controller.updateStatus(newId, "approved", this.doctorId);
                }

                JOptionPane.showMessageDialog(viewComponent, "Đã thêm và tự động duyệt lịch khám!");
                viewComponent.loadAppointments();
            } else {
                JOptionPane.showMessageDialog(viewComponent, "Thêm thất bại! Kiểm tra lại thông tin.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Hàm lấy ID lịch vừa thêm để xử lý duyệt tự động
    private int getLastAppointmentId(int userId) {
        String sql = "SELECT TOP 1 id FROM appointments WHERE user_id = ? ORDER BY id DESC";
        try (Connection conn = config.DBConnection.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("id");
        } catch (Exception e) { e.printStackTrace(); }
        return -1;
    }

    // ==========================================
    // 2. XỬ LÝ DUYỆT/HỦY LỊCH CŨ
    // ==========================================
    private void showQuickOptions() {
        String status = appointment.getStatus(); // Lấy trạng thái hiện tại
        
        StringBuilder infoMessage = new StringBuilder("XỬ LÝ LỊCH HẸN\n\n");
        infoMessage.append("• Bệnh nhân: ").append(appointment.getPatientName()).append("\n");
        infoMessage.append("• Thời gian: ").append(appointment.getAppointmentTime()).append(" | ").append(appointment.getAppointmentDate()).append("\n");
        infoMessage.append("• Vấn đề: ").append(appointment.getProblem()).append("\n");
        infoMessage.append("• Trạng thái hiện tại: ").append(status).append("\n\n");
        infoMessage.append("Vui lòng chọn hành động:");

        // Tạo danh sách nút động dựa trên trạng thái
        java.util.List<String> optionsList = new java.util.ArrayList<>();
        if ("pending".equals(status)) {
            optionsList.add("Đã duyệt");
            optionsList.add("Hủy lịch hẹn");
        } else if ("approved".equals(status)) {
            optionsList.add("Đã hoàn thành");
            optionsList.add("Hủy lịch hẹn");
        }
        optionsList.add("Đóng");

        int selection = JOptionPane.showOptionDialog(
                viewComponent, infoMessage.toString(), "Xử lý lịch khám",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, 
                null, optionsList.toArray(), optionsList.get(0)
        );

        // Xử lý sự kiện sau khi chọn
        if (selection >= 0 && selection < optionsList.size()) {
            String choice = optionsList.get(selection);
            String newStatus = "";

            if ("Đã duyệt".equals(choice)) newStatus = "approved";
            else if ("Đã hoàn thành".equals(choice)) newStatus = "completed";
            else if ("Hủy lịch hẹn".equals(choice)) newStatus = "reject";
            else return; // Người dùng chọn "Đóng"

            // Thực hiện update vào Database
           if (controller.updateStatus(appointment.getId(), newStatus, this.doctorId)) {
                // Ép sang tiếng Việt ở đây
                String statusVi = "";
                switch (newStatus) {
                    case "approved" -> statusVi = "Đã duyệt";
                    case "completed" -> statusVi = "Đã hoàn thành";
                    case "reject" -> statusVi = "Đã hủy";
                    default -> statusVi = newStatus;
                }
                
                JOptionPane.showMessageDialog(viewComponent, "Cập nhật thành công sang trạng thái: " + statusVi);
                viewComponent.loadAppointments();
                
                // Nếu vừa duyệt xong thì refresh bệnh án
                if ("approved".equals(newStatus)) {
                    MedicalRecordView.refreshData();
                }
            } else {
                JOptionPane.showMessageDialog(viewComponent, "Thao tác thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    }
