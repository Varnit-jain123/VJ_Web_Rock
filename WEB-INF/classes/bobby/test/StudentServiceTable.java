package bobby.test;

import java.sql.*;
import java.util.*;
import com.varnit.jain.webRock.annotations.*;

@Path("/studentTable")
public class StudentServiceTable {
    
    private Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/Student_db",
            "root",
            "poonamra1"
        );
    }

    @Path("/add")
    @POST
    public String add(Student s) {
        try (Connection con = getConnection()) {
            PreparedStatement ps = con.prepareStatement("INSERT INTO Student VALUES(?,?,?)");
            ps.setInt(1, s.getRollNumber());
            ps.setString(2, s.getName());
            ps.setString(3, s.getGender());
            ps.executeUpdate();
            return "Inserted Successfully!";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @Path("/update")
    @POST
    public String update(Student s) {
        try (Connection con = getConnection()) {
            PreparedStatement ps = con.prepareStatement("UPDATE Student SET name=?, gender=? WHERE rollNumber=?");
            ps.setString(1, s.getName());
            ps.setString(2, s.getGender());
            ps.setInt(3, s.getRollNumber());
            ps.executeUpdate();
            return "Updated Successfully!";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @Path("/delete")
    @GET
    public String delete(@RequestParameter("rollNumber") int rollNumber) {
        try (Connection con = getConnection()) {
            PreparedStatement ps = con.prepareStatement("DELETE FROM Student WHERE rollNumber=?");
            ps.setInt(1, rollNumber);
            ps.executeUpdate();
            return "Deleted Successfully!";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @Path("/getBy")
    @GET
    public Student getBy(@RequestParameter("rollNumber") int rollNumber) {
        try (Connection con = getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM Student WHERE rollNumber=?");
            ps.setInt(1, rollNumber);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Student s = new Student();
                s.setRollNumber(rs.getInt("rollNumber"));
                s.setName(rs.getString("name"));
                s.setGender(rs.getString("gender"));
                return s;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Path("/getAll")
    @GET
    public List<Student> getAll() {
        List<Student> list = new ArrayList<>();
        try (Connection con = getConnection()) {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Student");
            while (rs.next()) {
                Student s = new Student();
                s.setRollNumber(rs.getInt("rollNumber"));
                s.setName(rs.getString("name"));
                s.setGender(rs.getString("gender"));
                list.add(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
