<html>
<head>
    <title>Puzzle Game</title>

    <link rel="stylesheet" href="css/main.css">
    <script type="text/javascript">
        function toggleErrorMsg() {
            var x = document.getElementById("errorDiv");

            var h_str = document.getElementById("horizontal").value;
            var v_str = document.getElementById("vertical").value;

            var h = parseInt(h_str, 10);
            var v = parseInt(v_str, 10);

            if ((h * v) % 2 !== 0) {
                x.style.display = "block";
                return false;
            } else {
                x.style.display = "none";
                return true;
            }
        }
    </script>
</head>
<body>
<h2>Puzzle game</h2>
<div class="wrapper">
    <form action="startGameServlet" method="post" class="wrapper">
        <table style="margin: 30px auto; ">
            <tr>
                <td>
                    <label>Horizontal</label>
                </td>
                <td align="right">
                    <select name="horizontal" id="horizontal">
                        <%for (int i = 5; i < 9; i++) {%>
                        <option value="<%=i%>"><%=i%>
                        </option>
                        <% } %>
                    </select>
                </td>
            </tr>
            <tr>
                <td>
                    <label>Vertical</label>
                </td>
                <td align="right">
                    <select name="vertical" id="vertical">
                        <%for (int i = 3; i < 5; i++) {%>
                        <option value="<%=i%>"><%=i%>
                        </option>
                        <% } %>
                    </select>
                </td>
            </tr>
            <tr>
                <td>
                    <label>Theme</label>
                </td>
                <td align="right">
                    <select name="theme" id="theme">
                        <option value="colors">Colors</option>
                        <option value="coins">Coins</option>
                        <option value="shapes">Shapes</option>
                    </select>
                </td>
            </tr>

        </table>
        <button onclick="return toggleErrorMsg()">Play!</button>
    </form>

    <div id="errorDiv" style="color: red; text-align: center; display: none">Invalid cell number values! At least one
        must be even
    </div>

</div>
</body>
</html>
