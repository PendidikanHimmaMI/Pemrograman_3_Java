package himma.pendidikan.component;

import himma.pendidikan.model.TopMasterData;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;

public class Chart {
    public static void pieChart(PieChart pieChart, List<TopMasterData> dataList, String title, VBox legendBox) {
        pieChart.getData().clear();
        legendBox.getChildren().clear();

        Color[] pieColors = {
            Color.rgb(93, 179, 224),
            Color.rgb(22, 68, 118),
            Color.rgb(22, 104, 150),
            Color.rgb(22, 123, 175),
            Color.rgb(62, 159, 209)
        };

        double total = dataList.stream()
                .mapToDouble(data -> data.getJumlah() != null ? data.getJumlah() : 0)
                .sum();

        int i = 0;
        for (TopMasterData item : dataList) {
            double value = item.getJumlah() != null ? item.getJumlah() : 0;
            double percentage = (value / total) * 100;
            String label = String.format("%.0f", percentage) + "%";

            PieChart.Data chartData = new PieChart.Data(label, value);
            pieChart.getData().add(chartData);

            String hexColor = String.format("#%02x%02x%02x",
                    (int)(pieColors[i].getRed() * 255),
                    (int)(pieColors[i].getGreen() * 255),
                    (int)(pieColors[i].getBlue() * 255));

            int finalI = i;
            chartData.nodeProperty().addListener((obs, oldNode, newNode) -> {
                if (newNode != null) {
                    newNode.setStyle("-fx-pie-color: " + hexColor + ";");
                }
            });

            // Buat legend
            HBox legendItem = new HBox(5);
            Pane colorPane = new Pane();
            colorPane.setPrefSize(50, 10);
            colorPane.setStyle("-fx-background-color: " + hexColor + ";");

            Label legendLabel = new Label(item.getNama() + " - " + label);
            legendLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 10));

            legendItem.getChildren().addAll(colorPane, legendLabel);
            legendBox.getChildren().add(legendItem);

            i++;
        }
        pieChart.setTitle(title);
    }
}
