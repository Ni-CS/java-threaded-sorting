import java.util.Arrays;

class ParallelQuickSort extends SortWorker {
    private int left;
    private int right;
    private int threadCount;

    public ParallelQuickSort(int[] array, int left, int right, SortPanel sortPanel, int threadCount, int delay) {
        super(array, sortPanel, delay);
        this.left = left;
        this.right = right;
        this.threadCount = threadCount;
    }

    @Override
    protected Void doInBackground() throws InterruptedException {
        parallelQuickSort(array, left, right, threadCount);
        return null;
    }

    // Método recursivo do Quick Sort paralelo com threads
    private void parallelQuickSort(int[] array, int left, int right, int threadCount) throws InterruptedException {
        if (left < right) {
            checkPaused();
            if (isCancelled()) return;

            int pivotIndex = partition(array, left, right);

            if (threadCount > 1) {
                Thread leftThread = new Thread(() -> {
                    try {
                        parallelQuickSort(array, left, pivotIndex - 1, threadCount / 2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
                Thread rightThread = new Thread(() -> {
                    try {
                        parallelQuickSort(array, pivotIndex + 1, right, threadCount / 2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });

                leftThread.start();
                rightThread.start();

                leftThread.join();
                rightThread.join();
            } else {
                quickSort(array, left, right);
            }

            publish(Arrays.copyOf(array, array.length));
            Thread.sleep(delay);
        }
    }

    // Quick Sort sequencial
    private void quickSort(int[] array, int left, int right) throws InterruptedException {
        if (left < right) {
            checkPaused();
            if (isCancelled()) return;

            int pivotIndex = partition(array, left, right);
            quickSort(array, left, pivotIndex - 1);
            quickSort(array, pivotIndex + 1, right);
        }
    }

    // Método de partição do Quick Sort
    private int partition(int[] array, int left, int right) throws InterruptedException {
        int pivot = array[right];
        int i = left - 1;

        for (int j = left; j < right; j++) {
            checkPaused();
            if (isCancelled()) return i + 1;

            if (array[j] <= pivot) {
                i++;
                swap(array, i, j);

                publish(Arrays.copyOf(array, array.length));
                Thread.sleep(delay);
            }
        }

        swap(array, i + 1, right);
        publish(Arrays.copyOf(array, array.length));
        Thread.sleep(delay);

        return i + 1;
    }

    // Método para trocar elementos no array
    private void swap(int[] array, int i, int j) throws InterruptedException {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;

        publish(Arrays.copyOf(array, array.length));
        Thread.sleep(delay);
    }
}