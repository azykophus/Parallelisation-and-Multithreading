package org.example;



import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

import static org.example.BinaryTree.*;


class Node {

    int data;
    Node left, right;

    Node(int d) {
        data = d;
        left = right = null;
    }
}

class BinaryTree {

    static Node root;

    static Node arrayToBST(int[] arr, int start, int end) {
        if (start > end) {
            return null;
        }

        /* Get the middle element and make it root */
        int mid = (start + end) / 2;
        Node node = new Node(arr[mid]);

        node.left = arrayToBST(arr, start, mid - 1);
        node.right = arrayToBST(arr, mid + 1, end);

        return node;
    }

    static int height(Node root) {

        if (root == null)
            return 0;
        else {
            int lheight = height(root.left);
            int rheight = height(root.right);

            if (lheight > rheight)
                return (lheight + 1);
            else
                return (rheight + 1);
        }
    }

    Node generateTree(int N) {
        Random rand = new Random();
        int[] arr = new int[N];
        for (int i = 0; i < N; i++) {
            int val = rand.nextInt(2000000000) - 1000000000;
            arr[i] = val;
        }

        return arrayToBST(arr, 0, N - 1);
    }

    static boolean ifNodeExists(Node node, int key) {
        if (node == null)
            return false;

        if (node.data == key)
            return true;

        boolean res1 = ifNodeExists(node.left, key);

        //if node found, no need to look further
        if (res1) return true;

        //If node is not found in left
        return ifNodeExists(node.right, key);
    }

    public static void main(String[] args) {
        BinaryTree tree = new BinaryTree();
        String line = "----------------------------------------------------";
        int[] arr = new int[]{10, 1000, 1000000};
        System.out.println(line);
        System.out.println("Without Parallelization");
        System.out.println(line);
        for (int i = 0; i < arr.length; i++) {
            long startTimeForConstruction = System.nanoTime();
            root = tree.generateTree(arr[i]);
            long timeForTreeConstruction = System.nanoTime() - startTimeForConstruction;
            System.out.println("Time taken to construct the Tree : " + timeForTreeConstruction + "ns");
            System.out.println("Height of the tree is : " + height(root));

            //Generating a random number for searching
            Random rand = new Random();
            int val = rand.nextInt(2000000000) - 1000000000;
            long startTimeForSearching = System.nanoTime();
            boolean isNodeExists = ifNodeExists(root, val);
            long timeForSearching = System.nanoTime() - startTimeForSearching;
            System.out.println("Time taken to search for an element in a constructed tree is : " + timeForSearching + "ns");
            System.out.println();
        }

        System.out.println(line);
        System.out.println("With Parallelization using 2 threads");
        System.out.println(line);
        ForkJoinPool forkJoinPool = new ForkJoinPool(2);
        NewTask t = new NewTask(tree);
        root = forkJoinPool.invoke(t);
        System.out.println();
        forkJoinPool.shutdown();

        System.out.println(line);
        System.out.println("With Parallelization using 4 threads");
        System.out.println(line);
        ForkJoinPool forkJoinPool4 = new ForkJoinPool(4);
        NewTask t1 = new NewTask(tree);
        root = forkJoinPool4.invoke(t1);
        System.out.println();
    }
}

class NewTask extends RecursiveTask<Node>
{
    BinaryTree tree = new BinaryTree();
    NewTask(BinaryTree tree){
        this.tree = tree;
    }

    protected Node compute() {
        int[] arr = new int[]{10, 1000, 1000000};
        for (int i = 0; i < arr.length; i++) {
            long startTimeForConstruction = System.nanoTime();
            root = tree.generateTree(arr[i]);
            long timeForTreeConstruction = System.nanoTime() - startTimeForConstruction;
            System.out.println("Time taken to construct the Tree : " + timeForTreeConstruction + "ns");
            System.out.println("Height of the tree is : " + height(root));

            //Generating a random number for searching
            Random rand = new Random();
            int val = rand.nextInt(2000000000) - 1000000000;
            long startTimeForSearching = System.nanoTime();
            boolean isNodeExists = ifNodeExists(root, val);
            long timeForSearching = System.nanoTime() - startTimeForSearching;
            System.out.println("Time taken to search for an element in a constructed tree is : " + timeForSearching + "ns");
            System.out.println();
        }
        return root;
    }
}
