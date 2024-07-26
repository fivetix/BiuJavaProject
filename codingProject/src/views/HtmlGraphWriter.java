package views;

import java.util.*;
import configs.Graph;
import configs.Node;

public class HtmlGraphWriter {
    public static List<String> getGraphHTML(Graph graph) {
        List<String> html = new ArrayList<>();
        html.add("<html>");
        html.add("<head><title>Graph</title></head>");
        html.add("<style>");
        html.add("#graphCanvas { border: 1px solid black; }");
        html.add("</style>");
        html.add("</head>");
        html.add("<body>");
        html.add("<canvas id='graphCanvas' width='800' height='600'></canvas>");
        html.add("<script>");
        html.add("const canvas = document.getElementById('graphCanvas');");
        html.add("const ctx = canvas.getContext('2d');");

        html.add("function drawGraph(nodes, edges) {");
        html.add("ctx.clearRect(0, 0, canvas.width, canvas.height);");

        html.add("edges.forEach(edge => {");
        html.add("const [from, to] = edge;");
        html.add("ctx.beginPath();");
        html.add("ctx.moveTo(from.x, from.y);");
        html.add("ctx.lineTo(to.x, to.y);");
        html.add("ctx.stroke();");
        html.add("});");

        html.add("nodes.forEach(node => {");
        html.add("ctx.beginPath();");
        html.add("if (node.type === 'topic') {");
        html.add("ctx.rect(node.x - 40, node.y - 20, 80, 40);");
        html.add("} else {");
        html.add("ctx.arc(node.x, node.y, 20, 0, 2 * Math.PI);");
        html.add("}");
        html.add("ctx.stroke();");
        html.add("ctx.fillText(node.name, node.x - 20, node.y - 30);");
        html.add("});");
        html.add("}");

        html.add("const nodes = [");
        for (Node node : graph) {
            String type = node.getName().startsWith("T") ? "topic" : "agent";
            html.add(String.format("{ name: '%s', x: %d, y: %d, type: '%s' },",
                    node.getName(), node.getX(), node.getY(), type));
        }
        html.add("];");

        html.add("const edges = [");
        for (Node node : graph) {
            for (Node edge : node.getEdges()) {
                html.add(String.format("[{ x: %d, y: %d }, { x: %d, y: %d }],",
                        node.getX(), node.getY(), edge.getX(), edge.getY()));
            }
        }
        html.add("];");

        html.add("drawGraph(nodes, edges);");
        html.add("</script>");
        html.add("</body>");
        html.add("</html>");
        return html;
    }
}
