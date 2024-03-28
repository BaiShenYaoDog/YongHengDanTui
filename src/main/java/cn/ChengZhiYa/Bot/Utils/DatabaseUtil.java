package cn.ChengZhiYa.Bot.Utils;

import cn.ChengZhiYa.Bot.Class.Plugin;
import cn.ChengZhiYa.Bot.Class.User;
import org.apache.commons.lang3.RandomStringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static cn.ChengZhiYa.Bot.Main.dataSource;

public final class DatabaseUtil {
    public static List<Plugin> getPluginList() {
        List<Plugin> PluginList = new ArrayList<>();
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM t_plugin");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PluginList.add(new Plugin(rs.getInt("id"), rs.getString("plugin_name"), rs.getString("plugin_version")));
            }
            rs.close();
            ps.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return PluginList;
    }

    public static boolean ifPluginExists(String PluginName) {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM t_plugin WHERE plugin_name = ? LIMIT 1");
            ps.setString(1, PluginName);
            ResultSet rs = ps.executeQuery();
            boolean 结果 = rs.next();
            rs.close();
            ps.close();
            connection.close();
            return 结果;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Plugin getPlugin(String PluginName) {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM t_plugin WHERE plugin_name = ? LIMIT 1");
            ps.setString(1, PluginName);
            ResultSet rs = ps.executeQuery();
            Plugin Plugin = null;
            if (rs.next()) {
                Plugin = new Plugin(rs.getInt("id"), rs.getString("plugin_name"), rs.getString("plugin_version"));
            }
            rs.close();
            ps.close();
            connection.close();
            return Plugin;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Plugin getPlugin(int PluginID) {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM t_plugin WHERE id = ? LIMIT 1");
            ps.setInt(1, PluginID);
            ResultSet rs = ps.executeQuery();
            Plugin Plugin = null;
            if (rs.next()) {
                Plugin = new Plugin(rs.getInt("id"), rs.getString("plugin_name"), rs.getString("plugin_version"));
            }
            rs.close();
            ps.close();
            connection.close();
            return Plugin;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void addPlugin(String PluginName, String Version) {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO t_plugin (plugin_name, plugin_version) VALUES (?,?)");
            ps.setString(1, PluginName);
            ps.setString(2, Version);
            ps.executeUpdate();
            ps.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void setPlugin(String PluginName, String Version) {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement("UPDATE t_plugin SET plugin_version = ? WHERE plugin_name = ?");
            ps.setString(1, Version);
            ps.setString(2, PluginName);
            ps.executeUpdate();
            ps.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removePlugin(String PluginName) {
        try {
            Plugin Plugin = getPlugin(PluginName);
            {
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement("DELETE FROM t_plugin WHERE plugin_name = ?");
                ps.setString(1, PluginName);
                ps.executeUpdate();
                ps.close();
                connection.close();
            }
            {
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement("DELETE FROM t_plugin_user_con WHERE plugin_id = ?");
                ps.setInt(1, Objects.requireNonNull(Plugin).getId());
                ps.executeUpdate();
                ps.close();
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean ifUserExists(String UserName) {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM t_user WHERE user_name = ? LIMIT 1");
            ps.setString(1, UserName);
            ResultSet rs = ps.executeQuery();
            boolean 结果 = rs.next();
            rs.close();
            ps.close();
            connection.close();
            return 结果;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static User getUser(String UserName) {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM t_user WHERE user_name = ? LIMIT 1");
            ps.setString(1, UserName);
            ResultSet rs = ps.executeQuery();
            User User = null;
            if (rs.next()) {
                User = new User(rs.getString("id"), rs.getString("user_name"), rs.getString("user_password"), rs.getString("last_login_ip"));
            }
            rs.close();
            ps.close();
            connection.close();
            return User;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void register(String UserName) {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO t_user (id, user_name, user_password, last_login_ip) VALUES (?,?,?,?)");
            ps.setString(1, UserName);
            ps.setString(2, UserName);
            ps.setString(3, RandomStringUtils.randomAscii(12));
            ps.setString(4, "127.0.0.1");
            ps.executeUpdate();
            ps.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void unRegister(String UserName) {
        try {
            User User = getUser(UserName);
            {
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement("DELETE FROM t_user WHERE user_name = ?");
                ps.setString(1, UserName);
                ps.executeUpdate();
                ps.close();
                connection.close();
            }
            {
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement("DELETE FROM t_plugin_user_con WHERE user_id = ?");
                ps.setString(1, Objects.requireNonNull(User).getId());
                ps.executeUpdate();
                ps.close();
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Plugin> getHavePluginList(String UserName) {
        User User = getUser(UserName);

        List<Plugin> PluginList = new ArrayList<>();
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM t_plugin_user_con WHERE user_id = ?");
            ps.setString(1, Objects.requireNonNull(User).getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PluginList.add(new Plugin(rs.getInt("id"), rs.getString("plugin_name"), rs.getString("plugin_version")));
            }
            rs.close();
            ps.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return PluginList;
    }

    public static boolean havePlugin(String UserName, String PluginName) {
        for (Plugin Plugin : getHavePluginList(UserName)) {
            if (Plugin.getPluginName().equals(PluginName)) {
                return true;
            }
        }
        return false;
    }

    public static void allowUsePlugin(String UserName, String PluginName) {
        User User = getUser(UserName);
        Plugin Plugin = getPlugin(PluginName);

        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO t_plugin_user_con (user_id, plugin_id) VALUES (?,?)");
            ps.setString(1, Objects.requireNonNull(User).getId());
            ps.setInt(2, Objects.requireNonNull(Plugin).getId());
            ps.executeUpdate();
            ps.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void unAllowUsePlugin(String UserName, String PluginName) {
        User User = getUser(UserName);
        Plugin Plugin = getPlugin(PluginName);

        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement("DELETE FROM t_plugin_user_con WHERE user_id = ? AND plugin_id = ?");
            ps.setString(1, Objects.requireNonNull(User).getId());
            ps.setInt(2, Objects.requireNonNull(Plugin).getId());
            ps.executeUpdate();
            ps.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
