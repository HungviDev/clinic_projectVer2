package controller.admin;

import model.admin.ServiceModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.List;

import config.DBConnection;

public class ServiceController {

    // =====================================
    // GET ALL SERVICE
    // =====================================
    public List<ServiceModel> getAllService() {

        List<ServiceModel> serviceList =
                new ArrayList<>();

        try {

            Connection conn =
                    DBConnection.getConnection();

            String sql =
                    "SELECT * FROM services";

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ResultSet rs =
                    ps.executeQuery();

            while (rs.next()) {

                ServiceModel service =
                        new ServiceModel();

                service.setId(
                        rs.getInt("id")
                );

                service.setName(
                        rs.getString("name")
                );

                service.setDescription(
                        rs.getString("description")
                );

                service.setPrice(
                        rs.getDouble("price")
                );

                service.setImage(
                        rs.getString("image")
                );

                serviceList.add(service);
            }

            rs.close();

            ps.close();

            conn.close();

        } catch (Exception e) {

            e.printStackTrace();
        }

        return serviceList;
    }
    public int countServices() {

    int total = 0;

    try {

        Connection conn =
                DBConnection.getConnection();

        String sql =
                "SELECT COUNT(*) AS total FROM services";

        PreparedStatement ps =
                conn.prepareStatement(sql);

        ResultSet rs =
                ps.executeQuery();

        if (rs.next()) {

            total = rs.getInt("total");
        }

        rs.close();

        ps.close();

        conn.close();

    } catch (Exception e) {

        e.printStackTrace();
    }

    return total;
}
public List<String> getAllServiceNames() {

    List<String> serviceNames =
            new ArrayList<>();

    try {

        Connection conn =
                DBConnection.getConnection();

        String sql =
                "SELECT name FROM services";

        PreparedStatement ps =
                conn.prepareStatement(sql);

        ResultSet rs =
                ps.executeQuery();

        while (rs.next()) {

            serviceNames.add(
                    rs.getString("name")
            );
        }

        rs.close();
        ps.close();
        conn.close();

    } catch (Exception e) {

        e.printStackTrace();
    }

    return serviceNames;
}
    public int getServiceIdByName(String name) {

    int id = -1;

    try {

        Connection conn =
                DBConnection.getConnection();

        String sql = """
                SELECT id
                FROM services
                WHERE name = ?
                """;

        PreparedStatement ps =
                conn.prepareStatement(sql);

        ps.setString(1, name);

        ResultSet rs =
                ps.executeQuery();

        if (rs.next()) {

            id = rs.getInt("id");
        }

        rs.close();
        ps.close();
        conn.close();

    } catch (Exception e) {

        e.printStackTrace();
    }

    return id;
}
    
    public ServiceModel getServiceById(int id) {
        ServiceModel service = null;
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT * FROM services WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                service = new ServiceModel();
                service.setId(rs.getInt("id"));
                service.setName(rs.getString("name"));
                service.setDescription(rs.getString("description"));
                service.setPrice(rs.getDouble("price"));
                service.setImage(rs.getString("image"));
            }
            rs.close();
            ps.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return service;
    }

    public boolean insertService(ServiceModel service) {
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "INSERT INTO services(name, description, price, image) VALUES(?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, service.getName());
            ps.setString(2, service.getDescription());
            ps.setDouble(3, service.getPrice());
            ps.setString(4, service.getImage());
            int result = ps.executeUpdate();
            ps.close();
            conn.close();
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateService(ServiceModel service) {
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "UPDATE services SET name=?, description=?, price=?, image=? WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, service.getName());
            ps.setString(2, service.getDescription());
            ps.setDouble(3, service.getPrice());
            ps.setString(4, service.getImage());
            ps.setInt(5, service.getId());
            int result = ps.executeUpdate();
            ps.close();
            conn.close();
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteService(int id) {
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "DELETE FROM services WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            int result = ps.executeUpdate();
            ps.close();
            conn.close();
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public static void main(String[] args) {
        List<ServiceModel> userList = new ServiceController().getAllService();
        System.out.println(userList.size()+"hiển thị danh sách bác sĩ");
    }
}