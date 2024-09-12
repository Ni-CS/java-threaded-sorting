import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Random;

class SortPanel extends JPanel {
    private int[] array;
    private Color[] threadColors;

    public SortPanel(int[] array) {
        this.array = array;
        threadColors = generateRandomColors(array.length);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int width = getWidth();
        int height = getHeight();
        int barWidth = width / array.length;

        for (int i = 0; i < array.length; i++) {
            int barHeight = (int) ((array[i] / 500.0) * height);
            g.setColor(threadColors[i]);
            g.fillRect(i * barWidth, height - barHeight, barWidth, barHeight);
        }
    }

    // Atualiza o array e repinta o painel
    public void updateArray(int[] newArray) {
        this.array = Arrays.copyOf(newArray, newArray.length);
        threadColors = generateRandomColors(array.length); // Atualiza as cores
        repaint();
    }

    // Gera cores aleatórias para as threads
    private Color[] generateRandomColors(int length) {
        Color[] colors = new Color[length];
        Random rand = new Random();
        for (int i = 0; i < length; i++) {
            colors[i] = new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
        }
        return colors;
    }
}