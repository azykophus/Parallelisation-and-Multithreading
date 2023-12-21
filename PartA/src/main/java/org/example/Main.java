package org.example;

import java.util.*;

public class Main
{
    public static void oddEvenSort(int n){
        final Double[] arrn = new Double[n];
        final Random rd = new Random();
        for (int i = 0; i < n; ++i) {
            arrn[i] = rd.nextDouble(10.0f);
        }

        for (int i = 0; i < n/2; i++ ) {
            for (int j = 0; j+1 < n; j += 2)
                if (arrn[j] > arrn[j+1]) {
                    Double T = arrn[j];
                    arrn[j] = arrn[j+1];
                    arrn[j+1] = T;
                }
            for (int j = 1; j+1 < arrn.length; j += 2)
                if (arrn[j] > arrn[j+1]) {
                    Double T = arrn[j];
                    arrn[j] = arrn[j+1];
                    arrn[j+1] = T;
                }
        }
    }



    public static int threading(final int n) throws InterruptedException {
        final Double[] arrn = new Double[n];
        Random rd = new Random();
        final Thread[] threads = new Thread[(n + 1) / 2];
        int i=0;
        while(i<n){
            arrn[i] = rd.nextDouble(10.0f);
            i++;
        }
        i=0;
        while(i<n){
            if (i % 2 == 0) {
                int j=0;
                while(j<=n/2 -1){

                    final runnable obj1 = new runnable(2 * j, 2 * j + 1, arrn);
                    (threads[j] = new Thread(obj1)).start();
                    j++;
                }
                j=0;
                while(j<n/2-1){
                    j++;
                    threads[j].join();
                }

            }
            else
            {
                int j=0;
                while(j<n/2 -1){
                    final runnable obj1 = new runnable(2 * j + 1, 2 * j + 2, arrn);
                    (threads[j] = new Thread(obj1)).start();
                    j++;
                }
                j=0;
                while(j<n/2 -1){
                    threads[j].join();
                    j++;
                }
            }
            i++;
        }
        return 0;
    }

    public static void main(final String[] args) throws InterruptedException {
        ArrayList<Integer> arr=new ArrayList<>();
        arr.add(1);
        arr.add(10);
        arr.add(100);
        arr.add(1000);
        arr.add(10000);
        long start;
        long end;
        long dif;

        for(int i=0;i<5;i++){
            start=System.nanoTime();
            oddEvenSort(arr.get(i));
            end=System.nanoTime();
            dif=end-start;
            System.out.println("for "+arr.get(i)+" terms without threads : "+dif+" ns");

            start=System.nanoTime();
            threading(arr.get(i));
            end=System.nanoTime();
            dif=end-start;
            System.out.println("for "+arr.get(i)+" terms with threads : "+dif+" ns\n");
        }
    }

    public static class runnable implements Runnable
    {
        final private Double[] arrn;
        final private int idx1;
        final private int idx2;

        public runnable(int idx1, int idx2, final Double[] arrn) {
            this.idx1 = idx1;
            this.idx2 = idx2;
            this.arrn = arrn;
        }

        @Override
        public void run() {
            if (this.arrn[this.idx1] > this.arrn[this.idx2]) {
                final Double temp = this.arrn[this.idx1];
                this.arrn[this.idx1] = this.arrn[this.idx2];
                this.arrn[this.idx2] = temp;
            }
        }
    }
}
