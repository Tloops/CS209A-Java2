package labAHCSA;

import java.util.*;

public class SimulatedAnnealing {

    static int[][] board;
    static int[][] curBoard;
    static int curFitness;
    static boolean[][] isFixed;
    static boolean solutionFound;
    static int n;
    static int sum;
    static int x1;
    static int x2;
    static int y1;
    static int y2;
    
    static int[] fitColumn;
    static int[] fitRow;
    static int[] newFitColumn;
    static int[] newFitRow;

    public static void main(String[] args) {
        readBoard();
        long t1 = System.currentTimeMillis();
        while(!solutionFound) {
            initialize();
            simulatedAnnealingSolver();
            printBoard();
        }
        System.out.println((System.currentTimeMillis()-t1) + "ms");
    }

    public static void initialize(){
        fitColumn = new int[n*n];
        fitRow = new int[n*n];
        newFitColumn = new int[n*n];
        newFitRow = new int[n*n];
        
        curBoard = new int[board.length][board.length];
        for (int i = 0; i < n*n; i++)
            for (int j = 0; j < n*n; j++)
                curBoard[i][j] = board[i][j];
        for (int i = 0; i < n*n; i+=n)
            for (int j = 0; j < n*n; j+=n)
                initializeSmallSquare(i, j, i+n, j+n);
        curFitness = calculateFitness(curBoard);
    }

    private static void initializeSmallSquare(int x1, int y1, int x2, int y2){
        boolean[] hasNumber = new boolean[n*n+1];
        for (int i = x1; i < x2; i++) {
            for (int j = y1; j < y2; j++) {
                hasNumber[curBoard[i][j]] = true;
            }
        }
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 1; i <= n*n; i++)
            if(!hasNumber[i])
                list.add(i);
        Collections.shuffle(list);
        int cnt = 0;
        for (int i = x1; i < x2; i++) {
            for (int j = y1; j < y2; j++) {
                if (curBoard[i][j] == 0) {
                    curBoard[i][j] = list.get(cnt);
                    hasNumber[list.get(cnt)] = true;
                    cnt++;
                }
            }
        }
    }

    // t -> time
    // T -> Temperature
    public static void simulatedAnnealingSolver(){
        int noNew = 0;
        printBoard();
        System.out.println(curFitness);
        for (int t = 0; t < Integer.MAX_VALUE; t++) {
            double T = schedule(t);
            if(noNew >= 20000) {
                System.out.println(t);
                System.out.println(curFitness);
//                return;
                t = 0;
                noNew = 0;
            }
            int[][] neighbor = expand();
            int fitnessNext = newFitness(neighbor);
//            if (t % 1000 == 0) {
//                System.out.println("T: " + T);
//                System.out.println("Time: " + t);
////                System.out.println("FitnessNow: " + fitnessNow);
//                System.out.println("fitnessNext: " + fitnessNext);
////                printBoard();
//            }
            if (fitnessNext == 0){
                solutionFound = true;
                curBoard = neighbor;
                System.out.println(fitnessNext);
                return;
            }
            int delta = curFitness - fitnessNext;
            if(delta > 0 || probability(Math.exp(delta / T))){
                curFitness = fitnessNext;
                curBoard = neighbor;
                fitColumn[y1] = newFitColumn[y1];
                fitColumn[y2] = newFitColumn[y2];
                fitRow[x1] = newFitRow[x1];
                fitRow[x2] = newFitRow[x2];
                noNew = 0;
            }
            else
                noNew++;
        }
    }

    private static double schedule(int t){
//        return (200 * Math.exp(-0.0001 * t));
        return (40 * Math.pow(0.99, t));
    }

    private static int[][] expand(){
        Random random = new Random();
        int[][] neighbor = new int[curBoard.length][curBoard.length];
        for (int i = 0; i < curBoard.length; i++) {
            for (int j = 0; j < curBoard.length; j++) {
                neighbor[i][j] = curBoard[i][j];
            }
        }
        boolean isModified1 = false;
        boolean isModified2 = false;
        
        while (!isModified1 || !isModified2) {
            int x = random.nextInt(n) * n;
            int y = random.nextInt(n) * n;
            isModified1 = false;
            isModified2 = false;
            int count = 0;

            do {
                x1 = random.nextInt(n) + x;
                y1 = random.nextInt(n) + y;
                count++;
                if (!isFixed[x1][y1]) {
                    isModified1 = true;
                    break;
                }
            } while (count <= 10);

            count = 0;
            do {
                x2 = random.nextInt(n) + x;
                y2 = random.nextInt(n) + y;
                count++;
                if (!isFixed[x2][y2] && !(x2 == x1 && y2 == y1)) {
                    isModified2 = true;
                    break;
                }
            } while (count <= 10);
        }
        int temp = neighbor[x1][y1];
        neighbor[x1][y1] = neighbor[x2][y2];
        neighbor[x2][y2] = temp;
        return neighbor;
    }

    private static boolean probability(double p){
        return p > Math.random();
    }

    public static int calculateFitness(int[][] board){
        int fit = 0;
        int length = board.length;

        for (int i = 0; i < length; i++) {
            boolean[] hasOccurRow = new boolean[length];
            for (int j = 0; j < length; j++) {
                if(!hasOccurRow[board[i][j]-1])
                    hasOccurRow[board[i][j]-1] = true;
                else {
                    fitRow[i]++;
                    fit++;
                }
            }
        }

        for (int i = 0; i < length; i++) {
            boolean[] hasOccurColumn = new boolean[length];
            for (int j = 0; j < length; j++) {
                if(!hasOccurColumn[board[j][i]-1])
                    hasOccurColumn[board[j][i]-1] = true;
                else {
                    fitColumn[i]++;
                    fit++;
                }
            }
        }
        return fit;
    }
    
    public static int newFitness(int[][] board) {
        System.arraycopy(fitColumn, 0, newFitColumn, 0, n*n);
        System.arraycopy(fitRow, 0, newFitRow, 0, n*n);
        newFitColumn[y1] = 0;
        newFitColumn[y2] = 0;
        newFitRow[x1] = 0;
        newFitRow[x2] = 0;

        boolean[] hasOccurColumn = new boolean[board.length];
        for (int j = 0; j < board.length; j++) {
            if(!hasOccurColumn[board[j][y1]-1])
                hasOccurColumn[board[j][y1]-1] = true;
            else
                newFitColumn[y1]++;
        }

        if (y1 != y2) {
            hasOccurColumn = new boolean[board.length];
            for (int j = 0; j < board.length; j++) {
                if (!hasOccurColumn[board[j][y2] - 1])
                    hasOccurColumn[board[j][y2] - 1] = true;
                else
                    newFitColumn[y2]++;
            }
        }

        boolean[] hasOccurRow = new boolean[board.length];
        for (int j = 0; j < board.length; j++) {
            if(!hasOccurRow[board[x1][j]-1])
                hasOccurRow[board[x1][j]-1] = true;
            else
                newFitRow[x1]++;
        }

        if (x1 != x2) {
            hasOccurRow = new boolean[board.length];
            for (int j = 0; j < board.length; j++) {
                if (!hasOccurRow[board[x2][j] - 1])
                    hasOccurRow[board[x2][j] - 1] = true;
                else
                    newFitRow[x2]++;
            }
        }
        
        return curFitness - fitColumn[y1] + newFitColumn[y1] 
                - fitColumn[y2] + newFitColumn[y2]
                - fitRow[x1] + newFitRow[x1]
                - fitRow[x2] + newFitRow[x2];
    }

    public static void readBoard(){
        Scanner in = new Scanner(System.in);
        n = in.nextInt();
        sum = (1 + n*n) * n*n / 2;
        board = new int[n*n][n*n];
        isFixed = new boolean[n*n][n*n];
        for (int i = 0; i < n*n; i++) {
            for (int j = 0; j < n * n; j++) {
                board[i][j] = in.nextInt();
                if(board[i][j] != 0)
                    isFixed[i][j] = true;
            }
        }
        in.close();
    }

    public static void printBoard(){
        for (int i = 0; i < n*n; i++) {
            for (int j = 0; j < n*n; j++) {
                System.out.printf("%2d ", curBoard[i][j]);
            }
            System.out.println();
        }
    }
}
