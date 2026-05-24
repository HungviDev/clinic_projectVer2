package ui.doctor.View;

import javax.swing.*;
import ui.doctor.Controller.AppointmentController;
import ui.doctor.Model.DentalAppointmentModel;

public class AppointmentDialog {

    private AppointmentController controller;
    private DentalAppointmentModel appointment;
    private AppointmentView viewComponent;
    private int doctorId; // Thêm biến toàn cục để lưu ID tài khoản bác sĩ đang xử lý

    // Cập nhật Constructor nhận thêm tham số int doctorId từ AppointmentView truyền sang
    public AppointmentDialog(AppointmentView viewComponent, DentalAppointmentModel appointment, int doctorId) {
        this.viewComponent = viewComponent;
        this.appointment = appointment;
        this.doctorId = doctorId; // Gán dữ liệu vào biến toàn cục của class
        this.controller = new AppointmentController();

        if (appointment != null) {
            showQuickOptions();
        }
    }

    // Hiển thị hộp thoại tùy chọn hành động Duyệt hoặc Hủy cực kỳ ngắn gọn
    private void showQuickOptions() {
        String infoMessage = String.format(
                "YÊU CẦU LỊCH HẸN TỪ BỆNH NHÂN\n\n" +
                "• Bệnh nhân: %s\n" +
                "• Thời gian: %s ngày %s\n" +
                "• Vấn đề khám: %s\n\n" +
                "Vui lòng chọn hành động phê duyệt:",
                appointment.getPatientName(),
                appointment.getAppointmentTime(),
                appointment.getAppointmentDate(),
                appointment.getProblem()
        );

        // Định nghĩa mảng 3 nút hành động trực tiếp
        Object[] options = {"Duyệt lịch hẹn", "Hủy lịch hẹn", "Đóng"};

        int selection = JOptionPane.showOptionDialog(
                viewComponent,
                infoMessage,
                "Xử lý lịch khám nhanh",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        boolean success = false;
        String databaseStatus = "";

        // Bổ sung tham số thứ 3 (this.doctorId) khi gọi sang hàm updateStatus của Controller
        if (selection == JOptionPane.YES_OPTION) {
            databaseStatus = "approved"; // Duyệt lịch
            success = controller.updateStatus(appointment.getId(), databaseStatus, this.doctorId);
            // Trong file AppointmentDialog.java

// ... (các đoạn code phía trên giữ nguyên)

if (selection == JOptionPane.YES_OPTION) {
    databaseStatus = "approved"; // Duyệt lịch
    success = controller.updateStatus(appointment.getId(), databaseStatus, this.doctorId);
    
    // Đặt lệnh kích hoạt vào đây sau khi update status thành công:
    if (success) {
        controller.handleAppointmentCompletion(appointment.getId()); // <--- THÊM Ở ĐÂY
    }
} else if (selection == JOptionPane.NO_OPTION) {
    databaseStatus = "reject";   // Hủy lịch
    success = controller.updateStatus(appointment.getId(), databaseStatus, this.doctorId);
}

// ... (các đoạn code phía dưới giữ nguyên)
        } else if (selection == JOptionPane.NO_OPTION) {
            databaseStatus = "reject";   // Hủy lịch
            success = controller.updateStatus(appointment.getId(), databaseStatus, this.doctorId);
        }

        // Nếu người dùng chọn Duyệt hoặc Hủy, xử lý và làm mới bảng ngay lập tức
        if (selection == JOptionPane.YES_OPTION || selection == JOptionPane.NO_OPTION) {
            if (success) {
                JOptionPane.showMessageDialog(viewComponent, "Đã cập nhật trạng thái lịch khám thành công!");
                viewComponent.loadAppointments(); // Tải lại bảng ngay khi chọn xong
            } else {
                JOptionPane.showMessageDialog(viewComponent, "Thao tác cơ sở dữ liệu thất bại!", "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}