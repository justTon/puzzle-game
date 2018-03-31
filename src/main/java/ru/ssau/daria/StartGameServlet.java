package ru.ssau.daria;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.*;

import static ru.ssau.daria.Const.MAXIMUM_NUMBER_OF_DISTINCT_IMAGES;
import static ru.ssau.daria.Const.NUMBER_OF_SIMILAR_CARDS;

@WebServlet("/startGameServlet")
public class StartGameServlet extends HttpServlet {


    @Resource(name = "jdbc/PuzzleDS")
    private DataSource dataSource;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        super.doPost(request, response);

        String v_s = request.getParameter("vertical");
        String h_s = request.getParameter("horizontal");
        String theme = request.getParameter("theme");

        int v = Integer.parseInt(v_s);
        int h = Integer.parseInt(h_s);
        int half = (v * h) / NUMBER_OF_SIMILAR_CARDS;

        List<Integer> imgIds = selectImagesForGame(half);
        int[][] field = generatePlayField(v, h, imgIds);

        GameInfo gameInfo = new GameInfo(h, v, theme);
        List<GameCell> gameCells = prepareGameCells(v, h, field);

        GameDAO gameDAO = new GameDAO(dataSource);
        gameDAO.clearPreviousGames();
        gameDAO.storeGameInfo(gameInfo);
        gameDAO.storeGameField(gameCells);

        response.sendRedirect("game.jsp");
    }

    private List<GameCell> prepareGameCells(int v, int h, int[][] field) {
        List<GameCell> gameCells = new ArrayList<>();
        for (int i = 0; i < v; i++) {
            for (int j = 0; j < h; j++) {
                GameCell cell = new GameCell(i, j, field[i][j]);
                gameCells.add(cell);
            }
        }
        return gameCells;
    }

    private int[][] generatePlayField(int v, int h, List<Integer> imgIds) {

        int imagesCount = imgIds.size();

        Map<Integer, Integer> imgEntries = new HashMap<>(); //пары "название карточки" - "число карточек на поле"
        for (Integer imgId : imgIds) {
            imgEntries.put(imgId, 0);
        }

        int[][] field = new int[v][h]; //игровое поле. в каждой ячейке - ID картинки
        Random r = new Random();

        for (int i = 0; i < v; i++) {
            for (int j = 0; j < h; j++) {

                int imageId = -1;
                do {
                    int nextId = r.nextInt(imagesCount); //случайный номер картинки из ранее отобранных
                    imageId = imgIds.get(nextId); //получаем ID этой картинки

                    if (imgEntries.get(imageId) == NUMBER_OF_SIMILAR_CARDS) //если на поле уже есть две таких картинки, больше добавлять нельзя. пробуем еще раз
                        imageId = -1;
                } while (imageId < 0);

                field[i][j] = imageId;
                imgEntries.put(imageId, imgEntries.get(imageId) + 1); //увеличиваем счетчик картинок с этим ID на поле
            }
        }
        return field;
    }

    // По условию задания, максимум на поле может быть 16 (8*4/2) уникальных картинок. Из 16 картинок мы выбираем те,
    // которые будут на поле
    private List<Integer> selectImagesForGame(int half) {
        Set<Integer> imgIds = new HashSet<>();
        Random r = new Random();
        while (imgIds.size() < half) {
            int next = r.nextInt(MAXIMUM_NUMBER_OF_DISTINCT_IMAGES);
            imgIds.add(next);
        }
        return new ArrayList<>(imgIds);
    }
}
