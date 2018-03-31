package ru.ssau.daria;

import javax.servlet.ServletException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GameDAO {

    private static final String SELECT_GAME_INFO = "select * from game_info;";
    private static final String SELECT_GAME_FIELD = "select * from game_field;";
    private static final String SELECT_GAME_CELL = "select * from game_field where x = ? and y = ?;";
    private static final String SELECT_ANOTHER_OPEN_CELL = "select * from game_field where (x != ? or y != ?) " +
            "and open = ? and completed = ?;";
    private static final String CLEAR_GAME_INFO = "delete from game_info;";
    private static final String CLEAR_GAME_FIELD = "delete from game_field;";
    private static final String INSERT_GAME_INFO_ROW = "insert into game_info (horizontal, vertical, theme) values (?,?,?);";
    private static final String INSERT_GAME_FIELD_ROW = "insert into game_field (x, y, img_id) values (?,?,?);";
    private static final String UPDATE_GAME_FIELD_ROW = "update game_field set open = ?, completed = ? where x = ? and y = ?;";

    private DataSource dataSource = null;

    public GameDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void clearPreviousGames() throws ServletException {
        withDB(new JDBCRunner<Void>() {
            @Override
            public Void run(Connection connection) throws Exception {

                Statement statement = connection.createStatement();
                statement.executeUpdate(CLEAR_GAME_FIELD);
                statement.executeUpdate(CLEAR_GAME_INFO);

                return null;
            }
        });
    }


    public List<GameCell> loadGameField() throws ServletException {
        return withDB(new JDBCRunner<List<GameCell>>() {
            @Override
            public List<GameCell> run(Connection connection) throws Exception {

                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(SELECT_GAME_FIELD);
                List<GameCell> cells = new ArrayList<>();
                while (rs.next()) {
                    GameCell cell = new GameCell();
                    cell.setX(rs.getInt("x"));
                    cell.setY(rs.getInt("y"));
                    cell.setImageId(rs.getInt("img_id"));
                    cell.setOpen(rs.getBoolean("open"));
                    cell.setCompleted(rs.getBoolean("completed"));

                    cells.add(cell);
                }

                return cells;
            }
        });
    }

    public void storeGameField(List<GameCell> gameCells) throws ServletException {
        withDB(new JDBCRunner<Void>() {
            @Override
            public Void run(Connection connection) throws Exception {

                PreparedStatement gameFieldPS = connection.prepareStatement(INSERT_GAME_FIELD_ROW);
                for (GameCell gameCell : gameCells) {
                    gameFieldPS.setInt(1, gameCell.getX());
                    gameFieldPS.setInt(2, gameCell.getY());
                    gameFieldPS.setInt(3, gameCell.getImageId());

                    gameFieldPS.executeUpdate();
                }

                return null;
            }
        });
    }

    public void switchGameCell(int x, int y, boolean isOpen) throws ServletException {
        switchGameCell(x, y, isOpen, false);
    }

    public void switchGameCell(int x, int y, boolean isOpen, boolean completed) throws ServletException {
        withDB(new JDBCRunner<Void>() {
            @Override
            public Void run(Connection connection) throws Exception {

                PreparedStatement gameFieldPS = connection.prepareStatement(UPDATE_GAME_FIELD_ROW);
                gameFieldPS.setBoolean(1, isOpen);
                gameFieldPS.setBoolean(2, completed);
                gameFieldPS.setInt(3, x);
                gameFieldPS.setInt(4, y);

                gameFieldPS.executeUpdate();


                return null;
            }
        });
    }

    public GameCell loadGameCell(int x, int y) throws ServletException {
        return withDB(new JDBCRunner<GameCell>() {
            @Override
            public GameCell run(Connection connection) throws Exception {

                PreparedStatement statement = connection.prepareStatement(SELECT_GAME_CELL);
                statement.setInt(1, x);
                statement.setInt(2, y);
                ResultSet rs = statement.executeQuery();

                GameCell cell = null;
                if (rs.next()) {
                    cell = rsToGameCell(rs);
                }

                return cell;
            }
        });
    }


    public GameCell loadAnotherOpenNotCompletedCell(int x, int y) throws ServletException {
        return withDB(new JDBCRunner<GameCell>() {
            @Override
            public GameCell run(Connection connection) throws Exception {

                PreparedStatement statement = connection.prepareStatement(SELECT_ANOTHER_OPEN_CELL);
                statement.setInt(1, x);
                statement.setInt(2, y);
                statement.setBoolean(3, true);
                statement.setBoolean(4, false);
                ResultSet rs = statement.executeQuery();

                GameCell cell = null;
                if (rs.next()) {
                    cell = rsToGameCell(rs);
                }

                return cell;
            }
        });
    }

    private GameCell rsToGameCell(ResultSet rs) throws SQLException {
        GameCell cell;
        cell = new GameCell();
        cell.setX(rs.getInt("x"));
        cell.setY(rs.getInt("y"));
        cell.setImageId(rs.getInt("img_id"));
        cell.setOpen(rs.getBoolean("open"));
        cell.setCompleted(rs.getBoolean("completed"));
        return cell;
    }


    public GameInfo loadGameInfo() throws ServletException {
        return withDB(new JDBCRunner<GameInfo>() {
            @Override
            public GameInfo run(Connection connection) throws Exception {

                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(SELECT_GAME_INFO);
                GameInfo gameInfo = null;
                if (rs.next()) {

                    gameInfo = new GameInfo();
                    gameInfo.setHorizontal(rs.getInt("horizontal"));
                    gameInfo.setVertical(rs.getInt("vertical"));
                    gameInfo.setTheme(rs.getString("theme"));
                }
                return gameInfo;
            }
        });
    }

    public void storeGameInfo(GameInfo gameInfo) throws ServletException {
        withDB(new JDBCRunner<Void>() {
            @Override
            public Void run(Connection connection) throws Exception {

                PreparedStatement gameInfoPS = connection.prepareStatement(INSERT_GAME_INFO_ROW);
                gameInfoPS.setInt(1, gameInfo.getHorizontal());
                gameInfoPS.setInt(2, gameInfo.getVertical());
                gameInfoPS.setString(3, gameInfo.getTheme());
                gameInfoPS.executeUpdate();

                return null;
            }
        });
    }


    static interface JDBCRunner<T> {
        T run(Connection connection) throws Exception;
    }

    private <T> T withDB(JDBCRunner<T> runner) throws ServletException {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            T result = runner.run(connection);
            connection.commit();
            return result;
        } catch (Exception ex) {
            try {
                if (connection != null)
                    connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            throw new ServletException(ex);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception ex) {
                    //should log this as a warn or info
                    ex.printStackTrace();
                }
            }
        }
    }
}
