import javax.swing.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

abstract class SortWorker extends SwingWorker<Void, int[]> {
    protected AtomicBoolean isPaused = new AtomicBoolean(false);
    protected AtomicBoolean isCancelled = new AtomicBoolean(false);
    protected SortPanel sortPanel;
    protected int[] array;
    protected int delay;

    public SortWorker(int[] array, SortPanel sortPanel, int delay) {
        this.array = array;
        this.sortPanel = sortPanel;
        this.delay = delay;
    }

    // Método para pausar o processo
    public void pause() {
        isPaused.set(!isPaused.get()); // Alterna entre pausar e despausar
    }

    protected void checkPaused() throws InterruptedException {
        while (isPaused.get()) {
            Thread.sleep(100); // Espera enquanto estiver pausado
        }
    }

    // Override the process method to update the UI incrementally
    @Override
    protected void process(List<int[]> chunks) {
        for (int[] chunk : chunks) {
            sortPanel.updateArray(chunk);
        }
    }
}