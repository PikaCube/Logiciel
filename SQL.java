package fr.pikacube.logiciel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQL {

    Connection connection = null;
    ResultSet resultSet = null;
    PreparedStatement stmt = null;
    Statement statement = null;
    static String query = null;
    String url;
    String username;
    String password;
    String dbName;
    Logs messages;
    public SQL()
    {
        messages = new Logs();
        url = "sql11.freesqldatabase.com:3306";
        username = "sql11452791";
        password = "6phcB1jqjY";
        dbName = "sql11452791";
    }

    private boolean connect(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            messages.errorMessage(e.getMessage());
        }
        String connectionUrl =
                "jdbc:mysql://"+url+"/"+dbName;
        try
        {
            connection = DriverManager.getConnection(connectionUrl, username, password);
            return true;
        }
        catch(SQLException e)
        {
            messages.errorMessage(e.getMessage());
            return false;
        }
    }

    public boolean deleteUser(String pseudo)
    {
        connect();
        String sql = "DELETE FROM utilisateurs WHERE pseudo = ?";
        try {
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, pseudo);
            stmt.executeUpdate();
            Fenetre.updateList();
            return true;
        } catch (SQLException e) {
            messages.errorMessage("DELETE : "+e.getMessage());
            return false;
        }
        finally
        {
            closeAllConnections();
        }
    }

    public boolean modifyStatus(String pseudo, String grade) //Sert à définir le grade de l'utilisateur :
    {
        connect();
        try {
            stmt = connection.prepareStatement("UPDATE utilisateurs SET type = '"+grade+"' WHERE pseudo ='"+pseudo+"'");
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            messages.errorMessage("UPDATE : " +e.getMessage());
            return false;
        }
        finally {
            closeAllConnections();
        }
    }

    public String selectQuery(String query) //Sert à sélectionner les utilisateurs dans la base de données :
    {
        connect();
        try {

            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            while(resultSet.next()) {
                return resultSet.getString(1);
            }
        } catch (SQLException e) {
            messages.errorMessage("SELECT : " +e.getMessage());
            return null;
        }
        finally
        {
                closeAllConnections();
        }
        return null;
    }

    private void closeAllConnections()
    {
        if(stmt !=null) {
            try {
                stmt.close();
                if(resultSet != null)
                {
                    resultSet.close();
                }
                if(statement != null)
                    statement.close();
                if(connection != null)
                    connection.close();
            } catch (SQLException e) {
                messages.errorMessage("CLOSE : " + e.getMessage());
            }
        }
    }

    public List<String> getUsers()
    {
        List<String> users = new ArrayList();
        connect();
        try {
            statement = connection.createStatement();
            String sql = "SELECT pseudo FROM utilisateurs";
            resultSet = statement.executeQuery(sql);
            while(resultSet.next())
            {
                users.add(resultSet.getString("pseudo"));
            }
        } catch (SQLException e) {
            messages.errorMessage("getUsers : " + e.getMessage());
        }
        finally {
            closeAllConnections();
        }
        return users;
    }

    public boolean addUser(String pseudo, String password){ //Sert à insérer des utilisateurs dans la base de données :
        connect();
        try {
            stmt = connection.prepareStatement("INSERT INTO `utilisateurs` (`pseudo`, `password`, `type`) VALUES ('"+pseudo+"', '"+password+"', 'Utilisateur')");
            stmt.execute();
            Fenetre.updateList();
            return true;
        } catch (SQLException e) {
            messages.errorMessage("INSERT : " +e.getMessage());
            return false;
        }
        finally{
            closeAllConnections();
        }
    }
}
