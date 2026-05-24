package ui.doctor.Model;

public class DoctorDashboardModel {

    private int totalAppointmentsToday;
    private int totalPatientsTreating;
    private int completedCasesMonth;
    private double revenueToday;

    public DoctorDashboardModel() {
    }

    public DoctorDashboardModel(
            int totalAppointmentsToday,
            int totalPatientsTreating,
            int completedCasesMonth,
            double revenueToday
    ) {
        this.totalAppointmentsToday = totalAppointmentsToday;
        this.totalPatientsTreating = totalPatientsTreating;
        this.completedCasesMonth = completedCasesMonth;
        this.revenueToday = revenueToday;
    }

    public int getTotalAppointmentsToday() {
        return totalAppointmentsToday;
    }

    public void setTotalAppointmentsToday(int totalAppointmentsToday) {
        this.totalAppointmentsToday = totalAppointmentsToday;
    }

    public int getTotalPatientsTreating() {
        return totalPatientsTreating;
    }

    public void setTotalPatientsTreating(int totalPatientsTreating) {
        this.totalPatientsTreating = totalPatientsTreating;
    }

    public int getCompletedCasesMonth() {
        return completedCasesMonth;
    }

    public void setCompletedCasesMonth(int completedCasesMonth) {
        this.completedCasesMonth = completedCasesMonth;
    }

    public double getRevenueToday() {
        return revenueToday;
    }

    public void setRevenueToday(double revenueToday) {
        this.revenueToday = revenueToday;
    }
}