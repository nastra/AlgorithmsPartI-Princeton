public class Subset {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Provide k as a first param.");
            return;
        }
        int k = Integer.valueOf(args[0]);
        RandomizedQueue<String> q = new RandomizedQueue<String>();
        String[] items = StdIn.readLine().split(" ");

        for (String item : items) {
            q.enqueue(item);
        }

        for (int i = 0; i < k; i++) {
            StdOut.println(q.dequeue());
        }
    }

}
