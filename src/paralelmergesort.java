import java.util.Arrays;

class ParallelMergeSort extends SortWorker {
    private int left;
    private int right;
    private int threadCount;

    public ParallelMergeSort(int[] array, int left, int right, SortPanel sortPanel, int threadCount, int delay) {
        super(array, sortPanel, delay);
        this.left = left;
        this.right = right;
        this.threadCount = threadCount;
    }

    @Override
    protected Void doInBackground() throws InterruptedException {
        parallelMergeSort(array, left, right, threadCount);
        return null;
    }

    // Método recursivo do Merge Sort paralelo com threads
    private void parallelMergeSort(int[] array, int left, int right, int threadCount) throws InterruptedException {
        if (left < right) {
            checkPaused(); // Verifica se está pausado
            if (isCancelled()) return; // Cancela a execução

            int mid = (left + right) / 2;

            if (threadCount > 1) {
                Thread leftThread = new Thread(() -> {
                    try {
                        parallelMergeSort(array, left, mid, threadCount / 2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
                Thread rightThread = new Thread(() -> {
                    try {
                        parallelMergeSort(array, mid + 1, right, threadCount / 2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });

                leftThread.start();
                rightThread.start();

                leftThread.join();
                rightThread.join();
            } else {
                mergeSort(array, left, right);
            }

            merge(array, left, mid, right);

            publish(Arrays.copyOf(array, array.length));
            Thread.sleep(delay); // Controle de velocidade
        }
    }

    // Merge Sort sequencial
    private void mergeSort(int[] array, int left, int right) throws InterruptedException {
        if (left < right) {
            checkPaused();
            if (isCancelled()) return;

            int mid = (left + right) / 2;
            mergeSort(array, left, mid);
            mergeSort(array, mid + 1, right);

            merge(array, left, mid, right);
        }
    }

    // Método de merge
    private void merge(int[] array, int left, int mid, int right) throws InterruptedException {
        int[] temp = new int[right - left + 1];
        int i = left, j = mid + 1, k = 0;

        while (i <= mid && j <= right) {
            checkPaused();
            if (isCancelled()) return;

            if (array[i] <= array[j]) {
                temp[k++] = array[i++];
            } else {
                temp[k++] = array[j++];
            }

            publish(Arrays.copyOf(array, array.length));
            Thread.sleep(delay);
        }

        while (i <= mid) {
            checkPaused();
            if (isCancelled()) return;

            temp[k++] = array[i++];
            publish(Arrays.copyOf(array, array.length));
            Thread.sleep(delay);
        }

        while (j <= right) {
            checkPaused();
            if (isCancelled()) return;

            temp[k++] = array[j++];
            publish(Arrays.copyOf(array, array.length));
            Thread.sleep(delay);
        }

        System.arraycopy(temp, 0, array, left, temp.length);
    }
}