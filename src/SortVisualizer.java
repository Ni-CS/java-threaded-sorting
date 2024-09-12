import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class SortVisualizer extends JFrame {
    private int[] array;
    private SortPanel sortPanel;
    private JComboBox<Integer> threadCountSelector;
    private JComboBox<String> algorithmSelector;
    private JSlider arraySizeSlider;
    private JSlider speedSlider;
    private SwingWorker<Void, int[]> currentSorter;

    public SortVisualizer(int maxSize) {
        array = new int[maxSize];
        generateRandomArray(maxSize);

        sortPanel = new SortPanel(array);

        // Configuração da janela principal
        setTitle("Visualizador de Ordenação");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        // Painel de controle
        JPanel controlPanel = new JPanel();
        JButton startButton = new JButton("Iniciar Ordenação");
        JButton resetButton = new JButton("Reiniciar Array");
        JButton pauseButton = new JButton("Pausar");
        JButton stopButton = new JButton("Parar");

        threadCountSelector = new JComboBox<>(new Integer[]{1, 2, 4, 8, 16}); // Seleção de threads
        arraySizeSlider = new JSlider(10, maxSize, maxSize); // Definir tamanho do array
        speedSlider = new JSlider(1, 1000, 500); // Controle de velocidade (1 ms a 1000 ms)

        // Adicionando seleção de algoritmo
        algorithmSelector = new JComboBox<>(new String[]{"Merge Sort", "Quick Sort"});

        controlPanel.add(new JLabel("Algoritmo:"));
        controlPanel.add(algorithmSelector);
        controlPanel.add(new JLabel("Threads:"));
        controlPanel.add(threadCountSelector);
        controlPanel.add(new JLabel("Tamanho do Array:"));
        controlPanel.add(arraySizeSlider);
        controlPanel.add(new JLabel("Velocidade (ms):"));
        controlPanel.add(speedSlider);
        controlPanel.add(startButton);
        controlPanel.add(pauseButton);
        controlPanel.add(stopButton);
        controlPanel.add(resetButton);

        // Adiciona os componentes à janela
        add(sortPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        // Ação para iniciar a ordenação
        startButton.addActionListener(e -> {
            int threadCount = (int) threadCountSelector.getSelectedItem();
            int speed = speedSlider.getValue();
            String algorithm = (String) algorithmSelector.getSelectedItem();

            if (currentSorter != null && !currentSorter.isDone()) {
                currentSorter.cancel(true); // Cancela o sorter anterior caso esteja rodando
            }

            if ("Merge Sort".equals(algorithm)) {
                currentSorter = new ParallelMergeSort(array, 0, array.length - 1, sortPanel, threadCount, speed);
                currentSorter.execute();
            } else if ("Quick Sort".equals(algorithm)) {
                currentSorter = new ParallelQuickSort(array, 0, array.length - 1, sortPanel, threadCount, speed);
                currentSorter.execute();
            }
        });

        // Ação para pausar a ordenação
        pauseButton.addActionListener(e -> {
            if (currentSorter instanceof SortWorker) {
                ((SortWorker) currentSorter).pause();
            }
        });

        // Ação para parar a ordenação
        stopButton.addActionListener(e -> {
            if (currentSorter != null && !currentSorter.isDone()) {
                currentSorter.cancel(true);
            }
        });

        // Ação para reiniciar o array
        resetButton.addActionListener(e -> {
            if (currentSorter != null && !currentSorter.isDone()) {
                currentSorter.cancel(true); // Para o sorter antes de resetar o array
            }
            int newSize = arraySizeSlider.getValue();
            generateRandomArray(newSize);
            sortPanel.updateArray(array);
        });
    }

    // Gera um array aleatório
    private void generateRandomArray(int size) {
        array = new int[size];
        Random rand = new Random();
        for (int i = 0; i < array.length; i++) {
            array[i] = rand.nextInt(500) + 1; // Valores aleatórios entre 1 e 500
        }
    }
}