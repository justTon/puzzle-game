package ru.ssau.daria;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;

@WebServlet("/openImage")
public class OpenImageServlet extends HttpServlet {

    @Resource(name = "jdbc/PuzzleDS")
    private DataSource dataSource;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String x_s = request.getParameter("x");
        String y_s = request.getParameter("y");
        String img_s = request.getParameter("img_ig");

        int x = Integer.parseInt(x_s);
        int y = Integer.parseInt(y_s);
        int img_id = Integer.parseInt(img_s);

        GameDAO gameDAO = new GameDAO(dataSource);
        GameCell anotherCell = gameDAO.loadAnotherOpenNotCompletedCell(x, y);
        if (anotherCell == null) {
            gameDAO.switchGameCell(x, y, true);
            response.sendRedirect("game.jsp?wait_before_open=true");
        } else {
            if (anotherCell.getImageId() == img_id) {
                gameDAO.switchGameCell(x, y, true, true);
                gameDAO.switchGameCell(anotherCell.getX(), anotherCell.getY(), true, true);
            } else {
                gameDAO.switchGameCell(anotherCell.getX(), anotherCell.getY(), false);
            }
            response.sendRedirect("game.jsp?wait_before_open=false");
        }
    }
}
