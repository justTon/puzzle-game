<%@ page import="static ru.ssau.daria.Const.*" %>
<%@ page import="ru.ssau.daria.GameCell" %>
<%@ page import="ru.ssau.daria.GameDAO" %>
<%@ page import="ru.ssau.daria.GameInfo" %>
<%@ page import="javax.naming.Context" %>
<%@ page import="javax.naming.InitialContext" %>
<%@ page import="javax.sql.DataSource" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<html>

<%
    InitialContext initContext = new InitialContext();
    Context envContext = (Context) initContext.lookup("java:/comp/env");
    DataSource ds = (DataSource) envContext.lookup("jdbc/PuzzleDS");

    Boolean shouldWait = Boolean.parseBoolean(request.getParameter("wait_before_open"));

    GameDAO gameDAO = new GameDAO(ds);
    GameInfo gameInfo = gameDAO.loadGameInfo();
    List<GameCell> cells = gameDAO.loadGameField();

    GameCell[][] gameField = new GameCell[gameInfo.getVertical()][gameInfo.getHorizontal()];
    for (GameCell cell : cells) {
        gameField[cell.getX()][cell.getY()] = cell;
    }

%>

<head>
    <title>Puzzle Game</title>
    <link rel="stylesheet" href="css/main.css">

    <script type="text/javascript">
        var stopWork = false;

        function switchCursor(elementId, elementCursor, bodyCursor) {
            document.getElementById(elementId).style.cursor = elementCursor;
            document.body.style.cursor = bodyCursor;
        }

        function showImg(x, y, img_id, theme, shouldWait) {
            if (stopWork) {
                return false;
            }

            stopWork = true;

            var newHref = "openImage?x=" + x + "&y=" + y + "&img_ig=" + img_id;
            var elementId = x + "_" + y;

            switchCursor(elementId, "wait", "wait");
            document.getElementById(elementId).src = "img/" + theme + "/" + img_id + ".png";

            var proceedFunc = function () {
                stopWork = false;
                window.location.href = newHref;
                switchCursor(elementId, "pointer", "default");
            };

            if (shouldWait)
                setTimeout(proceedFunc, 1500);
            else
                proceedFunc();
        }
    </script>

</head>
<body>

<div class="wrapper">
    <div class="divTable">
        <div class="divTableBody">
            <%for (int i = 0; i < gameInfo.getVertical(); i++) {%>
            <div class="divTableRow">
                <%for (int j = 0; j < gameInfo.getHorizontal(); j++) {%>
                <div class="divTableCell">
                    <%
                        GameCell gameCell = gameField[i][j];
                        String imgName = gameCell.isOpen() ? Integer.toString(gameCell.getImageId()) : "back_cover";
                        int x = gameCell.getX();
                        int y = gameCell.getY();

                        String onClickFunc = "return ";
                        if (gameCell.isOpen()) {
                            onClickFunc += "false;"; //картинка уже открыта - нажатие не должно работать
                        } else {
                            onClickFunc += "showImg(" + x + ", " + y + ", " + gameCell.getImageId() + ", '" + gameInfo.getTheme() + "', " + shouldWait + ");";
                        }
                    %>
                    <a href="#" onclick="<%=onClickFunc%>">
                        <img id="<%=x+"_"+y%>" src=<%="\"img/" + gameInfo.getTheme() + "/" + imgName + ".png\""%>/>
                    </a>
                </div>
                <%}%>
            </div>
            <%}%>


        </div>
    </div>
    <div>
        <a href="index.jsp">Go back</a>
    </div>
</div>


</body>
</html>
